package com.zlin.smartrefresh.drawable;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.NonNull;
import com.zlin.smartrefresh.utils.RandomUtils;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ThreeBallDrawable extends PaintDrawable implements Animatable , ValueAnimator.AnimatorUpdateListener{
    private String TAG="ThreeBallDrawable";

    protected int mProgressDegree = 0;
    private ValueAnimator mValueAnimator;
    protected Path mPath = new Path();

    private ScheduledExecutorService scheduledExecutorService;
    private ScheduledFuture<?> scheduledFuture;

    private Handler handler=new Handler(Looper.getMainLooper());

    private ArrayList<Integer> listRandom=new ArrayList<>();

    private boolean isOld=false;

    public ThreeBallDrawable() {
        if (isOld){
            mValueAnimator = ValueAnimator.ofInt(1, 8);
            mValueAnimator.setDuration(30*60*1000);
            mValueAnimator.setInterpolator(null);
            mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
            mValueAnimator.setRepeatCount(-1);
        }else{
            scheduledExecutorService = Executors.newScheduledThreadPool(1);
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        int value = (int) animation.getAnimatedValue();
        Log.e(TAG,"value="+value);

        mProgressDegree = 30 * (value / 30);

        final Drawable drawable = ThreeBallDrawable.this;
        drawable.invalidateSelf();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        //canvas.drawColor(Color.BLACK);

        final Drawable drawable = ThreeBallDrawable.this;
        final Rect bounds = drawable.getBounds();
        //bounds.width();
        //bounds.height()

        final float sFloat= mBallRadius;
        final float mFload= mDrawableHeight /2;
        final float eFloat=(mDrawableHeight - mBallRadius);
        final ArrayList<Float> list=new ArrayList<>();
        list.add(sFloat);
        list.add(mFload);
        list.add(eFloat);

         float value1= RandomUtils.randomInt(sFloat,eFloat);
         float value2= RandomUtils.randomInt(sFloat,eFloat);
         float value3= RandomUtils.randomInt(sFloat,eFloat);
        Log.e("Animation2","中心点范围="+sFloat+"-"+eFloat+">>>"+value1+"/"+value2+"/"+value3+"---");


//        randomNumberGenerator();
//        float value1=list.get(listRandom.get(0));
//        float value2=list.get(listRandom.get(1));
//        float value3=list.get(listRandom.get(2));
//        Log.e("Animation2","中心点范围="+sFloat+"-"+eFloat+">>>"+listRandom.get(0)+"/"+listRandom.get(1)+"/"+listRandom.get(2)+"---");

        Log.e("Animation3","scheduledExecutorService="+scheduledExecutorService);
        Log.e("Animation3","scheduledFuture="+scheduledFuture);

        mPath.reset();
        mPath.addCircle(mBallRadius, value1, mBallRadius, Path.Direction.CW);
        mPath.addCircle(3* mBallRadius + mBallHgap, value2, mBallRadius, Path.Direction.CW);
        mPath.addCircle(5* mBallRadius +2* mBallHgap, value3, mBallRadius, Path.Direction.CW);

        //canvas.save();
        canvas.drawPath(mPath, mPaint);
        //canvas.restore();
    }

    @Override
    public void start() {
        if (isOld){
            if (!mValueAnimator.isRunning()) {
                mValueAnimator.addUpdateListener(this);
                mValueAnimator.start();
           }
        }else{
            if (scheduledExecutorService==null){
                scheduledExecutorService = Executors.newScheduledThreadPool(1);
            }
            scheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    Log.e("Animation4","执行55555555555555555555555555555555555555555555555555555555555");
                    invalidateSelf();
                }
            }, 0, 200, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void stop() {
        if (isOld){
            if (mValueAnimator.isRunning()) {
                Animator animator = mValueAnimator;
                animator.removeAllListeners();
                mValueAnimator.removeAllUpdateListeners();
                mValueAnimator.cancel();
           }
        }else{
            Log.e("Animation4","正在停止");
            try{
                if (scheduledFuture!=null && !scheduledFuture.isCancelled()){
                    Log.e("Animation4","执行001");
                    scheduledFuture.cancel(true);
                    Log.e("Animation4","执行002");
                    scheduledFuture=null;
                    Log.e("Animation4","执行003");
                }

                if (scheduledExecutorService!=null && !scheduledExecutorService.isShutdown()){
                    Log.e("Animation4","执行201");
                    scheduledExecutorService.shutdownNow();
                    Log.e("Animation4","执行202");
                }

                Log.e("Animation4","执行301");
            }catch (Exception e){
                e.printStackTrace();
                Log.e("Animation4","错误:"+e.getMessage());
            }finally {
                scheduledExecutorService=null;
            }
        }
    }

    @Override
    public boolean isRunning() {
        if (isOld){
            return mValueAnimator.isRunning();
        }else{
            return scheduledFuture!=null && !scheduledFuture.isCancelled();
        }
    }

    public void closeTask(){
        try{
            stop();

            if (scheduledExecutorService!=null && !scheduledExecutorService.isShutdown()){
                scheduledExecutorService.shutdownNow();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
