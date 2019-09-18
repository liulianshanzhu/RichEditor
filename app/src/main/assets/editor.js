'use strict';//进入"严格模式"的编译指示

var EDITOR = {
    setting: {
		screenWidth: 0,
		screenDpr: 0,
		margin: 20
	},
	imageCache: new Map(),
	init: function init() {
        //初始化内部变量
        var _self = this;
        _self.initSetting();
    },
    commandSet: ['bold', 'italic', 'strikethrough', 'underline', 'redo', 'undo', 'superscript', 'subscript'],
    initSetting: function initSetting() {
        var _self = this;
        _self.setting.screenWidth = window.innerWidth - 50;
        _self.setting.screenDpr = window.devicePixelRatio;
    },
    insertHtml: function insertHtml(html) {
		var _self = this;
		document.execCommand('insertHtml', false, html);
	},
    insertCover:function insertCover(url) {
        document.getElementById('add_cover_tip').style.display='none';
        document.getElementById('replace_cover_tip').style.display='inline';
        document.getElementById('cover').src=''+url+'';
    },
    insertImage: function insertImage(url, width, height, id) {
        var _self = this;
        var newWidth = 0,
            newHeight = 0;
        var _self$setting = _self.setting,
            screenWidth = _self$setting.screenWidth,
            screenDpr = _self$setting.screenDpr;

        if (width > screenWidth * screenDpr) {
            newWidth = screenWidth;
            newHeight = height * newWidth / width;
        } else {
            newWidth = width / screenDpr;
            newHeight = height / screenDpr;
        }
        var image = '<div class="image-area" id="'+id+'">\n\t<div style="width:auto;display: inline-block;">\n\t\t<div style="position:relative;text-align:center;width:100%;">\n\t\t\t<img src="'+url+'"style="width:'+newWidth+'px;height:'+newHeight+'px;" class="image">\n\t\t\t<img src="ic_image_delete.png" id="'+id+'" class="image_delete" onclick="EDITOR.removeImage(\''+id+'\')">\n\t\t</div>\n\t</div>\n</div><br>\n'
        _self.insertHtml(image);
        _self.imageCache.set(id, url);
    },
    saveRange: function saveRange() {
        //保存节点位置
        var _self = this;
        var selection = window.getSelection();
        if (selection.rangeCount > 0) {
            var range = selection.getRangeAt(0);
            var startContainer = range.startContainer,
                startOffset = range.startOffset,
                endContainer = range.endContainer,
                endOffset = range.endOffset;

            _self.currentRange = {
                startContainer: range.startContainer,
                startOffset: range.startOffset,
                endContainer: range.endContainer,
                endOffset: range.endOffset
            };
        }
    },
    removeImage: function removeImage(id) {
        var _self = this;
        var node = document.getElementById(id);
        node.parentNode.removeChild(node)
        _self.imageCache.delete(id)
    },
    setTextColor: function setTextColor(color) {
        document.execCommand("styleWithCSS", null, true);
        document.execCommand('foreColor', false, color);
        document.execCommand("styleWithCSS", null, false);
    },
    insertLine: function insertLine() {
        var _self = this;
        var html = '<hr><div><br></div>';
        _self.insertHtml(html);
    },
    insertBr: function insertBr() {
        var _self = this;
        var html = '<div><br></div>';
        _self.insertHtml(html);
    },
    setJustifyLeft: function setJustifyLeft() {
        document.execCommand('justifyLeft', false, null);
    },
    setJustifyCenter: function setJustifyCenter() {
        document.execCommand('justifyCenter', false, null);
    },
    setJustifyRight: function setJustifyRight() {
        document.execCommand('justifyRight', false, null);
    },
    setUnorderedList: function setUnorderedList() {
        document.execCommand('insertUnorderedList', false, null);
    },
    setOrderedList: function setOrderedList() {
        document.execCommand('insertOrderedList', false, null);
    },
    insertLink: function insertLink(url, name) {
        var _self = this;
        var html = '<a href="' + url + '" class="link">' + name + '</a>';
        _self.insertHtml(html);
    },
    exec: function exec(command) {
        //执行指令
        var _self = this;
        if (_self.commandSet.indexOf(command) !== -1) {
            document.execCommand(command, false, null);
        } else {
            var value = '<' + command + '>';
            document.execCommand('formatBlock', false, value);
        }
    },
    getImageList: function getImageList() {
        var _self = this;
        var json = ''
        _self.imageCache.forEach(function (value, key, map) {
            json = json + value + ','
        });
        EditorJs.getImageListJson(json);
    },
    getJson: function getJson() {
        var editor = document.querySelector("#editor_parent");
        var result = getChildrenJSON($(editor.innerHTML));
        var str = JSON.stringify(result);
        EditorJs.getHtmlJson(str);
    }
}
EDITOR.init();

