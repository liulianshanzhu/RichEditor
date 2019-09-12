package com.durian.richeditor

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.TypedValue
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.durian.richeditor.editor.RichEditor
import com.durian.richeditor.menu.LinkDialog
import com.durian.richeditor.menu.MenuImageView
import com.durian.richeditor.menu.MenuType
import top.defaults.colorpicker.ColorPickerPopup
import java.io.*


class MainActivity : AppCompatActivity() {
    private val ADD_COVER = 0x01
    private val CROP_CODE = 0x2
    private val INSERT_IMG = 0x3;
    private lateinit var editor: RichEditor
    private lateinit var menu_container: LinearLayout

    private val menu = mutableListOf<MenuType>(
        MenuType.CODE,
        MenuType.PIC, MenuType.BOLD, MenuType.ITALIC, MenuType.STRIKETHROUGH, MenuType.UNDERLINE,
        MenuType.SUPERSCRIPT, MenuType.SUBSCRIPT,
        MenuType.H1, MenuType.H2, MenuType.H3, MenuType.H4, MenuType.H5, MenuType.H6,
        MenuType.TEXTCOLOR, MenuType.HR, MenuType.BLOCKQUOTE,
        MenuType.ALIGN_LEFT, MenuType.ALIGN_CENTER, MenuType.ALIGN_RIGHT,
        MenuType.OL, MenuType.UL, MenuType.LINK,
        MenuType.UNDO, MenuType.REDO
    )

    private val menuRes = mutableListOf<Int>(
        R.mipmap.ic_code_review,
        R.mipmap.ic_insert_photo,
        R.mipmap.ic_format_bold,
        R.mipmap.ic_format_italic,
        R.mipmap.ic_format_strikethrough,
        R.mipmap.ic_format_underlined,
        R.mipmap.ic_format_superscript,
        R.mipmap.ic_format_subscript,
        R.mipmap.ic_format_h1,
        R.mipmap.ic_format_h2,
        R.mipmap.ic_format_h3,
        R.mipmap.ic_format_h4,
        R.mipmap.ic_format_h5,
        R.mipmap.ic_format_h6,
        R.mipmap.ic_format_text_color,
        R.mipmap.ic_line,
        R.mipmap.ic_format_quote,
        R.mipmap.ic_format_align_left,
        R.mipmap.ic_format_align_center,
        R.mipmap.ic_format_align_right,
        R.mipmap.ic_format_list_numbered,
        R.mipmap.ic_format_list_bulleted,
        R.mipmap.ic_insert_link,
        R.mipmap.ic_undo,
        R.mipmap.ic_redo
    )

    private val menuView = mutableListOf<MenuImageView>()

