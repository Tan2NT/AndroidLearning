package com.tantnt.android.runstatistic.models

object UserLevel {
    val mLevelDefined = listOf<Long>(0, 10000, 30000, 60000, 80000, 100000, 130000)

    fun getUserLevel(stepUntilNow: Long): Int{
        for (i in mLevelDefined.size - 1 downTo 0)  {
            if(stepUntilNow >= mLevelDefined.get(i))
                return i
        }
        return 0
    }

    fun getStepToNextLevel(stepUntilNow: Long) : Long {
        val level = getUserLevel(stepUntilNow)
        if(level < mLevelDefined.size - 2)
            return mLevelDefined[level + 1] - stepUntilNow
        return 0L
    }

    fun getMaxStep() : Long {
        return mLevelDefined.get(mLevelDefined.size - 1)
    }

}