package com.example.android.minipaint

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.res.ResourcesCompat

private const val STROKE_WIDTH = 12f // has to be float

class MyCanvasView (context: Context) : View(context) {
    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap

    private val drawColor = ResourcesCompat.getColor(resources, R.color.colorPaint, null)

    //set up the paint with which to draw
    private val pain = Paint().apply {
        color = drawColor
        // smooth out edge of what is drawn without affecting shape
        isAntiAlias = true
        // Dithering affects how colors with higher-precision than the device are down-sampled.
        isDither = true
        style = Paint.Style.STROKE // default: FILL
        strokeJoin = Paint.Join.ROUND // default: MITER
        strokeCap = Paint.Cap.ROUND // default: BUTT
        strokeWidth = STROKE_WIDTH // default: Hairline-width (really thin)
    }

    // for calculate the draw path
    private var path = Path()
    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f
    private var currentX = 0f
    private var currentY = 0f
    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop

    // Frame
    private lateinit var frame: Rect

    private val backgroundColor = ResourcesCompat.getColor(resources, R.color.colorBackground, null)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        Log.i("TDebug", "onSizeChanged")
        super.onSizeChanged(w, h, oldw, oldh)

        if(::extraBitmap.isInitialized) extraBitmap.recycle()

        extraBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)
        extraCanvas.drawColor(backgroundColor)

        // Calculate a rectangular frame around the picture.
        val inset = 40
        frame = Rect(inset, inset, width - inset, height - inset)
    }

    override fun onDraw(canvas: Canvas?) {
        Log.i("TDebug", "onDraw")
        super.onDraw(canvas)
        Log.i("TDebug", "onDraw 111")

        if(extraBitmap != null){
            Log.i("TDebug", "onDraw 222")
            canvas?.drawBitmap(extraBitmap, 0f, 0f, null)
        }

        // Draw a frame around the canvas.
        canvas?.drawRect(frame, pain)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        motionTouchEventX = event!!.x
        motionTouchEventY = event!!.y

        when(event?.action) {
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
            MotionEvent.ACTION_UP -> touchUp()
        }

        return true
    }

    private fun touchStart() {
        path.reset()
        path.moveTo(motionTouchEventX, motionTouchEventY)
        currentX = motionTouchEventX
        currentY = motionTouchEventY
    }

    private fun touchMove() {
        val dx = Math.abs(motionTouchEventX - currentX)
        val dy = Math.abs(motionTouchEventY -  currentY)
        if(dx >= touchTolerance || dy >= touchTolerance){
            path.quadTo(currentX, currentY, (motionTouchEventX + currentX)/2, (motionTouchEventY + currentY)/2)
            currentX = motionTouchEventX
            currentY = motionTouchEventY
            //draw the poath in the extra bitmap to cache it.
            extraCanvas.drawPath(path, pain)
        }
        invalidate()
    }

    private fun touchUp() {
        // Reset the path so it doesn't get draw again
    }
}