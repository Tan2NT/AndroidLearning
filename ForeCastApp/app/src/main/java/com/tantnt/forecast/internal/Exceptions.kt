package com.tantnt.forecast.internal

import java.io.IOException

class NoConnectivityException : IOException()
class LocationPermissionNotGranted : Exception()
class DateNotFoundException: Exception()