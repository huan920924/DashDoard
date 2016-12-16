package com.lh.zmxyf;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import static android.graphics.Paint.Style.FILL;
import static android.graphics.Paint.Style.STROKE;

/**
 * -----------------------------------------------------------------------------------Author Info---
 * Company Name:          xjyy.
 * Author:                Liu Huan.
 * Email:                 771383629@qq.com.
 * Date:                  16-12-14 上午9:46.
 * -----------------------------------------------------------------------------------Message-------
 * If the following code to run properly, it is coding by Liu Huan.
 * otherwise I don't know.
 * -----------------------------------------------------------------------------------Class Info----
 * ClassName:             com.lh.zmxyf.
 * -----------------------------------------------------------------------------------Describe------
 * Function:              这货不是支付宝芝麻信用分仪表盘 ^-^.
 * -----------------------------------------------------------------------------------Modify--------
 * 16-12-14 上午9:46     Modified By liuhuan.
 * -----------------------------------------------------------------------------------End-----------
 */
public class DashDoardView extends View {

    private static final int DEFAULT_SIZE = 1600;//View默认大小单位px
    private static final int SCALE_PAINT_WIDTH = 8;//刻度画笔宽度
    private float mProportion = 1f;//缩放量

    private float mPointorOffset = 100;//指针偏移量
    private float mCurrentDegree;//当前划过的百分比
    private float mFractionMarginTop = 0;//分数距顶部距离
    private float mCustomMarginTop = 0;//自定义文字距顶部距离
    private float mDashEffectValueOne = 2.0f;//虚线的ValueOne
    private float mDashEffectValueTwo = 5f / 3;//虚线的ValueTwo
    private float[] mPosition;//扫描渲染定位0.0-1.0其中数组长度要和mSweepGradientColors长度一致
    private int[] mSweepGradientColors = new int[]{0x11FFFFFF, Color.WHITE};//扫描渲染颜色值-->这里选择了透明到白色
    private int mAnimDuration = 4000;//动画持续时间,默认4S.
    private int mStartAngle = 75;//进度盘开始角度
    private int mSweepAngle = 210;//进度盘划过最大角度
    private int mOutsideMargin = 50;//
    private int mInsideScaleLength = mOutsideMargin + 40;
    private int mWidth;//宽度
    private int mFractionTextSize = 20;
    private int mCustomTextColor = Color.WHITE;
    private int mFractionTextColor = Color.WHITE;
    private int mDefEndValue = 0;
    private String mFractionText;
    private String mCustomStr = "自定义表盘";
    /**
     * 字体
     */
    private Typeface mFractionTextTypeface;
    private Typeface mCustomTextTypeface;
    /**
     * 指针Bitmap
     */
    private Bitmap pointerBitmap;
    /**
     * 文本画笔
     */
    private TextPaint mCustomTextPaint;
    private TextPaint mFractionTextPaint;
    /**
     * 画笔
     */
    private Paint mInsideScalePaint;
    private Paint mTrianglePaint;
    private Paint mScalePaint;
    private Paint mRPAPaint;
    private Paint paint;
    /**
     * 路径
     */
    private Path mPath;
    /**
     * 值动画
     */
    private ValueAnimator mAnimator;
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 扫描渲染
     */
    private SweepGradient sweepGradient;
    /**
     * 虚线效果
     */
    private DashPathEffect dashPathEffect;
    /**
     * 高精度矩形 float
     */
    private RectF mTableRectF;
    /**
     * 矩形 int
     */
    private Rect mRect;

    public DashDoardView(Context context) {
        this(context, null);
    }

