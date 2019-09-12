package com.durian.richeditor.menu

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView

/**
 * @author Durian
 * @since 2019/9/11 16:24
 */
class MenuImageView(context: Context, attributes: AttributeSet?, defStyleAttr: Int) :
    AppCompatImageView(context, attributes, defStyleAttr) {

    constructor(context: Context) : this(context, null, 0)

    var type: MenuType? = null


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_UP) {
            isSelect = !isSelect
        }
        return super.onTouchEvent(event)
    }

    var isSelect = false
        set(value) {
            field = value
            //部分按钮样式不需要显示选中效果
            if (type == MenuType.TEXTCOLOR ||
                type == MenuType.ALIGN_LEFT ||
                type == MenuType.ALIGN_CENTER ||
                type == MenuType.ALIGN_RIGHT ||
                type == MenuType.UL ||
                type == MenuType.OL ||
                type == MenuType.LINK ||
                type == MenuType.CODE ||
                type == MenuType.HR
            ) return
            if (value) {
                setColorFilter(Color.BLUE)
            } else {
                setColorFilter(Color.BLACK)
            }
        }

    fun setTextColor(color: Int) {
        setColorFilter(color)
    }
}