package com.wepon.pentagonview

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // 换一种自定义的非主流显示风格
        btSetDiffShow.setOnClickListener {
            pentagonView.mRingPliesCount = 5

            pentagonView.mTextOffsetRing = 20
            pentagonView.mTextSize = 50f
            pentagonView.mTextColor = Color.BLACK
            pentagonView.mAngleText = arrayOf("打野", "治疗量", "伤害量", "参团率", "承受")

            pentagonView.mScoreLineStrokeWidth = 5f
            pentagonView.mScoreLineColor = Color.parseColor("#53A616")
            pentagonView.mScoreFillColor = Color.parseColor("#8967FF5A")

            pentagonView.mRingLineColor = Color.BLACK
            pentagonView.mRingFillColors = intArrayOf(Color.YELLOW, Color.RED)

            pentagonView.mMaxScore = 1000
            pentagonView.mAnimatorTime = 1000
        }

        // 设置分数
        btSetScore.setOnClickListener {
            val random = Random()
            pentagonView.mScoreData = intArrayOf(
                    random.nextInt(pentagonView.mMaxScore),
                    random.nextInt(pentagonView.mMaxScore),
                    random.nextInt(pentagonView.mMaxScore),
                    random.nextInt(pentagonView.mMaxScore),
                    random.nextInt(pentagonView.mMaxScore))
        }

    }
}
