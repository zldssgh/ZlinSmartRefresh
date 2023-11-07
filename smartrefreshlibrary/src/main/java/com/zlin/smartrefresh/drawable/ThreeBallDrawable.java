package com.zlin.smartrefresh.drawable;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.NonNull;

import com.zlin.smartrefresh.api.config.BallInfoConfig;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class ThreeBallDrawable extends PaintDrawable implements Animatable , ValueAnimator.AnimatorUpdateListener{

    protected int mWidth = 0;
    protected int mHeight = 0;
    protected int mProgressDegree = 0;
    private ValueAnimator mValueAnimator;
    protected Path mPath = new Path();

    private ScheduledExecutorService scheduledExecutorService;
    private ScheduledFuture<?> scheduledFuture;

    private Handler handler=new Handler(Looper.getMainLooper());

    private ArrayList<Integer> listRandom=new ArrayList<>();

    private float ballRadius=0f;
    private float ballHgap=0f;
    private float ballVgap=0f;

    private float width=0f;
    private float height=0f;

    public ThreeBallDrawable(BallInfoConfig ballInfoConfig) {
        ballRadius=ballInfoConfig.getBallRadius();
        ballHgap=ballInfoConfig.getBallHgap();
        ballVgap=ballInfoConfig.getBallVgap();
        width=ballInfoConfig.getViewWidth();
        height=ballInfoConfig.getViewHeight();


//        mValueAnimator = ValueAnimator.ofInt(30, 3600);
//        mValueAnimator.setDuration(90000);
//        mValueAnimator.setInterpolator(null);
//        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
//        mValueAnimator.setRepeatMode(ValueAnimator.RESTART);

        scheduledExecutorService = Executors.newScheduledThreadPool(1);
    }


    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        int value = (int) animation.getAnimatedValue();
        Log.e("Animation1","value="+value);
        mProgressDegree = 30 * (value / 30);
        final Drawable drawable = ThreeBallDrawable.this;
        drawable.invalidateSelf();
    }


    private  void randomNumberGenerator () {
        listRandom.clear();

        int n = 3; // 需要生成的不重复随机数的数量
        int maxNumber = 3; // 随机数的上限

        Random random = new Random();
        for (int i = 0; i < n; i++) {
            int randomNumber = random.nextInt(maxNumber);
            while (listRandom.contains(randomNumber)) {
                randomNumber = random.nextInt(maxNumber);
            }
            listRandom.add(randomNumber);
        }
    }

    private float randomInt(float sFloat, float eFloat){
        int min_val = (int) sFloat;
        int max_val = (int) eFloat;
//        SecureRandom rand = new SecureRandom();
//        rand.setSeed(new Date().getTime());
//        int randomNum = rand.nextInt((max_val - min_val) + 1) + min_val;

        ThreadLocalRandom tlr = ThreadLocalRandom.current();
        int randomNum = tlr.nextInt(min_val, max_val );
        return randomNum;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        final Drawable drawable = ThreeBallDrawable.this;
        final Rect bounds = drawable.getBounds();
        //bounds.width();
        //bounds.height()*2/3;

        final float sFloat=ballRadius;
        final float mFload=height/2;
        final float eFloat=(height-ballRadius);
        final ArrayList<Float> list=new ArrayList<>();
        list.add(sFloat);
        list.add(mFload);
        list.add(eFloat);

         float value1=randomInt(sFloat,eFloat);
         float value2=randomInt(sFloat,eFloat);
         float value3=randomInt(sFloat,eFloat);
        Log.e("Animation2","中心点范围="+sFloat+"-"+eFloat+">>>"+value1+"/"+value2+"/"+value3+"---");


//        randomNumberGenerator();
//        float value1=list.get(listRandom.get(0));
//        float value2=list.get(listRandom.get(1));
//        float value3=list.get(listRandom.get(2));
//        Log.e("Animation2","中心点范围="+sFloat+"-"+eFloat+">>>"+listRandom.get(0)+"/"+listRandom.get(1)+"/"+listRandom.get(2)+"---");

        Log.e("Animation2","scheduledExecutorService="+scheduledExecutorService);
        Log.e("Animation2","scheduledFuture="+scheduledFuture);

        mPath.reset();
        mPath.addCircle(ballRadius, value1, ballRadius, Path.Direction.CW);
        mPath.addCircle(3*ballRadius+ballHgap, value2, ballRadius, Path.Direction.CW);
        mPath.addCircle(5*ballRadius+2*ballHgap, value3, ballRadius, Path.Direction.CW);

        //canvas.save();
        canvas.drawPath(mPath, mPaint);
        //canvas.restore();
    }
    //</editor-fold>

    @Override
    public void start() {
//        if (!mValueAnimator.isRunning()) {
//            mValueAnimator.addUpdateListener(this);
//            mValueAnimator.start();
//        }

        scheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                invalidateSelf();
            }
        }, 0, 200, TimeUnit.MILLISECONDS);
    }

    @Override
    public void stop() {
//        if (mValueAnimator.isRunning()) {
//            Animator animator = mValueAnimator;
//            animator.removeAllListeners();
//            mValueAnimator.removeAllUpdateListeners();
//            mValueAnimator.cancel();
//        }

        try{
            scheduledFuture.cancel(true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean isRunning() {
        //return mValueAnimator.isRunning();
        return scheduledExecutorService.isShutdown()==false;
    }

}
