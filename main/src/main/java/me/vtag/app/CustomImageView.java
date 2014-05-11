package me.vtag.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by anuraag on 9/5/14.

 */
public class CustomImageView extends ImageView {

    public static float radius = 18.0f;

    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //float radius = 36.0f;
        /*
        super.onDraw(canvas);

        Bitmap rounder = Bitmap.createBitmap(getWidth(),getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvasRound = new Canvas(rounder);
        canvasRound.drawColor(0xFFFFFFFF);

        Paint xferPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        xferPaint.setColor(Color.BLACK);

        final int rx = 60; //our x radius
        final int ry = 60; //our y radius

        canvasRound.drawRoundRect(new RectF(0,0,this.getWidth(),this.getHeight()), rx, ry, xferPaint);

        xferPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        canvas.drawBitmap(rounder, 0, 0, xferPaint);
        */
        setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        Path clipPath = new Path();
        RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        clipPath.addRoundRect(rect, 20, 20, Path.Direction.CW);
        canvas.clipPath(clipPath);
        super.onDraw(canvas);
    }
}