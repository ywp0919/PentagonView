package com.wepon.pentagonviewlib

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator


/**
 * desc: 自定义五边形View
 * 多边形的生成可以通过path路径和canvas旋转实现
 * 这里5边形先使用path路径实现
 * 设置的默认值目前都是针对自己项目里面的UI需要来设置的
 * 暂时没去适配设置padding了，调位置的话先用margin吧
 * kotlin需要注解暴露多个重载方法
 *
 * 会上传到github使用。
 *
 * @author Wepon.Yan
 * created at 2018/5/31 下午15:39
 */
class PentagonView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    /**
     * 多边形的边数，这里默认是5，以后想扩展再弄
     */
    private val mSideCount = 5

    /**
     * 多边形设置的环数，这里默认4环
     */
    var mRingPliesCount = 4
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 多边形对应的角的文字，从正上方开始顺时针方向计算，这里默认是5个
     * 提供外界设置值
     */
    var mAngleText = arrayOf("专心", "效率", "讨论", "笔记", "考核")
        set(value) {
            if (value.size == mSideCount) {
                field = value
                invalidate()
            }
        }

    /**
     * 设置默认角分数的最大值为1000分
     * 现在这个值提供给外界设置吧
     */
    var mMaxScore = 1000
        set(value) {
            if (value > 0) {
                field = value
                invalidate()
            }
        }

    /**
     * 这个用来重置用的
     */
    private var mAnimator: ValueAnimator? = null

    /**
     * 每个角对应的分数值，这是随便给的测试效果用
     * 这里会开放给外界设置值，外界根据最大值为100按比例计算好数值再传进来
     *
     */
    var mScoreData = intArrayOf(0, 0, 0, 0, 0)
        set(data) {
            if (data.size != mSideCount || data.any { it > mMaxScore }) {
                return
            }
            if (mAnimator?.isRunning == true) {
                mAnimator?.cancel()
                mAnimator = null
            }
            val mOldScoreData = field.clone()
            mAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
                duration = mAnimatorTime
                interpolator = AccelerateDecelerateInterpolator()
                addUpdateListener {

                    if (this@PentagonView.isAttachedToWindow) {
                        val percent = it.animatedValue as Float
                        data.forEachIndexed { index, dataScore ->
                            field[index] = (mOldScoreData[index] + (dataScore - mOldScoreData[index]) * percent).toInt()
                        }
                        invalidate()
                    }
                }
            }
            mAnimator?.start()
        }

    /**
     * view 中心点坐标
     */
    private var mCenterX: Int = 0
    private var mCenterY: Int = 0

    /**
     * 不包括文字的时候的圆形的半径
     */
    private var mRadius = 0

    /**
     * 保存5个点的坐标
     */
    private val mPointX by lazy { IntArray(mSideCount) }
    private val mPointY by lazy { IntArray(mSideCount) }

    /**
     * 每一个边块对应的弧度
     * 角度数 * Math.PI/180即为当前角度数的弧度值
     * (Math.PI/180)*(360/5)
     */
    private val mRadian
        get() = Math.PI * 2 / mSideCount

    /**
     * 环填充的颜色
     * 目前就两种交替 从最外层开始
     * 开放给外界设置值
     */
    var mRingFillColors = intArrayOf(Color.WHITE, Color.parseColor("#AAF5F5F5"))
        set(value) {
            if (value.size == 2) {
                field = value
                invalidate()
            }
        }


    /**
     * 多边形的环线条的颜色值
     */
    var mRingLineColor = Color.GRAY
        set(value) {
            field = value
            mRingLinePaint.color = value
            invalidate()
        }


    /**
     * 分数线条的颜色值
     */
    var mScoreLineColor = Color.parseColor("#53A616")
        set(value) {
            field = value
            mScoreLinePaint.color = value
            invalidate()
        }

    /**
     * 分数填充的颜色值
     */
    var mScoreFillColor = Color.parseColor("#3367FF5A")
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 文字颜色
     */
    var mTextColor = Color.BLACK
        set(value) {
            field = value
            mTextPaint.color = value
            invalidate()
        }

    /**
     * 文字大小 也要转成px再传进来
     */
    var mTextSize = 40f
        set(value) {
            field = value
            mTextPaint.textSize = value
            invalidate()
        }

    /**
     * 文字与图形的间距 px 设置的时候转化成px再传进来
     */
    var mTextOffsetRing = 20
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 设置数据的动画时长
     */
    var mAnimatorTime = 500L

    /**
     * 画环形的画笔
     */
    private var mRingLinePaint: Paint = Paint().apply {
        isAntiAlias = true
        strokeWidth = 2f
        color = mRingLineColor
        style = Paint.Style.FILL_AND_STROKE
    }

    /**
     * 画分数多边形边界的线条宽度 px
     */
    var mScoreLineStrokeWidth = 4f
        set(value) {
            field = value
            mScoreLinePaint.strokeWidth = value
            invalidate()
        }

    /**
     * 画分数多边形的画笔
     */
    private var mScoreLinePaint: Paint = Paint().apply {
        isAntiAlias = true
        strokeWidth = mScoreLineStrokeWidth
        color = mScoreLineColor
        style = Paint.Style.FILL_AND_STROKE
    }


    /**
     * 画文字的画笔
     */
    private var mTextPaint: Paint = Paint().apply {
        isAntiAlias = true
        textSize = mTextSize
        color = mTextColor
        style = Paint.Style.FILL_AND_STROKE
    }

    /**
     * 画的路径，通用
     */
    private var mPath: Path = Path()

    /**
     * 设置一个默认的最小宽高的值。
     */
    private val mDefaultWidth = 600

    /**
     * 计算不同模式下控件大小
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(measure(widthMeasureSpec), measure(heightMeasureSpec))
    }

    /**
     * 计算大小
     */
    private fun measure(origin: Int): Int {
        val specMode = MeasureSpec.getMode(origin)
        val specSize = MeasureSpec.getSize(origin)
        return when (specMode) {
        // 精确模式
            MeasureSpec.EXACTLY -> specSize
        // 最大值模式
            MeasureSpec.AT_MOST -> Math.min(mDefaultWidth, specSize)
        // 不指定其大小测量模式
            MeasureSpec.UNSPECIFIED -> mDefaultWidth
            else -> mDefaultWidth
        }

    }

    /**
     * 画画从这里开始
     */
    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        // 计算出一些位置的值
        // 中心点
        mCenterX = width / 2
        mCenterY = height / 2

        mRadius = if (width > height) {
            height
        } else {
            width
        } / 2
        // 先把文字去掉之后 计算这个View的宽高，取最小边做正方形,也就是圆的半径
        // 文字 按宽度比高度值要大的情况来计算，取最长的一个，最好是不要设置的差别太大了。
        var maxStr = mAngleText[0]
        mAngleText.forEach {
            if (it.length > maxStr.length) {
                maxStr = it
            }
        }
        mRadius = mRadius - getTextBoundWidth(maxStr) - mTextOffsetRing

        // 画多边形的环出来
        drawPolygonRing(canvas)
        // 画出最外环需要画出从圆心到点的线出来
        drawCenterToAngleLine(canvas)
        // 画出文字吧
        drawAngleText(canvas)
        // 然后要画出分数的多边形了。
        drawScorePolygon(canvas)
    }

    /**
     * 提供计算文字宽度
     */
    private fun getTextBoundWidth(str: String): Int {
        val rect = Rect()
        mTextPaint.getTextBounds(str, 0, str.length, rect)
        return rect.width()
    }

    /**
     * 把文字画出来吧
     * 这个时间的点的位置都是最外层的环的点
     */
    private fun drawAngleText(canvas: Canvas) {
        mAngleText.forEachIndexed { index, string ->
            // 位置还要调整一下
            when (index) {
                0 -> canvas.drawText(string, mPointX[index].toFloat() - getTextBoundWidth(mAngleText[index]) / 2, mPointY[index].toFloat() - mTextOffsetRing, mTextPaint)
                1 -> canvas.drawText(string, mPointX[index].toFloat() + mTextOffsetRing, mPointY[index].toFloat(), mTextPaint)
                2 -> canvas.drawText(string, mPointX[index].toFloat() + mTextOffsetRing, mPointY[index].toFloat() + mTextOffsetRing, mTextPaint)
                3 -> canvas.drawText(string, mPointX[index].toFloat() - getTextBoundWidth(mAngleText[index]) - mTextOffsetRing, mPointY[index].toFloat() + mTextOffsetRing, mTextPaint)
                4 -> canvas.drawText(string, mPointX[index].toFloat() - getTextBoundWidth(mAngleText[index]) - mTextOffsetRing, mPointY[index].toFloat(), mTextPaint)
            }

        }
    }

    /**
     * 这里负责画出mRingCount层的环出来
     */
    private fun drawPolygonRing(canvas: Canvas) {
        // 画几个五边形环
        for (i in 0 until mRingPliesCount) {
            // 从最外层开始画
            calculateRingLocation((mRingPliesCount - i) * 1.0f / mRingPliesCount)
            // 点计算完了，开始画线
            drawPathByLocation(canvas, i)
        }
    }

    /**
     * 画出分数能力的多边形
     */
    private fun drawScorePolygon(canvas: Canvas, isFill: Boolean = true) {
        // 先计算相应分数对应的位置
        mScoreData.forEachIndexed { index, score ->
            calculateLocationByIndex(index, score * 1.0f / mMaxScore)
        }
        // 设置path
        movePath()
        mScoreLinePaint.apply {
            if (isFill) {
                color = mScoreFillColor
                style = Paint.Style.FILL
            } else {
                color = mScoreLineColor
                style = Paint.Style.STROKE
            }
        }
        // 画出来
        canvas.drawPath(mPath, mScoreLinePaint)
        if (isFill) {
            drawScorePolygon(canvas, false)
        }
    }

    /**
     * 抽取计算位置数据的这块出来放方法体内
     * percent就是原点到点的值相对于半径mRadius的比例
     * 也可以是当前分数相对于可设置最大分数值的比例
     * 计算5个点的坐标
     */
    private fun calculateLocationByIndex(index: Int, percent: Float) {
        when (index) {
            0 -> {
                mPointX[index] = mCenterX
                mPointY[index] = mCenterY - (mRadius * percent).toInt()
            }
            1 -> {
                mPointX[index] = mCenterX + (mRadius * Math.sin(mRadian) * percent).toInt()
                mPointY[index] = mCenterY - (mRadius * Math.cos(mRadian) * percent).toInt()
            }
            2 -> {
                mPointX[index] = mCenterX + (mRadius * Math.sin(mRadian / 2) * percent).toInt()
                mPointY[index] = mCenterY + (mRadius * Math.cos(mRadian / 2) * percent).toInt()
            }
            3 -> {
                mPointX[index] = mCenterX - (mRadius * Math.sin(mRadian / 2) * percent).toInt()
                mPointY[index] = mCenterY + (mRadius * Math.cos(mRadian / 2) * percent).toInt()
            }
            4 -> {
                mPointX[index] = mCenterX - (mRadius * Math.sin(mRadian) * percent).toInt()
                mPointY[index] = mCenterY - (mRadius * Math.cos(mRadian) * percent).toInt()
            }
        }
    }

    /**
     * 画出最外环需要画出从圆心到点的线出来
     */
    private fun drawCenterToAngleLine(canvas: Canvas) {
        // 拿到最外层的点的位置
        calculateRingLocation(1f)
        // 一个一个画出路径
        mPointX.forEachIndexed { index, x ->
            mPath.reset()
            mPath.moveTo(mCenterX.toFloat(), mCenterY.toFloat())
            mPath.lineTo(x.toFloat(), mPointY[index].toFloat())

            mRingLinePaint.style = Paint.Style.STROKE
            mRingLinePaint.color = mRingLineColor
            canvas.drawPath(mPath, mRingLinePaint)
        }
    }

    /**
     * 通过计算拿到中心点坐标和5个点的坐标
     * 现在只是针对5边形的情况，如果是多边形就不能这么计算了
     * radius 代表圆的半径
     */
    private fun calculateRingLocation(percent: Float) {
        for (i in 0 until mSideCount) {
            calculateLocationByIndex(i, percent)
        }
    }

    /**
     * 根据 mPointX 和 mPointX 画出路径
     * 这里递归是为了画出线条来。
     */
    private fun drawPathByLocation(canvas: Canvas, ringIndex: Int, isFill: Boolean = true) {
        movePath()
        if (!isFill) {
            mRingLinePaint.color = mRingLineColor
            mRingLinePaint.style = Paint.Style.STROKE
        } else {
            // 画出来
            mRingLinePaint.color = mRingFillColors[ringIndex % 2]
            mRingLinePaint.style = Paint.Style.FILL
        }

        canvas.drawPath(mPath, mRingLinePaint)
        if (isFill) {
            drawPathByLocation(canvas, ringIndex, false)
        }
    }

    /**
     * 先跑一次路径，跑完就画出来
     */
    private fun movePath() {
        mPath.reset()
        // 先设置路径
        mPointX.forEachIndexed { index, x ->
            if (index == 0) {
                // 移动到第一个点
                mPath.moveTo(x.toFloat(), mPointY[index].toFloat())
            } else {
                // 画到下一个点
                mPath.lineTo(x.toFloat(), mPointY[index].toFloat())
            }
        }
        mPath.close()
    }

}
