package jp.co.nohana.core.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;

@SuppressWarnings("unused") // public APIs
public class CompoundImageView extends ImageView implements Checkable {
    private static final int ADDITIONAL_STATE_COUNT = 1;
    private static final int[] CHECKED_STATE_SET = { android.R.attr.state_checked };
    private boolean mChecked;
    private OnCheckedChangeListener mCheckedChangeListener;

    public CompoundImageView(Context context) {
        this(context, null);
    }

    public CompoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CompoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setClickable(true);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CompoundImageView);
        boolean checked = array.getBoolean(R.styleable.CompoundImageView_checked, false);
        setChecked(checked);
        array.recycle();
    }

    @Override
    public boolean performClick() {
        toggle();
        return super.performClick();
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            refreshDrawableState();
            if (mCheckedChangeListener != null) {
                mCheckedChangeListener.onCheckedChanged(this, mChecked);
            }
        }
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        int[] state = super.onCreateDrawableState(extraSpace + ADDITIONAL_STATE_COUNT);
        if (isChecked()) {
            mergeDrawableStates(state, CHECKED_STATE_SET);
        }
        return state;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mCheckedChangeListener = listener;
    }

    public static interface OnCheckedChangeListener {
        public void onCheckedChanged(CompoundImageView buttonView, boolean isChecked);
    }
}
