package jp.co.nohana.core.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class MaskOverlayImageView extends ImageView {
    private Bitmap mMaskOverlay;
    private Paint mMaskPaint;
    private Paint mImagePaint;

    public MaskOverlayImageView(Context context) {
        this(context, null);
    }

    public MaskOverlayImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaskOverlayImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mMaskPaint = new Paint();
        mImagePaint = new Paint();
        mMaskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mImagePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // we cannot do anything without bitmaps.
        if (mMaskOverlay == null) {
            return;
        }
        if (getDrawable() == null) {
            return;
        }

        canvas.save();
        canvas.drawBitmap(mMaskOverlay, 0, 0, mMaskPaint);
        canvas.drawBitmap(((BitmapDrawable) getDrawable()).getBitmap(), 0, 0, mImagePaint);
        canvas.restore();
    }

    public void setMaskOverlay(Bitmap bitmap) {
        mMaskOverlay = bitmap;
        invalidate();
    }
}