Android的富文本编辑器，基于webview实现，已进行屏幕适配，兼容多种屏幕大小，支持html转json数据，主要有以下功能（后续待完善）：  
1、**BOLD（粗体）**  
2、**ITALIC（斜体）**  
3、**STRIKETHROUGH（删除线）**  
4、**UNDERLINE（下划线）**  
5、**SUPERSCRIPT（上标）**  
6、**SUBSCRIPT（下标）**  
7、**H1、2、3、4、5、6（各类标题）**  
8、**TEXTCOLOR（字体颜色）**  
9、**HR（分割线）**  
10、**BLOCKQUOTE（引用）**  
11、**ALIGN_LEFT、ALIGN_CENTER、ALIGN_RIGHT（段落对齐方式：左对齐、居中对齐、向右对齐）**  
12、**UL、OL（无序、有序列表）**  
13、**LINK（超链接）**  
14、**IMG（插入图片）**

编辑器分为三部分：封面、标题、正文  
整体在html实现，具体参见 [editor.html](https://github.com/liulianshanzhu/RichEditor/blob/master/app/src/main/assets/editor.html)  
点击添加封面，通过 *EditorJs.addCover()* 方法通知[RichEditor](https://github.com/liulianshanzhu/RichEditor/blob/master/app/src/main/java/com/durian/richeditor/editor/RichEditor.kt)进行处理  
屏幕适配需将px转换成rem。详情查看[rem.js](https://github.com/liulianshanzhu/RichEditor/blob/master/app/src/main/assets/rem.js) 
  
**支持html转json***  
调用editor.getJson()方法，可以在EditorJs.getHtmlJson(json)获取转换后的json数据  

**支持获取图片路径列表，方便上传服务器**  
调用editor.getImageList()方法，可以在EditorJs.getImageListJson(json: String)返回一个字符串，格式img-1-url,img-2-url...可自行切割获取Array数组  

**实时获取标题和文本内容**  
在Editor.titleChange(title)和Editor.contentChange(content)监听，注意ui交互时切换为主线程  


|**小知识点**|  
1、一些相对布局对齐效果无法实现的情况下，可以通过多嵌套外层布局来达到目的，具体可参见插入图片（含删除）的代码添加  
2、.js文件需要严格注明标点符号，不能缺失，必须形成严格的闭合，否则调用无效  
3、各种样式显示，如：删除线、引用块，可以在.css文件定义，但需要在.html引入  
4、编辑器基于webview实现，为满足绝大部分功能，必须在编辑器[RichEditor](https://github.com/liulianshanzhu/RichEditor/blob/master/app/src/main/java/com/durian/richeditor/editor/RichEditor.kt)开启*settings.javaScriptEnabled*  
5、封面布局的实现是控制多个控件的显示，选择图片后的url赋值给<img> src属性；在未选择图片时，可以设置<img alt="">，隐藏占位图  
6、如webview添加某些控件后可以左右滑动，可对该控件进行包裹，并设置合适的宽度  