    private val operator: ((MenuType) -> Unit) = {
        when (it) {
            MenuType.PIC -> {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, INSERT_IMG)
            }
            MenuType.BOLD -> {
                editor.setBold()
            }
            MenuType.ITALIC -> {
                editor.setItalic()
            }
            MenuType.STRIKETHROUGH -> {
                editor.setStrikethrough()
            }
            MenuType.UNDERLINE -> {
                editor.setUnderline()
            }
            MenuType.REDO -> {
                editor.setRedo()
            }
            MenuType.UNDO -> {
                editor.setUndo()
            }
            MenuType.SUPERSCRIPT -> {
                if (menuView[it.type].isSelect) {
                    //取消下标状态
                    menuView[it.type].isSelect = false
                }
                editor.setSuperscript()
            }
            MenuType.SUBSCRIPT -> {
                if (menuView[it.type - 2].isSelect) {
                    //取消上标状态
                    menuView[it.type - 2].isSelect = false
                }
                editor.setSubscript()
            }
            MenuType.H1,
            MenuType.H2,
            MenuType.H3,
            MenuType.H4,
            MenuType.H5,
            MenuType.H6 -> {
                menuView
                    .filter { (it.type?.type!! >= 8) and (it.type?.type!! <= 13) }
                    .forEach { img ->
                        if (it == img.type) {
                            editor.setH(it.type - 7, !img.isSelect)
                        } else {
                            img.isSelect = false
                        }
                    }
            }
            MenuType.TEXTCOLOR -> {
                ColorPickerPopup.Builder(this)
                    .initialColor(Color.BLACK)
                    .enableAlpha(false)
                    .okTitle("确定")
                    .cancelTitle("取消")
                    .showIndicator(true)
                    .showValue(true)
                    .onlyUpdateOnTouchEventUp(false)
                    .build()
                    .show(object : ColorPickerPopup.ColorPickerObserver() {
                        override fun onColorPicked(color: Int) {
                            menuView[it.type - 1].setTextColor(color)
                            editor.setTextColor(color)
                        }
                    })
            }
            MenuType.HR -> {
                editor.insertHr()
            }
            MenuType.BLOCKQUOTE -> {
                editor.setBlockquote(menuView[it.type].isSelect)
            }
            MenuType.ALIGN_LEFT -> {
                editor.justifyLeft()
            }
            MenuType.ALIGN_CENTER -> {
                editor.justifyCenter()
            }
            MenuType.ALIGN_RIGHT -> {
                editor.justifyRight()
            }
            MenuType.OL -> {
                editor.setOrderedList()
            }
            MenuType.UL -> {
                editor.setUnorderedList()
            }
            MenuType.LINK -> {
                LinkDialog().apply {
                    onComfirmClick = { url, name ->
                        if (url.isNotEmpty() && name.isNotEmpty()) {
                            editor.insertLink(url, name)
                        }
                    }
                }.show(supportFragmentManager, "link")
            }
            MenuType.CODE -> {
                editor.getHtml()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editor = findViewById(R.id.editor)
        menu_container = findViewById(R.id.menu_container)
        editor.onAddCover = {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, ADD_COVER)
        }
        editor.onOutputHtml = {
            val intent = Intent(this, HtmlActivity::class.java)
            intent.putExtra("html", it)
            startActivity(intent)
        }

        val width = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 40f,
            resources.displayMetrics
        ).toInt()
        val padding = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 9f,
            resources.displayMetrics
        ).toInt()

        menu.forEachIndexed { index, type ->
            val menu = MenuImageView(this).apply {
                this.type = type
                layoutParams = LinearLayout.LayoutParams(width, width)
                setPadding(padding, padding, padding, padding)
                setImageResource(menuRes[index])
                setOnClickListener { operator.invoke(type) }
            }
            menu_container.addView(menu)
            menuView.add(menu)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                ADD_COVER -> {
                    data?.data?.let {
                        //返回的Uri为content类型的Uri,不能进行复制等操作,需要转换为文件Uri
                        var uri = convertUri(it)
                        startImageZoom(uri!!)
                    }
                }
                CROP_CODE -> {
                    val extras = data?.getExtras()
                    if (extras != null) {
                        //获取到裁剪后的图像
                        val bm = extras.getParcelable<Bitmap>("data")
                        val urlpath = FileUtil.saveFile(this@MainActivity, "cover.jpg", bm!!);
                        editor.addCover(urlpath)
                    }
                }
                INSERT_IMG -> {
                    data?.data?.let {
                        //返回的Uri为content类型的Uri,不能进行复制等操作,需要转换为文件Uri
                        val cursor = contentResolver.query(it, null, null, null, null)
                        if (cursor != null && cursor.moveToFirst()) {
                            val path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                            val size = FileUtil.getBitmapSize(path)
                            editor.insertImage(path, size[0], size[1])
                        }

                    }
                }
            }
        }
    }

    /**
     * 将Bitmap写入SD卡中的一个文件中,并返回写入文件的Uri
     * @param bm
     * @param dirPath
     * @return
     */
    private fun saveBitmap(bm: Bitmap, dirPath: String): Uri? {
        //新建文件夹用于存放裁剪后的图片
        val tmpDir = File(Environment.getExternalStorageDirectory().path + "/" + dirPath)
        if (!tmpDir.exists()) {
            tmpDir.mkdir()
        }

        //新建文件存储裁剪后的图片
        val img = File(tmpDir.getAbsolutePath() + "/avator.png")
        try {
            //打开文件输出流
            val fos = FileOutputStream(img)
            //将bitmap压缩后写入输出流(参数依次为图片格式、图片质量和输出流)
            bm.compress(Bitmap.CompressFormat.PNG, 85, fos)
            //刷新输出流
            fos.flush()
            //关闭输出流
            fos.close()
            //返回File类型的Uri
            return Uri.fromFile(img)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return null
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

    }

    /**
     * 将content类型的Uri转化为文件类型的Uri
     * @param uri
     * @return
     */
    private fun convertUri(uri: Uri): Uri? {
        val `is`: InputStream?
        try {
            //Uri ----> InputStream
            `is` = contentResolver.openInputStream(uri)
            //InputStream ----> Bitmap
            val bm = BitmapFactory.decodeStream(`is`)
            //关闭流
            `is`!!.close()
            return saveBitmap(bm, "temp")
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return null
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

    }

    /**
     * 通过Uri传递图像信息以供裁剪
     * @param uri
     */
    private fun startImageZoom(uri: Uri) {
        //构建隐式Intent来启动裁剪程序
        val intent = Intent("com.android.camera.action.CROP")
        //设置数据uri和类型为图片类型
        intent.setDataAndType(uri, "image/*")
        //显示View为可裁剪的
        intent.putExtra("crop", true)
        //裁剪的宽高的比例为1:1
        intent.putExtra("aspectX", 16)
        intent.putExtra("aspectY", 9)
        //输出图片的宽高均为150
        intent.putExtra("outputX", 490)
        intent.putExtra("outputY", 225)
        //裁剪之后的数据是通过Intent返回
        intent.putExtra("return-data", true)
        startActivityForResult(intent, CROP_CODE)
    }
}
