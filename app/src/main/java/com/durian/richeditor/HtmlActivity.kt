package com.durian.richeditor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.durian.richeditor.editor.RichEditor
import kotlinx.android.synthetic.main.activity_html.*

class HtmlActivity : AppCompatActivity() {
    private var isAll = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_html)

        val all = intent.getStringExtra("all")
        val part = intent.getStringExtra("part")
        html.text = all

        change.setOnClickListener {
            isAll = !isAll
            change.text = if (isAll) "显示正文" else "显示所有"
            html.text = if (isAll) all else part
        }
    }
}
