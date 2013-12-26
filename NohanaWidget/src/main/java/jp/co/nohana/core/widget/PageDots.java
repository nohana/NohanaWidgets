package jp.co.nohana.core.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.amalgam.view.ViewUtils;

/**
 * ページ位置を表すドット.
 * 
 * 以下のようにしてlayout XMLでプロパティを指定できます（省略可能）。
 * 
 * <pre>
 * &gt;RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
 *     xmlns:nohana="http://schemas.android.com/apk/res-auto"
 *     ...snip...
 *     >
 *     &gt;jp.co.nohana.view.PageDots
 *         android:layout_width="wrap_content"
 *         android:layout_height="wrap_content"
 *         ...snip...
 *         nohana:count="5"
 *         nohana:radius="2dp"
 *         nohana:interval="6dp"
 *         nohana:dot_color="#FFFFFFFF"
 *         nohana:selected_dot_color="#FF000000"
 *         />
 * </pre>
 * 
 * @author nohana Team
 * 
 */
@SuppressWarnings("unused") // public APIs
public class PageDots extends View {
    private static final int DEFAULT_SELECTED_DOT_INDEX = 0;
    private static final int DEFAULT_COUNT = 4;
    private static final int DEFAULT_RADIUS_DIP = 2;
    private static final int DEFAULT_INTERVAL_DIP = 6;
    private static final int DEFAULT_COLOR = Color.WHITE;
    private static final int DEFAULT_SELECTED_COLOR = Color.BLACK;
    /** ペイント */
    private final Paint mPaint;
    /** 選択中のドット位置 */
    private int mSelectedDot;
    /** ドットの数 */
    private int mDotCount;
    /** ドットの半径 */
    private int mDotRadius;
    /** ドットとドットの間隔 */
    private int mDotInterval;
    /** ドットのデフォルト色 */
    private int mDotColor;
    /** ドットの選択色 */
    private int mSelectedDotColor;

    public PageDots(Context context) {
        this(context, null);
    }

    public PageDots(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageDots(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PageDots);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mSelectedDot = array.getInt(R.styleable.PageDots_selectedDotIndex, DEFAULT_SELECTED_DOT_INDEX);
        mDotCount = array.getInt(R.styleable.PageDots_count, DEFAULT_COUNT);
        mDotRadius = array.getDimensionPixelSize(R.styleable.PageDots_dotRadius, ViewUtils.dipToPixel(getResources(), DEFAULT_RADIUS_DIP));
        mDotInterval = array.getDimensionPixelSize(R.styleable.PageDots_dotMargin, ViewUtils.dipToPixel(getResources(), DEFAULT_INTERVAL_DIP));
        mDotColor = array.getColor(R.styleable.PageDots_dotColor, DEFAULT_COLOR);
        mSelectedDotColor = array.getColor(R.styleable.PageDots_dotSelectedColor, DEFAULT_SELECTED_COLOR);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int diameter = 2 * mDotRadius;
        setMeasuredDimension(diameter * mDotCount + mDotInterval * (mDotCount - 1), diameter);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int diameter = 2 * mDotRadius;
        int x = mDotRadius;
        for (int i = 0; i < mDotCount; i++) {
            mPaint.setColor(mSelectedDot == i ? mSelectedDotColor : mDotColor);
            canvas.drawCircle(x, mDotRadius, mDotRadius, mPaint);
            x += mDotInterval + diameter;
        }
    }

    public int getSelectedDot() {
        return mSelectedDot;
    }

    public void setSelectedDot(int selectedDot) {
        mSelectedDot = selectedDot;
    }

    public int getDotCount() {
        return mDotCount;
    }

    public void setDotCount(int dotCount) {
        mDotCount = dotCount;
    }

    public int getDotRadius() {
        return mDotRadius;
    }

    public void setDotRadius(int dotRadius) {
        mDotRadius = dotRadius;
    }

    public int getDotInterval() {
        return mDotInterval;
    }

    public void setDotInterval(int dotInterval) {
        mDotInterval = dotInterval;
    }

    public void setDotColor(int dotColor) {
        mDotColor = dotColor;
    }

    public void setSelectedDotColor(int selectedDotColor) {
        mSelectedDotColor = selectedDotColor;
    }

    /**
     * 次のドットを選択する
     * 
     * @return 選択中のドット位置が変わった場合はtrue
     */
    public boolean moveNext() {
        if (mDotCount - 1 <= mSelectedDot) {
            return false;
        }
        mSelectedDot++;
        invalidate();
        return true;
    }

    /**
     * 前のドットを選択する
     * 
     * @return 選択中のドット位置が変わった場合はtrue
     */
    public boolean movePrev() {
        if (mSelectedDot <= 0) {
            return false;
        }
        mSelectedDot--;
        invalidate();
        return true;
    }

    public void setSelectedDotPosition(int position){
        if (position < 0 || position >= mDotCount) {
            return;
        }
        mSelectedDot = position;
        invalidate();
    }
}