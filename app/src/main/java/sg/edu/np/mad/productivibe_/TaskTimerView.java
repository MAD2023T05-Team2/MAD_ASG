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

    private static final float THICKNESS_SCALE = 0.04f;

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

        // it reads the circleColor attribute from XML to set the color of the circular timer.
        // If not specified, it defaults to RED.
        int circleColor = Color.RED;

        // customize its behavior and appearance
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TaskTimerView);
            if (ta != null) {
                circleColor = ta.getColor(R.styleable.TaskTimerView_circleColor, circleColor);
                ta.recycle();
            }
        }

        // It uses two Paint objects, mCirclePaint and mEraserPaint,
        // for drawing the circular timer and the eraser effect, respectively.
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
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        // makes the view square by setting both the width and height,
        // even if it's used inside a rectangular container
    }

    @Override
    // creates a new Bitmap and Canvas to draw the circular timer and updates the bounds
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
    // responsible for drawing the circular timer on the canvas.
    protected void onDraw(Canvas canvas) {
        // It first clears the canvas with a transparent color.
        mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);

        // draws the arc of the circular timer
        if (mCircleSweepAngle > 0f) {
            mCanvas.drawArc(mCircleOuterBounds, ARC_START_ANGLE, mCircleSweepAngle, true, mCirclePaint);

            mCanvas.drawOval(mCircleInnerBounds, mEraserPaint);
            // To create the eraser effect, it draws an oval with the mEraserPaint,
            // which makes the portion inside the circle transparent
        }

        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    // When the timer is started, the ValueAnimator animates
    // from 0 to 1 (a full circle) over the specified duration.

    // obtains duration
    public long getDuration(){
        if(mTimerAnimator.isRunning()){
            return mTimerAnimator.getCurrentPlayTime()/1000;
        }
        else{
            return 0;
        }
    }

    // handles the start timer animation
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

    // handles the stop timer animation
    public void stop() {
        if (mTimerAnimator != null && mTimerAnimator.isRunning()) {
            mTimerAnimator.cancel();
            mTimerAnimator = null;

            drawProgress(0f);
        }
    }

    // handles the pause timer animation
    public void pause(){
        if (mTimerAnimator != null && mTimerAnimator.isRunning()) {
            mTimerAnimator.pause();
        }
    }

    // handles the resume timer animation
    public void resume(){
        if (mTimerAnimator != null && mTimerAnimator.isRunning()) {
            mTimerAnimator.resume();
        }
    }

    // updates the mCircleSweepAngle according to the animation progress
    // and calls invalidate() to redraw the view
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