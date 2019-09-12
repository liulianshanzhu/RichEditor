package com.durian.richeditor.menu

/**
 * @author Durian
 * @since 2019/9/11 16:18
 */
enum class MenuType(val type: Int) {
    CODE(-1),  //跳转到页面查看代码详情
    NONE(0), //常规字体
    PIC(1),  //插入图片
    BOLD(2),  //粗体
    ITALIC(3), //斜体
    STRIKETHROUGH(4), //删除线
    UNDERLINE(5),  //下划线
    SUPERSCRIPT(6),   //上标
    SUBSCRIPT(7),  //下标
    H1(8),
    H2(9),
    H3(10),
    H4(11),
    H5(12),
    H6(13),
    TEXTCOLOR(14),
    HR(15),
    BLOCKQUOTE(16),
    ALIGN_LEFT(17),
    ALIGN_CENTER(18),
    ALIGN_RIGHT(19),
    UL(20),
    OL(21),
    LINK(22),

    REDO(99), //撤回
    UNDO(100)//回退

}