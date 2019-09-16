package com.durian.richeditor.editor

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import java.util.*

/**
 * @author Durian
 * @since 2019/9/10
 */
class RichEditor @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    WebView(context, attrs, defStyleAttr) {
    private var imageId = 0

    var onAddCover: (() -> Unit)? = null
    var onOutputHtml: ((String) -> Unit)? = null
    var onOutputPartHtml: ((String) -> Unit)? = null

    private val SETUP_HTML = "file:///android_asset/editor.html"


    init {
        loadUrl(SETUP_HTML)
        // 设置加载进来的页面自适应手机屏幕
        isVerticalScrollBarEnabled = false
        isHorizontalScrollBarEnabled = false
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.javaScriptEnabled = true
        addJavascriptInterface(EditorJs(), "EditorJs")
        requestFocus()
    }

    fun addCover(url: String) {
        load("javascript:EDITOR.insertCover('$url');")
    }

    fun insertImage(url: String, width: Long, height: Long) {
        load("javascript:EDITOR.saveRange();")
        load("javascript:EDITOR.insertImage('$url',$width,$height, 'image-$imageId');")
        imageId++
    }

    fun setBold() {
        load("javascript:EDITOR.saveRange();")
        load("javascript:EDITOR.exec('bold');")
    }

    fun setItalic() {
        load("javascript:EDITOR.saveRange();")
        load("javascript:EDITOR.exec('italic');")
    }

    fun setStrikethrough() {
        load("javascript:EDITOR.saveRange();")
        load("javascript:EDITOR.exec('strikethrough');")
    }

    fun setUnderline() {
        load("javascript:EDITOR.saveRange();")
        load("javascript:EDITOR.exec('underline');")
    }

    fun setUndo() {
        load("javascript:EDITOR.saveRange();")
        load("javascript:EDITOR.exec('undo');")
    }

    fun setRedo() {
        load("javascript:EDITOR.saveRange();")
        load("javascript:EDITOR.exec('redo');")
    }

    fun setSuperscript() {
        load("javascript:EDITOR.saveRange();")
        load("javascript:EDITOR.exec('superscript');")
    }

    fun setSubscript() {
        load("javascript:EDITOR.saveRange();")
        load("javascript:EDITOR.exec('subscript');")
    }

    fun setH(level: Int, normal: Boolean) {
        load("javascript:EDITOR.saveRange();")
        Log.e("HZMDurian", "isselect = $normal")
        if (!normal) {
            load("javascript:EDITOR.exec('h$level')")
        } else {
            load("javascript:EDITOR.exec('p')")
        }

    }

    /**
     * 不支持透明度变化
     */
    fun setTextColor(color: Int) {
        load("javascript:EDITOR.saveRange();")
        load("javascript:EDITOR.setTextColor('${colorHex(color)}');")
    }

    fun insertHr() {
        load("javascript:EDITOR.saveRange();")
        load("javascript:EDITOR.insertLine();")
    }

    fun setBlockquote(block: Boolean) {
        load("javascript:EDITOR.saveRange();")
        if (block) {
            load("javascript:EDITOR.exec('blockquote')")
        } else {
            load("javascript:EDITOR.exec('p')")
        }
    }

    fun justifyLeft() {
        load("javascript:EDITOR.setJustifyLeft()")
    }

    fun justifyRight() {
        load("javascript:EDITOR.setJustifyRight()")
    }

    fun justifyCenter() {
        load("javascript:EDITOR.setJustifyCenter()")
    }

    fun setUnorderedList() {
        load("javascript:EDITOR.setUnorderedList()")
    }

    fun setOrderedList() {
        load("javascript:EDITOR.setOrderedList()")
    }

    fun insertLink(url: String, name: String) {
        load("javascript:EDITOR.saveRange();")
        load("javascript:EDITOR.insertLink('$title', '$name');")
    }

    fun getHtml(all: Boolean) {
        if (all) {
            loadUrl("javascript:window.EditorJs.getHtml('<head>'+" + "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
        }else {
            loadUrl("javascript:window.EditorJs.getPartHtml(" + "document.getElementById('editor').innerHTML+'');");
        }
    }

    private fun colorHex(color: Int): String {
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)
        return String.format(Locale.getDefault(), "#%02X%02X%02X", r, g, b)
    }


    private fun load(js: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            evaluateJavascript(js, null)
        } else {
            loadUrl(js)
        }
    }


    inner class EditorJs {

        @JavascriptInterface
        fun addCover() {
            onAddCover?.invoke()
        }

        @JavascriptInterface
        fun getHtml(html: String) {
            onOutputHtml?.invoke(html)
        }

        @JavascriptInterface
        fun getPartHtml(html: String) {
            onOutputPartHtml?.invoke(html)
        }
    }

}