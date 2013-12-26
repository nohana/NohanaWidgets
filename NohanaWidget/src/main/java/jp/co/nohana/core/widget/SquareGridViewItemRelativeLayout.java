package jp.co.nohana.core.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

@SuppressWarnings("unused") // public APIs
public class SquareGridViewItemRelativeLayout extends RelativeLayout {
    public SquareGridViewItemRelativeLayout(Context context) {
        this(context, null);
    }

    public SquareGridViewItemRelativeLayout(Context context, AttributeSet attrs) {
        this(context, null, 0);
    }

    public SquareGridViewItemRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}