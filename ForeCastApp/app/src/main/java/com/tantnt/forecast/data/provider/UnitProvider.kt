package com.tantnt.forecast.data.provider

import com.tantnt.forecast.internal.UnitSystem

interface UnitProvider{
    fun getUnitSystem() : UnitSystem
}