    public DashDoardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DashDoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        initPaint();
        startAnimator(0, 80, false, mAnimDuration);
    }

    public void setmStartAngle(int mStartAngle) {
        this.mStartAngle = mStartAngle;
        invalidate();
    }

    public void setmSweepAngle(int mSweepAngle) {
        this.mSweepAngle = mSweepAngle;
    }

    public void setmDashEffectValueOne(float mDashEffectValueOne) {
        this.mDashEffectValueOne = mDashEffectValueOne;
        invalidate();
    }

    public void setmDashEffectValueTwo(float mDashEffectValueTwo) {
        this.mDashEffectValueTwo = mDashEffectValueTwo;
        invalidate();
    }

    public void setmCustomStr(String mCustomStr) {
        this.mCustomStr = mCustomStr;
        invalidate();
    }

    public void setmAnimDuration(int mAnimDuration) {
        this.mAnimDuration = mAnimDuration;
    }

    public void setmSweepGradientColors(int[] mSweepGradientColors) {
        this.mSweepGradientColors = mSweepGradientColors;
    }

    public void setmProportion(float mProportion) {
        this.mProportion = mProportion;
        pointerBitmap = getPointerBitmap();
        invalidate();
    }

    public void setmOutsideMargin(int mOutsideMargin) {
        this.mOutsideMargin = mOutsideMargin;
    }

    public void setmFractionTextColor(int mFractionTextColor) {
        this.mFractionTextColor = mFractionTextColor;
    }

    public void setmPointorOffset(float mPointorOffset) {
        this.mPointorOffset = mPointorOffset;
        pointerBitmap = getPointerBitmap();
        invalidate();
    }

    public void setmFractionTextSize(int mFractionTextSize) {
        this.mFractionTextSize = mFractionTextSize;
        mFractionTextPaint.setTextSize(mFractionTextSize);
        invalidate();
    }

    public int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mTrianglePaint = new Paint();
        mTrianglePaint.setColor(Color.WHITE);//
        mTrianglePaint.setStyle(FILL);//
        mTrianglePaint.setAntiAlias(true);//抗锯齿
        mTrianglePaint.setStrokeWidth(1);

        mScalePaint = new Paint();
        mScalePaint.setColor(Color.WHITE);//
        mScalePaint.setStyle(STROKE);//
        mScalePaint.setAntiAlias(true);//抗锯齿
        mScalePaint.setStrokeWidth(SCALE_PAINT_WIDTH);
        mScalePaint.setAlpha(120);//透明度
        mScalePaint.setStrokeCap(Paint.Cap.ROUND);//让画笔变圆


        mRPAPaint = new Paint();
        mRPAPaint.setStyle(STROKE);//
        mRPAPaint.setAntiAlias(true);//抗锯齿
        mRPAPaint.setStrokeWidth(SCALE_PAINT_WIDTH);
        mRPAPaint.setStrokeCap(Paint.Cap.ROUND);

        mInsideScalePaint = new Paint();
        mInsideScalePaint.setColor(Color.WHITE);//
        mInsideScalePaint.setStyle(STROKE);//
        mInsideScalePaint.setAntiAlias(true);//抗锯齿
        mInsideScalePaint.setStrokeWidth(10);
        mInsideScalePaint.setAlpha(120);//透明度
        mInsideScalePaint.setDither(true);
        mInsideScalePaint.setStrokeCap(Paint.Cap.ROUND);

        paint = new Paint();
        paint.setTextSize(20);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(1);
        paint.setTextAlign(Paint.Align.CENTER);


        mFractionTextPaint = new TextPaint();
        mFractionTextPaint.setColor(mFractionTextColor);
        mFractionTextPaint.setTextSize(dp2px(mContext, mFractionTextSize));
        mFractionTextPaint.setTypeface(mFractionTextTypeface);

        mCustomTextPaint = new TextPaint();
        mCustomTextPaint.setTextSize(dp2px(mContext, 24));
        mCustomTextPaint.setColor(mCustomTextColor);
        mCustomTextPaint.setTypeface(mCustomTextTypeface);
    }

    public void setmCustomTextColor(int mCustomTextColor) {
        this.mCustomTextColor = mCustomTextColor;
    }

    private void init(Context context) {
        mRect = new Rect();
        mContext = context;
        mPath = new Path();
        mAnimator = new ValueAnimator();
        mFractionTextTypeface = Typeface.createFromAsset(getResources().getAssets(), "fonts/digital-7.ttf");
        mCustomTextTypeface = Typeface.createFromAsset(getResources().getAssets(), "fonts/OpenSans-Light.ttf");

    }

    public void setCustomTextSize(int textSize) {
        mCustomTextPaint.setTextSize(dp2px(mContext, textSize));
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = measureHanlder(widthMeasureSpec);
        int height = measureHanlder(heightMeasureSpec);

        setMeasuredDimension(mWidth, height);
    }

    /**
     * 定义一个方法处理  widthMeasureSpec,heightMeasureSpec的值
     * MeasureSpec.getSize()会解析MeasureSpec值得到父容器width或者height。
     * MeasureSpec.getMode()会得到三个int类型的值分别为:MeasureSpec.EXACTLY
     * MeasureSpec.AT_MOST,
     * MeasureSpec.UNSPECIFIED。
     * MeasureSpec.UNSPECIFIED 未指定，所以可以设置任意大小。
     * MeasureSpec.AT_MOST  View可以为任意大小，但是有一个上限。
     * MeasureSpec.EXACTLY 父容器为MeasureExampleView决定了一个大小，View大小只能在这个父容器限制的范围之内。
     *
     * @param measureSpec
     * @return
     */
    private int measureHanlder(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(px2dp(mContext, DEFAULT_SIZE), specSize);
        } else {
            result = px2dp(mContext, DEFAULT_SIZE);
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //位置方框
        mTableRectF = new RectF(mInsideScaleLength, mInsideScaleLength, mWidth - mInsideScaleLength, mWidth - mInsideScaleLength);
    }

    public void setmFractionMarginTop(float mFractionMarginTop) {
        this.mFractionMarginTop = dp2px(mContext, mFractionMarginTop);
        invalidate();
    }

    public void setmCustomMarginTop(float mCustomMarginTop) {
        this.mCustomMarginTop = dp2px(mContext, mCustomMarginTop);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mFractionText = "" + (int) (mCurrentDegree * 100);
        mFractionTextPaint.getTextBounds(mFractionText, 0, mFractionText.length(), mRect);
        canvas.drawText(mFractionText, mWidth / 2 - mRect.width() / 2, mFractionMarginTop == 0 ? mWidth / 4 : mFractionMarginTop, mFractionTextPaint);

        mCustomTextPaint.getTextBounds(mCustomStr, 0, mCustomStr.length(), mRect);
        canvas.drawText(mCustomStr, mWidth / 2 - mRect.width() / 2, mCustomMarginTop == 0 ? mWidth / 3 : mCustomMarginTop, mCustomTextPaint);


        //旋转画布顺时针90度
        canvas.rotate(90, mWidth / 2, mWidth / 2);
        //画外层刻度盘
        drawRingArc(canvas);
        //画外层进度值
        drawRingProgressArc(canvas, mCurrentDegree);
        //内测的虚线
        drawInsideScaleArc(canvas, mTableRectF);
        //画分数文本
        paintPercentText(canvas);


        drawPointerAnim(canvas, mCurrentDegree);

    }

    private void drawPointerAnim(Canvas canvas, float range) {
        canvas.restore();
        canvas.rotate((-270 + mStartAngle) + mSweepAngle * range, mWidth / 2, mWidth / 2);
        if (pointerBitmap == null || pointerBitmap.isRecycled()) {
            pointerBitmap = getPointerBitmap();
        }
        canvas.drawBitmap(pointerBitmap, mWidth / 2 - pointerBitmap.getWidth() / 2, 0, null);
    }

    private Bitmap getPointerBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(200, (int) (200 * mProportion) <= 0 ? 1 : (int) (200 * mProportion), Bitmap.Config.ARGB_8888);
        Canvas canvas1 = new Canvas(bitmap);
        drawPointer(canvas1);
        return bitmap;
    }

    public void startAnimator(int endValue, boolean restart, long duration) {
        startAnimator(mDefEndValue, endValue, restart, duration);


    }

    public void startAnimator(int startValue, int endValue, boolean restart, long duration) {

        if (restart) {//归0后重新开始
            mAnimator.setFloatValues(startValue, 0, endValue);
        } else {//不归0
            mAnimator.setFloatValues(startValue, endValue);
        }
        mAnimator.setDuration(duration);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentDegree = (0 + (Float) animation.getAnimatedValue()) / 100;
                invalidate();
            }
        });
        mAnimator.start();
        mDefEndValue = endValue;
    }

    public void setAnimatorStyle(TimeInterpolator interpolator) {
        mAnimator.setInterpolator(interpolator);
        startAnimator(mDefEndValue, true, 5000);
    }

    /**
     * 画一个内部虚化线段
     *
     * @param canvas
     * @param mTableRectF
     */
    private void drawInsideScaleArc(Canvas canvas, RectF mTableRectF) {
        mPath.reset();
        mPath.addArc(mTableRectF, mStartAngle, mSweepAngle);
        //计算路径的长度
        PathMeasure pathMeasure = new PathMeasure(mPath, false);
        float length = pathMeasure.getLength();
        float step = length / 100;
        dashPathEffect = new DashPathEffect(new float[]{step / mDashEffectValueOne, step * mDashEffectValueTwo}//虚线数组
                , 0//初始偏移量
        );
        mInsideScalePaint.setAlpha(120);
        mInsideScalePaint.setPathEffect(dashPathEffect);
        canvas.drawPath(mPath, mInsideScalePaint);
    }

    /**
     * 画一个内部带进度的虚化线段
     *
     * @param canvas
     * @param mTableRectF
     */
    private void drawProgressISArc(Canvas canvas, RectF mTableRectF, float range) {
        mPath.reset();
        //在油表路径中增加一个从起始弧度
        mPath.addArc(mTableRectF, mStartAngle, mSweepAngle * range);
        //计算路径的长度
        //PathMeasure pathMeasure = new PathMeasure(mPath, false);
        //float length = pathMeasure.getLength();
        //float step = pathLength*range / 100*range;
        float step = 20;
        dashPathEffect = new DashPathEffect(new float[]{step / 2, step * 5 / 3}//虚线数组
                , 0//初始偏移量
        );
        mInsideScalePaint.setAlpha(180);
        mInsideScalePaint.setPathEffect(dashPathEffect);
        //mInsideScalePaint.setShader(mColorShader);
        canvas.drawPath(mPath, mInsideScalePaint);
    }

    private void paintPercentText(Canvas canvas) {
        canvas.save();
        //canvas.rotate(-90, mWidth / 2, mWidth / 2);
        canvas.rotate(-270 + mStartAngle, mWidth / 2, mWidth / 2);
        int angle = mSweepAngle / 10;
        for (int i = 0; i < 11; i++) {
            //保存画布
            canvas.save();
            canvas.rotate(angle * i, mWidth / 2, mWidth / 2);
            //画文字
            canvas.drawText(10 * i + "分", mWidth / 2, 80, paint);
            canvas.restore();
        }
    }

    private void drawRingArc(Canvas canvas) {

        RectF oval1 = new RectF(50, 50, mWidth - 50, mWidth - 50);

        canvas.drawArc(oval1, mStartAngle, mSweepAngle, false, mScalePaint);//弧形
    }

    private void drawRingProgressArc(Canvas canvas, float range) {
        //这个数组长度要和SWEEP_GRADIENT_COLORS数组长度一致
        mPosition = new float[]{mStartAngle * 1.0f / 360, (mStartAngle * 1.0f + mSweepAngle * range) / 360};
        sweepGradient = new SweepGradient(mWidth / 2, mWidth / 2, mSweepGradientColors, mPosition);

        mRPAPaint.setShader(sweepGradient);
        RectF oval1 = new RectF(mOutsideMargin, mOutsideMargin, mWidth - mOutsideMargin, mWidth - mOutsideMargin);

        canvas.drawArc(oval1, mStartAngle, mSweepAngle * range, false, mRPAPaint);//弧形
    }

    /**
     * 画指针样式
     *
     * @param canvas
     */
    private void drawPointer(Canvas canvas) {
        float canvasWidth = canvas.getWidth();
        float radius = 10;
        float triangleHeight = radius * 3;
        float x = canvasWidth / 2;
        float startOffset =
                x -
                        (radius * mProportion);
        //canvas.saveLayer会返回一个int值 用于表示layer的ID(有ps知识的可以将它理解为ps中的图层)
        int layerId = canvas.saveLayer(startOffset,             //left
                mPointorOffset,                                  //top
                startOffset + (radius * mProportion) * 2,       //right
                mPointorOffset + (radius * mProportion) +
                        triangleHeight * mProportion,           //bottom
                null, Canvas.ALL_SAVE_FLAG);

        RectF oval = new RectF(startOffset,
                mPointorOffset + triangleHeight * mProportion - (radius * mProportion),
                startOffset + (radius * mProportion) * 2,
                mPointorOffset + triangleHeight * mProportion + radius * mProportion
        );
        canvas.drawArc(oval, 0, 180, false, mTrianglePaint);//小弧形

        Path path = new Path();

        path.moveTo(
                x
                , mPointorOffset);// 此点为多边形的起点
        path.lineTo(
                x
                        - radius * mProportion, mPointorOffset + triangleHeight * mProportion);
        path.lineTo(x + radius * mProportion, mPointorOffset + triangleHeight * mProportion);
        path.close(); // 使这些点构成封闭的多边形(三角形)
        canvas.drawPath(path, mTrianglePaint);
        //圆
        mTrianglePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mTrianglePaint.setColor(Color.WHITE);
        canvas.drawCircle(x//圆心X坐标
                , mPointorOffset + triangleHeight * mProportion//圆心Y坐标
                , (radius - 4) * mProportion//半径
                , mTrianglePaint);
        //最后将画笔去除Xfermode
        mTrianglePaint.setXfermode(null);
        canvas.restoreToCount(layerId);
    }

    public int px2dp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
