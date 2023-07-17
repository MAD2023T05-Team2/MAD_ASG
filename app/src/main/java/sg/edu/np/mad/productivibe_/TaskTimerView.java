package sg.edu.np.mad.productivibe_;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class TaskTimerView extends View {

    private static final int ARC_START_ANGLE = 270; // 12 o'clock

    private static final float THICKNESS_SCALE = 0.03f;

    private Bitmap mBitmap;
    private Canvas mCanvas;

    private RectF mCircleOuterBounds;
    private RectF mCircleInnerBounds;

    private Paint mCirclePaint;
    private Paint mEraserPaint;

    private float mCircleSweepAngle;

    private ValueAnimator mTimerAnimator;

    private TaskTimerListener mListener;

    public TaskTimerView(Context context) {
        this(context, null);
    }

    public TaskTimerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TaskTimerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        int circleColor = Color.RED;

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TaskTimerView);
            if (ta != null) {
                circleColor = ta.getColor(R.styleable.TaskTimerView_circleColor, circleColor);
                ta.recycle();
            }
        }

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(circleColor);

        mEraserPaint = new Paint();
        mEraserPaint.setAntiAlias(true);
        mEraserPaint.setColor(Color.TRANSPARENT);
        mEraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    public void setTaskTimerListener(TaskTimerListener listener){
        this.mListener = listener;
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec); // Trick to make the view square
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw || h != oldh) {
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mBitmap.eraseColor(Color.TRANSPARENT);
            mCanvas = new Canvas(mBitmap);
        }

        super.onSizeChanged(w, h, oldw, oldh);
        updateBounds();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);

        if (mCircleSweepAngle > 0f) {
            mCanvas.drawArc(mCircleOuterBounds, ARC_START_ANGLE, mCircleSweepAngle, true, mCirclePaint);
            mCanvas.drawOval(mCircleInnerBounds, mEraserPaint);
        }

        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    public long getDuration(){
        if(mTimerAnimator.isRunning()){
            return mTimerAnimator.getCurrentPlayTime()/1000;
        }
        else{
            return 0;
        }
    }


    public void start(int secs) {
        stop();

        mTimerAnimator = ValueAnimator.ofFloat(0f, 1f);
        mTimerAnimator.setDuration(TimeUnit.SECONDS.toMillis(secs));
        mTimerAnimator.setInterpolator(new LinearInterpolator());
        mTimerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                drawProgress((float) animation.getAnimatedValue());
                mListener.onTaskTimerUpdate(getDuration());

            }
        });
        mTimerAnimator.start();
    }

    public void stop() {
        if (mTimerAnimator != null && mTimerAnimator.isRunning()) {
            mTimerAnimator.cancel();
            mTimerAnimator = null;

            drawProgress(0f);
        }
    }

    public void pause(){
        if (mTimerAnimator != null && mTimerAnimator.isRunning()) {
            mTimerAnimator.pause();
        }
    }

    public void resume(){
        if (mTimerAnimator != null && mTimerAnimator.isRunning()) {
            mTimerAnimator.resume();
        }
    }

    private void drawProgress(float progress) {
        mCircleSweepAngle = 360 * progress;

        invalidate();
    }

    private void updateBounds() {
        final float thickness = getWidth() * THICKNESS_SCALE;

        mCircleOuterBounds = new RectF(0, 0, getWidth(), getHeight());
        mCircleInnerBounds = new RectF(
                mCircleOuterBounds.left + thickness,
                mCircleOuterBounds.top + thickness,
                mCircleOuterBounds.right - thickness,
                mCircleOuterBounds.bottom - thickness);

        invalidate();
    }
}