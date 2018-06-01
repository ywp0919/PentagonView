# PentagonView
项目里面做的一个五边形能力分数值的View，稍微弄了一下，有什么问题可以提下issues方便改进。
#### 这是一个kotlin的项目，没有写java版本，有需要java版本的可以自己转化一下。。。
## 效果图
![Mou icon](mdFile/pic_1.png)
![Mou icon](mdFile/pic_2.png)

![Mou icon](mdFile/pic_3.png)
![Mou icon](mdFile/pic_4.png)

## 动图来一份  如果看不到的话麻烦下载项目看算了 ^_^
![](mdFile/gif_1.gif)






## 使用方法
#### Step 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:

    allprojects {
        repositories {
                 ...
                maven { url 'https://jitpack.io' }
        }
    }

#### Step 2. Add the dependency
    dependencies {
            implementation 'com.github.ywp0919:PentagonView:v1.1.0'
    }


## 部分值 设置如下，具体还是看项目代码吧。
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

