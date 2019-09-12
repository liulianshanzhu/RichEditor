package com.durian.richeditor.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.durian.richeditor.R

/**
 * @author Durian
 * @since 2019/9/12 11:51
 */
class LinkDialog : DialogFragment() {
    private var dialog: View? = null
    var onComfirmClick: ((String, String) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog = inflater.inflate(R.layout.dialog_fragment_link, container, false)
        val comfirm = dialog?.findViewById<TextView>(R.id.confirm_btn)
        val cancle = dialog?.findViewById<TextView>(R.id.cancel_btn)
        val url = dialog?.findViewById<EditText>(R.id.link_url_et)
        val name = dialog?.findViewById<EditText>(R.id.link_name_et)

        comfirm?.setOnClickListener {
            onComfirmClick?.invoke(url?.text.toString(), name?.text.toString())
            dismiss()
        }

        cancle?.setOnClickListener {
            dismiss()
        }
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

}