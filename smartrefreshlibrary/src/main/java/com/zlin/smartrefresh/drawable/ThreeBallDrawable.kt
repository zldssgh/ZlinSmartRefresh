package com.zlin.smartrefresh.drawable

import android.animation.Animator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import com.zlin.smartrefresh.utils.RandomUtils.randomInt
import com.zlin.smartrefresh.utils.SelfLogUtils.log
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class ThreeBallDrawable : PaintDrawable(), Animatable, AnimatorUpdateListener {
    private val TAG = "ThreeBallDrawable"

    private var mProgressDegree = 0
    private var mValueAnimator: ValueAnimator? = null
    private var mPath = Path()
    private var scheduledExecutorService: ScheduledExecutorService? = null
    private var scheduledFuture: ScheduledFuture<*>? = null
    private val handler = Handler(Looper.getMainLooper())
    private val listRandom = ArrayList<Int>()
    private val isOld = false

    init {
        if (isOld) {
            mValueAnimator = ValueAnimator.ofInt(1, 8)
            mValueAnimator?.duration = (30 * 60 * 1000).toLong()
            mValueAnimator?.interpolator = null
            mValueAnimator?.repeatMode = ValueAnimator.RESTART
            mValueAnimator?.repeatCount = -1
        } else {
            scheduledExecutorService = Executors.newScheduledThreadPool(1)
        }
    }

    override fun onAnimationUpdate(animation: ValueAnimator) {
        val value = animation.animatedValue as Int
        log(TAG, "value=$value")
        mProgressDegree = 30 * (value / 30)
        val drawable: Drawable = this@ThreeBallDrawable
        drawable.invalidateSelf()
    }

    override fun draw(canvas: Canvas) {
        if (mCanvasColor != -1) {
            canvas.drawColor(mCanvasColor)
        }
        val drawable: Drawable = this@ThreeBallDrawable
        val bounds = drawable.bounds
        //bounds.width();
        //bounds.height()
        val sFloat = mBallRadius
        val mFload = mDrawableHeight / 2
        val eFloat = mDrawableHeight - mBallRadius
        val list = ArrayList<Float>()
        list.add(sFloat)
        list.add(mFload)
        list.add(eFloat)
        val value1 = randomInt(sFloat, eFloat)
        val value2 = randomInt(sFloat, eFloat)
        val value3 = randomInt(sFloat, eFloat)
        log("Animation2", "中心点范围=$sFloat-$eFloat>>>$value1/$value2/$value3---")


//        randomNumberGenerator();
//        float value1=list.get(listRandom.get(0));
//        float value2=list.get(listRandom.get(1));
//        float value3=list.get(listRandom.get(2));
//        SelfLogUtils.log("Animation2","中心点范围="+sFloat+"-"+eFloat+">>>"+listRandom.get(0)+"/"+listRandom.get(1)+"/"+listRandom.get(2)+"---");
        log("Animation3", "scheduledExecutorService=$scheduledExecutorService")
        log("Animation3", "scheduledFuture=$scheduledFuture")
        mPath.reset()
        mPath.addCircle(mBallRadius, value1, mBallRadius, Path.Direction.CW)
        mPath.addCircle(3 * mBallRadius + mBallHgap, value2, mBallRadius, Path.Direction.CW)
        mPath.addCircle(5 * mBallRadius + 2 * mBallHgap, value3, mBallRadius, Path.Direction.CW)

        //canvas.save();
        canvas.drawPath(mPath, mPaint)
        //canvas.restore();
    }

    override fun start() {
        if (isOld) {
            if (!mValueAnimator!!.isRunning) {
                mValueAnimator!!.addUpdateListener(this)
                mValueAnimator!!.start()
            }
        } else {
            log("Animation5", "打开动画1111111111111111111111111111111111111111111")

            if (scheduledExecutorService == null) {
                scheduledExecutorService = Executors.newScheduledThreadPool(1)
            }
            scheduledFuture = scheduledExecutorService?.scheduleWithFixedDelay({
                log("Animation4", "执行55555555555555555555555555555555555555555555555555555555555")

                handler.post { invalidateSelf() }
            }, 0, 200, TimeUnit.MILLISECONDS)
        }
    }

    override fun stop() {
        if (isOld) {
            if (mValueAnimator?.isRunning==true) {
                val animator: Animator? = mValueAnimator
                animator?.removeAllListeners()
                mValueAnimator?.removeAllUpdateListeners()
                mValueAnimator?.cancel()
            }
        } else {
            log("Animation5", "关闭动画2222222222222222222222222222222222222")

            try {
                if (scheduledFuture != null && scheduledFuture?.isCancelled==false) {
                    log("Animation4", "执行001")
                    scheduledFuture?.cancel(true)
                    log("Animation4", "执行002")
                }
                if (scheduledExecutorService != null && scheduledExecutorService?.isShutdown==false) {
                    log("Animation4", "执行201")
                    scheduledExecutorService?.shutdownNow()
                    log("Animation4", "执行202")
                }
                log("Animation4", "执行301")
            } catch (e: Exception) {
                e.printStackTrace()
                log("Animation4", "错误:" + e.message)
            } finally {
                scheduledFuture = null
                scheduledExecutorService = null
            }
        }
    }

    override fun isRunning(): Boolean {
        return if (isOld) {
            mValueAnimator?.isRunning==true
        } else {
            scheduledFuture != null && scheduledFuture?.isCancelled==false
        }
    }

}