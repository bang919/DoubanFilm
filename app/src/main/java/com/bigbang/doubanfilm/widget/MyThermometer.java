package com.bigbang.doubanfilm.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.bigbang.doubanfilm.R;


/**
 * Created by Administrator on 2018/2/8.
 */

public class MyThermometer extends View {

    private int mCrustColor;
    private int mLiquidColor;
    private float mInitTemperature;

    /**
     * 定义温度计感温泡的位置
     */

    float circleX;
    float circleY;
    //创建画笔
    Paint p;

    Paint lp;

    public MyThermometer(Context context) {
        this(context, null);
    }

    public MyThermometer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 获取自定义属性
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public MyThermometer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        //获取自定义属性
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyThermometer, defStyle, 0);
        int n = typedArray.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = typedArray.getIndex(i);

            switch (attr) {
                //设置默认颜色为蓝色
                case R.styleable.MyThermometer_liquid_color:
                    mLiquidColor = typedArray.getColor(attr, Color.BLUE);
                    break;
                //设置默认颜色为黑色
                case R.styleable.MyThermometer_crust_color:
                    mCrustColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                //设置温度计默认温度
                case R.styleable.MyThermometer_init_temperature:
                    mInitTemperature = typedArray.getFloat(attr, 0);
                    if (mInitTemperature > 1)
                        mInitTemperature = 1;
                    break;
            }
        }
        typedArray.recycle();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        /**
         * 先画温度计外壳
         */
        int strokeWidth = 2;
        p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setColor(mCrustColor);
        // p.setColor(Color.BLUE);
        p.setAntiAlias(true);
        p.setStrokeWidth(strokeWidth);

        //温度计的位置
        circleX = canvas.getWidth() / 2;
        circleY = canvas.getHeight() - canvas.getWidth() / 2;
        float mRadius = canvas.getWidth() / 2 - strokeWidth;
        canvas.drawCircle(circleX, circleY, mRadius, p);

        canvas.drawRect(circleX - mRadius * 0.5f, strokeWidth, circleX + mRadius * 0.5f, circleY - mRadius, p);

        /**
         * 画温度计里的液体
         */
        lp = new Paint();

        lp.setStyle(Paint.Style.FILL);
        lp.setColor(mLiquidColor);
        lp.setAntiAlias(true);

        float mLRadius = mRadius * 0.8f;
        canvas.drawCircle(circleX, circleY, mLRadius, lp);
        //圆球里面的小长方形
        canvas.drawRect(circleX - mRadius * 0.5f + mRadius * 0.2f, circleY - mRadius, circleX + mRadius * 0.5f - mRadius * 0.2f, circleY, lp);
        //进度条里面的大长方形
        float length = mInitTemperature * (circleY - mRadius * 1.3f);
        canvas.drawRect(circleX - mRadius * 0.5f + mRadius * 0.2f, circleY - mRadius - length, circleX + mRadius * 0.5f - mRadius * 0.2f, circleY - mRadius, lp);
    }

    public void setTemperature(float mTemperature) {


        mInitTemperature = mTemperature;
        postInvalidate();

    }
}
