package com.cofbro.xun.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.text.InputType
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.inputmethod.BaseInputConnection
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputMethodManager
import com.cofbro.xun.R
import com.cofbro.xun.view.InputView.Config.CURSOR_PADDING
import com.cofbro.xun.view.InputView.Config.DEFAULT_HEIGHT
import com.cofbro.xun.view.InputView.Config.DEFAULT_PADDING
import com.cofbro.xun.view.InputView.Config.DEFAULT_WIDTH
import kotlin.math.abs

class InputView : View {
    object Config {
        const val TEXT_INDENTED = 30f
        const val HINT_BACKGROUND_PADDING = 10f
        const val CURSOR_HEIGHT = 45f
        const val CURSOR_PADDING = 5f
        const val DEFAULT_WIDTH = 800f
        const val DEFAULT_HEIGHT = 130f
        const val DEFAULT_PADDING = 50
    }

    private lateinit var typedArray: TypedArray
    private var isPasswordType = false
    private var inputString: String = ""
    private var hideInputString = ""
    private var cursorAnimator: Animator? = null
    private var cursorAlpha = 255
    private var textOffsetY = 0f
    private var textOffsetX = 0f
    private var left = 0f
    private var top = 0f
    private var right = 0f
    private var bottom = 0f
    private var mWidth = DEFAULT_WIDTH
    private var mHeight = DEFAULT_HEIGHT
    private var hintText = ""
    private val borderPaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.parseColor("#dfeeff")
        strokeWidth = 5f
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
        isDither = true
    }
    private val hintPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        textSize = 35f
        color = Color.parseColor("#cccccc")
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
        isDither = true
    }
    private val hintBackgroundPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#fafafa")
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
        isDither = true
    }

    private val cursorPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = 5f
        color = Color.argb(0, 87, 209, 118)
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
        isDither = true
    }

    private val textPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        textSize = 40f
        color = Color.BLACK
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
        isDither = true
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)

    }

    private fun init(attrs: AttributeSet?) {
        isFocusable = true
        isFocusableInTouchMode = true
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.InputView)
        hintText = typedArray.getString(R.styleable.InputView_hint).toString()
        if (typedArray.getString(R.styleable.InputView_type).toString() == "password") isPasswordType = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)

        mWidth = width.toFloat() - DEFAULT_PADDING
        mHeight = height.toFloat() - DEFAULT_PADDING
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas.let {
            drawBorder(it!!)
            drawTextBackground(it)
            drawHint(it, hintText)
            drawCursor(it)
            drawInputtedText(it)
        }
    }

    private fun drawBorder(canvas: Canvas) {
        canvas.drawRoundRect(left, top, right, bottom, 30f, 30f, borderPaint)
    }

    private fun drawTextBackground(canvas: Canvas) {
        val rect = Rect()
        hintPaint.getTextBounds(hintText, 0, hintText.length, rect)
        val l = left + Config.TEXT_INDENTED - Config.HINT_BACKGROUND_PADDING + textOffsetX
        val t = top + (bottom - top) / 2f - rect.height() / 2f - textOffsetY
        val r = l + rect.width() + Config.HINT_BACKGROUND_PADDING * 2
        val b = t + rect.height()
        canvas.drawRect(l, t, r, b, hintBackgroundPaint)
    }

    private fun drawHint(canvas: Canvas, str: String) {
        canvas.drawText(
            str,
            0,
            str.length,
            left + Config.TEXT_INDENTED + textOffsetX,
            getTextBaseline(hintPaint) - textOffsetY,
            hintPaint
        )
    }

    private fun drawCursor(canvas: Canvas) {
        var l = left + CURSOR_PADDING
        val t = top + (bottom - top - Config.CURSOR_HEIGHT) / 2f
        val b = t + Config.CURSOR_HEIGHT
        if (hideInputString.isNotEmpty()) {
            val rect = Rect()
            textPaint.getTextBounds(hideInputString, 0, hideInputString.length, rect)
            l += rect.width()
        }

        canvas.drawLine(
            l + Config.TEXT_INDENTED,
            t,
            l + Config.TEXT_INDENTED + 2f,
            b,
            cursorPaint
        )
    }

    private fun drawInputtedText(canvas: Canvas) {
        hideInputString = inputString
        if (isPasswordType) {
            hideInputString = ""
            inputString.forEach {
                hideInputString += "*"
            }
        }
        canvas.drawText(
            hideInputString,
            0,
            hideInputString.length,
            left + Config.TEXT_INDENTED,
            getTextBaseline(textPaint),
            textPaint
        )
    }

    private fun hintOffsetYAnimation(
        offsetYStart: Float,
        offsetYEnd: Float,
        offsetXStart: Float,
        offsetXEnd: Float
    ) {
        if (getTextString().isNotEmpty()) return
        ValueAnimator.ofFloat(offsetYStart, offsetYEnd).apply {
            duration = 200
            repeatCount = 0
            interpolator = LinearInterpolator()
            addUpdateListener {
                textOffsetY = it.animatedValue as Float
                invalidate()
            }
        }.start()
        ValueAnimator.ofFloat(offsetXStart, offsetXEnd).apply {
            duration = 200
            interpolator = LinearInterpolator()
            repeatCount = 0
            addUpdateListener {
                textOffsetX = it.animatedValue as Float
                invalidate()
            }
        }.start()
    }

    private fun startCursorAnimator() {
        if (cursorAnimator == null) {
            cursorAnimator = ValueAnimator.ofInt(255, 0).apply {
                duration = 1000
                interpolator = LinearInterpolator()
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.RESTART
                addUpdateListener {
                    cursorAlpha = it.animatedValue as Int
                    cursorPaint.color = Color.argb(cursorAlpha, 87, 209, 118)
                    invalidate()
                }
            }
        }
        cursorAnimator!!.start()
    }

    private fun stopCursorAnimator() {
        if (cursorAnimator != null) {
            cursorAnimator!!.cancel()
            cursorAnimator = null
            cursorPaint.color = Color.argb(0, 87, 209, 118)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        left = width / 2f - mWidth / 2f
        top = height / 2f - mHeight / 2f
        right = left + mWidth
        bottom = top + mHeight
    }

    private fun showKeyboard() {
        isFocusable = true
        isFocusableInTouchMode = true
        requestFocus()
        val im =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                ?: return
        im.showSoftInput(this, InputMethodManager.SHOW_FORCED)

    }


    private fun hideInputMethod() {
        val imm: InputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)

    }

    private fun getTextBaseline(paint: Paint): Float {
        val fontMetrics = paint.fontMetrics
        val bottom = fontMetrics.bottom
        val top = fontMetrics.top
        return height / 2f + (bottom + abs(top)) / 2f - bottom
    }

    fun getTextString(): String {
        return inputString
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        requestFocus()
        return true
    }

    override fun onFocusChanged(gainFocus: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect)
        if (gainFocus) {
            showKeyboard()
            borderPaint.color = Color.parseColor("#3170ff")
            hintOffsetYAnimation(0f, (bottom - top) / 2f, 0f, 20f)
            startCursorAnimator()
        } else {
            hideInputMethod()
            borderPaint.color = Color.parseColor("#dfeeff")
            hintOffsetYAnimation((bottom - top) / 2f, 0f, 20f, 0f)
            stopCursorAnimator()
        }
    }

    override fun onCheckIsTextEditor(): Boolean {
        return true
    }

    // 方法，需要返回一个InputConnect对象，这个是和输入法输入内容的桥梁。
    override fun onCreateInputConnection(outAttrs: EditorInfo): InputConnection {
        // outAttrs就是我们需要设置的输入法的各种类型最重要的就是:
        outAttrs.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
        outAttrs.inputType = InputType.TYPE_NULL
        return MyInputConnection(this, true)
    }

    inner class MyInputConnection(targetView: View, fullEditor: Boolean) :
        BaseInputConnection(targetView, fullEditor) {
        override fun commitText(text: CharSequence?, newCursorPosition: Int): Boolean {
            val temp = inputString + text.toString()
            if (temp.length < 23) {
                inputString = temp
            }
            invalidate()
            return true
        }

        override fun sendKeyEvent(event: KeyEvent?): Boolean {
            /** 当手指离开的按键的时候 */
            if (event != null) {
                Log.d("tag", "sendKeyEvent:KeyCode=" + event.keyCode)
            }
            if (event?.action == KeyEvent.ACTION_DOWN) {
                if (event.keyCode == KeyEvent.KEYCODE_DEL) {
                    //删除按键
                    if (inputString.isNotEmpty()) {
                        inputString = inputString.substring(0, inputString.length - 1)
                    }
                }
            }
            postInvalidate()
            return true
        }

        override fun deleteSurroundingText(beforeLength: Int, afterLength: Int): Boolean {
            Log.d(
                "tag",
                "deleteSurroundingText beforeLength=$beforeLength afterLength=$afterLength"
            )
            return true
        }

        override fun finishComposingText(): Boolean {
            // 结束组合文本输入的时候，这个方法基本上会出现在切换输入法类型，点击回车（完成、搜索、发送、下一步）点击输入法右上角隐藏按钮会触发。
            Log.d("tag", "finishComposingText")
            return true
        }
    }


}
