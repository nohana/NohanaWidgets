package jp.co.nohana.core.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.LinearLayout;

/**
 * This view group contains wizard step views to indicate the current step.
 * Each wizard step view implements {@link android.widget.Checkable}, so current step is treated as checked state.
 * Ideally, we need to define our own state such as state_current_step, or state_done_step, state_undone_step...
 *
 * This group manages the current step position, and set the position results to check all steps up to the item at the position.
 * @author keishin.yokomaku
 */
public class WizardStepGroup extends LinearLayout {
    private OnClickListener mChildObserver;
    private OnHierarchyChangeListener mHierarchyListener;
    private int mCurrentPosition;
    private OnWizardStepSelectedListener mStepListener;

    public WizardStepGroup(Context context) {
        this(context, null);
    }

    public WizardStepGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WizardStepGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);

        mChildObserver = new ChildStateObserver();
        mHierarchyListener = new PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(mHierarchyListener);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.WizardStepGroup, defStyle, 0);
        mCurrentPosition = array.getInt(R.styleable.WizardStepGroup_firstStepPosition, 0);
        array.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        setCurrentStepAt(mCurrentPosition, true);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof WizardStepState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        WizardStepState own = (WizardStepState) state;
        super.onRestoreInstanceState(own.getSuperState());
        int position = own.getCurrentPosition();
        setCurrentStepAt(position, true);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        WizardStepState state = new WizardStepState(super.onSaveInstanceState());
        state.setCurrentPosition(mCurrentPosition);
        return state;
    }

    public int getChildStepsCound() {
        int count = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof Checkable) {
                count++;
            }
        }
        return count;
    }

    public void setCurrentStepAt(int position, boolean notifyTabSelection) {
        if (position >= getChildCount() || position < 0) {
            // not capable
            return;
        }

        View selected = getChildAt(position);
        mCurrentPosition = position;
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (!(view instanceof Checkable)) {
                continue;
            }
            Checkable checkable = (Checkable) view;
            checkable.setChecked(i <= position);
        }
        if (mStepListener != null && notifyTabSelection) {
            mStepListener.onTabSelected(selected, selected.getId(), position);
        }
    }

    public int getCurrentStepId() {
        View view = getChildAt(getCurrentStepAt());
        return view.getId();
    }

    public int getCurrentStepAt() {
        return mCurrentPosition;
    }

    public int getViewPosition(View v) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view.equals(v)) {
                return i;
            }
        }
        return -1;
    }

    public void nextStep() {
        setCurrentStepAt(getCurrentStepAt() + 1, true);
    }

    public void previousStep() {
        setCurrentStepAt(getCurrentStepAt() - 1, true);
    }

    public void setOnWizardStepSelectedListener(OnWizardStepSelectedListener listener) {
        mStepListener = listener;
    }

    private class ChildStateObserver implements OnClickListener {
        @Override
        public void onClick(View v) {
            int position = getViewPosition(v);
            setCurrentStepAt(position, true);
        }
    }

    private class PassThroughHierarchyChangeListener implements OnHierarchyChangeListener {
        @Override
        public void onChildViewAdded(View parent, View child) {
            if (parent == WizardStepGroup.this && child instanceof Checkable) {
                child.setOnClickListener(mChildObserver);
                
            }
        }

        @Override
        public void onChildViewRemoved(View parent, View child) {
            if (parent == WizardStepGroup.this && child instanceof Checkable) {
                child.setOnClickListener(null);
            }
        }
    }

    public static interface OnWizardStepSelectedListener {
        public void onTabSelected(View view, int id, int position);
    }

    private static class WizardStepState extends BaseSavedState {
        @SuppressWarnings("unused")
        public static final Creator<WizardStepState> CREATOR = new Creator<WizardStepState>() {
            @Override
            public WizardStepState[] newArray(int size) {
                return new WizardStepState[size];
            }

            @Override
            public WizardStepState createFromParcel(Parcel source) {
                return new WizardStepState(source);
            }
        };
        private int mCurrentPosition;

        public WizardStepState(Parcel source) {
            super(source);
            mCurrentPosition = source.readInt();
        }

        public WizardStepState(Parcelable source) {
            super(source);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(mCurrentPosition);
        }

        public int getCurrentPosition() {
            return mCurrentPosition;
        }

        public void setCurrentPosition(int currentPos) {
            mCurrentPosition = currentPos;
        }
    }
}