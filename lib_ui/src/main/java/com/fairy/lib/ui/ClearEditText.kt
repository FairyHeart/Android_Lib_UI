package com.fairy.lib.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData

/**
 * 输入文本框 右边有自带的删除按钮 当有输入时，显示删除按钮，当无输入时，不显示删除按钮。
 *
 */
class ClearEditText(context: Context, attrs: AttributeSet?, defStyle: Int) :
    AppCompatEditText(context, attrs, defStyle), OnFocusChangeListener,
    TextWatcher {

    constructor(context: Context) : this(context, null) {}
    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        R.attr.editTextStyle
    ) // 这里构造方法也很重要，不加这个很多属性不能再XML里面定义

    /**
     * 删除按钮的引用
     */
    private var mClearDrawable: Drawable? = null

    /**
     * 控件是否有焦点
     */
    private var hasFoucs = false

    /**
     * 内容变化监听器
     */
    var onTextChanged: OnTextChanged? = null

    val onTextChangeLiveData by lazy {
        MutableLiveData<String?>()
    }

    init {
        init()
    }

    private fun init() { // 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
        mClearDrawable = compoundDrawables[2]
        if (mClearDrawable == null) {
            mClearDrawable = ContextCompat.getDrawable(context, R.mipmap.ic_cancel)
        }
        mClearDrawable?.setBounds(
            0,
            0,
            mClearDrawable?.intrinsicWidth ?: 0,
            mClearDrawable?.intrinsicHeight ?: 0
        )
        // 默认设置隐藏图标
        setClearIconVisible(false)
        // 设置焦点改变的监听
        onFocusChangeListener = this
        // 设置输入框里面内容发生改变的监听
        addTextChangedListener(this)
    }

    /**
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件 当我们按下的位置 在 EditText的宽度 -
     * 图标到控件右边的间距 - 图标的宽度 和 EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            if (compoundDrawables[2] != null) {
                val touchable =
                    event.x > width - totalPaddingRight && event.x < width - paddingRight
                if (touchable) {
                    this.setText("")
                }
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
     */
    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        hasFoucs = hasFocus
        if (hasFocus) {
            text?.isNotEmpty()?.let { setClearIconVisible(it) }
        } else {
            setClearIconVisible(false)
        }
    }

    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     *
     * @param visible
     */
    fun setClearIconVisible(visible: Boolean) {
        val right = if (visible) mClearDrawable else null
        setCompoundDrawables(
            compoundDrawables[0],
            compoundDrawables[1],
            right,
            compoundDrawables[3]
        )
    }

    /**
     * 当输入框里面内容发生变化的时候回调的方法
     */
    override fun onTextChanged(
        s: CharSequence,
        start: Int,
        count: Int,
        after: Int
    ) {
        if (hasFoucs) {
            setClearIconVisible(s.isNotEmpty())
        }
        onTextChanged?.onTextChanged(s, start, count, after)
        onTextChangeLiveData.value = s.toString()
    }

    override fun beforeTextChanged(
        s: CharSequence,
        start: Int,
        count: Int,
        after: Int
    ) {
    }

    override fun afterTextChanged(s: Editable) {}

    interface OnTextChanged {
        fun onTextChanged(
            s: CharSequence,
            start: Int,
            count: Int,
            after: Int
        )
    }
}