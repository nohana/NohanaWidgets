package jp.co.nohana.core.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to create a multiple-exclusion scope for a set of {@link CompoundImageView}s such as {@link android.widget.CheckBox}.
 * @author keishin.yokomaku
 *
 */
@SuppressWarnings("unused") // public APIs
public class CompoundImageViewGroup extends LinearLayout {
    private CompoundImageView.OnCheckedChangeListener mChildObserver;
    private PassThroughHierarchyChangeListener mHierarchyListener;
    private OnCheckedChangeListener mCheckListener;

    public CompoundImageViewGroup(Context context) {
        this(context, null);
    }

    public CompoundImageViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CompoundImageViewGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);

        mChildObserver = new ChildStateObserver();
        mHierarchyListener = new PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(mHierarchyListener);
    }

    @Override
    public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
        // the user listener is delegated to our pass-through listener
        mHierarchyListener.mOnHierarchyChangeListener = listener;
    }

    public int getCheckedCount() {
        int count = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (!(view instanceof CompoundImageView)) {
                continue;
            }
            CompoundImageView button = (CompoundImageView) view;
            if (button.isChecked()) {
                count++;
            }
        }
        return count;
    }

    public List<CompoundImageView> getCheckedViews() {
        List<CompoundImageView> buttons = new ArrayList<CompoundImageView>();
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (!(view instanceof CompoundImageView)) {
                continue;
            }
            CompoundImageView button = (CompoundImageView) view;
            buttons.add(button);
        }
        return buttons;
    }

    public List<Integer> getCheckedIndices() {
        List<Integer> indices = new ArrayList<Integer>();
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (!(view instanceof CompoundImageView)) {
                continue;
            }
            CompoundImageView button = (CompoundImageView) view;
            if (button.isChecked()) {
                indices.add(i);
            }
        }
        return indices;
    }

    public void uncheckAll() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (!(view instanceof CompoundImageView)) {
                continue;
            }
            CompoundImageView button = (CompoundImageView) view;
            button.setChecked(false);
        }
    }

    public boolean isChecked(int index) {
        View view = getChildAt(index);
        if (!(view instanceof CompoundImageView)) {
            return false;
        }
        CompoundImageView button = (CompoundImageView) view;
        return button.isChecked();
    }

    public void setCheckedChangeListener(OnCheckedChangeListener listener) {
        mCheckListener = listener;
    }

    private class PassThroughHierarchyChangeListener implements OnHierarchyChangeListener {
        private OnHierarchyChangeListener mOnHierarchyChangeListener;

        @Override
        public void onChildViewAdded(View parent, View child) {
            if (parent == CompoundImageViewGroup.this && child instanceof CompoundImageView) {
                int id = child.getId();
                if (id == View.NO_ID) {
                    id = child.hashCode();
                    child.setId(id);
                }
                ((CompoundImageView) child).setOnCheckedChangeListener(mChildObserver);
            }

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }

        @Override
        public void onChildViewRemoved(View parent, View child) {
            if (parent == CompoundImageViewGroup.this && child instanceof CompoundImageView) {
                ((CompoundImageView) child).setOnCheckedChangeListener(null);
            }
        }
    }

    private class ChildStateObserver implements CompoundImageView.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundImageView buttonView, boolean isChecked) {
            if (mCheckListener != null) {
                mCheckListener.onCheckedChanged(CompoundImageViewGroup.this);
            }
        }
    }

    public static interface OnCheckedChangeListener {
        public void onCheckedChanged(CompoundImageViewGroup group);
    }
}