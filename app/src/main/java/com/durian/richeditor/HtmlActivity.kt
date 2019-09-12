package com.durian.richeditor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_html.*

class HtmlActivity : AppCompatActivity() {
    private var isAll = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_html)

        val code = intent.getStringExtra("html")
        html.text = code

        change.setOnClickListener {
            isAll = !isAll
            change.text = if (isAll) "显示正文" else "显示所有"
            html.text = if (isAll) code else code.substring(
                code.indexOf("<div id=\"editor\""), code.indexOf(
                    "</div>\n" +
                            "    <script src=\""
                )
            )
        }
    }
}
