function replaceHtmlSymbol(html) {
  if (html == null) {
    return "";
  }
  return html
    .replace(/</gm, "&lt;")
    .replace(/>/gm, "&gt;")
    .replace(/"/gm, "&quot;")
    .replace(/(\r\n|\r|\n)/g, "<br/>");
}

// 获取一个 elem.childNodes 的 JSON 数据
function getChildrenJSON($elem) {
  const result = [];
  const $children = $elem.childNodes() || []; // 注意 childNodes 可以获取文本节点
  $children.forEach(curElem => {
    let elemResult;
    const nodeType = curElem.nodeType;

    // 文本节点
    // if (nodeType === 3) {
    //   elemResult = curElem.textContent;
    //   elemResult = replaceHtmlSymbol(elemResult);
    // }
    if (nodeType === 3) {
      elemResult = {};
      elemResult.tag = "span";
      elemResult.attrs = {};
      elemResult.value = replaceHtmlSymbol(curElem.textContent) || "";
      elemResult.children = [];
    }

    // 普通 DOM 节点
    if (nodeType === 1) {
      elemResult = {};

      // tag
      elemResult.tag = curElem.nodeName.toLowerCase();
      // attr
      const attrData = {};
      const attrList = curElem.attributes || {};
      const attrListLength = attrList.length || 0;
      for (let i = 0; i < attrListLength; i++) {
        const attr = attrList[i];
        attrData[attr.name] = attr.value;
      }
      elemResult.attrs = attrData;
      // children（递归）
      elemResult.children = getChildrenJSON($(curElem));
      // a标签
      if (elemResult.tag === "a") {
        elemResult.link = elemResult.attrs.href;
        elemResult.value = curElem.textContent;
      }
      //img标签
      if (elemResult.tag === "img") {
        elemResult.value = elemResult.attrs.src || "";
        elemResult.children = [];
      }
      //iframe标签
      if (elemResult.tag === "iframe") {
        const src = elemResult.attrs.src;
        let value = "";
        if (src) {
          if (/^(http|https):\/\/.+$/.test(src)) {
            value = src;
          } else {
            const hasSlashPrefix = /^\/\//.test(src);
            const protocol = "https:";
            value = hasSlashPrefix
              ? `${protocol}${src}`
              : `${protocol}//${src}`;
          }
        }
        elemResult.value = value;
        elemResult.children = [];
      }
    }
    if (elemResult) {
      result.push(elemResult);
    }
  });
  return result;
}
