package com.fairy.lib.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 *
 *
 * @author: Fairy.
 * @date  : 2020/8/26.
 */
class DividerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
) : View(context, attrs, defStyleAttr) {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        0
    )

    var mPaint: Paint
    private var orientation = 0

    init {
        var dashGap = 5
        var dashLength = 5
        var dashThickness = 3
        var color: Int = -0x1000000
        if (context != null) {
            val array = context.obtainStyledAttributes(attrs, R.styleable.DividerView)
            try {
                dashGap = array.getDimensionPixelSize(R.styleable.DividerView_dash_gap, 5)
                dashLength = array.getDimensionPixelSize(R.styleable.DividerView_dash_length, 5)
                dashThickness =
                    array.getDimensionPixelSize(R.styleable.DividerView_dash_thickness, 3)
                color = array.getColor(R.styleable.DividerView_divider_line_color, -0x1000000)
                orientation =
                    array.getInt(R.styleable.DividerView_divider_orientation, 0)
            } finally {
                array.recycle()
            }
        }

        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.color = color
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = dashThickness.toFloat()
        mPaint.pathEffect = DashPathEffect(
            floatArrayOf(dashGap.toFloat(), dashLength.toFloat()),
            0f
        )
    }

    override fun onDraw(canvas: Canvas) {
        if (orientation == 0) {
            val center = height * 0.5f
            canvas.drawLine(0f, center, width.toFloat(), center, mPaint)
        } else {
            val center = width * 0.5f
            canvas.drawLine(center, 0f, center, height.toFloat(), mPaint)
        }
    }
}