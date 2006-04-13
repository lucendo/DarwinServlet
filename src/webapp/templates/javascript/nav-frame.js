
var ns4 = (document.layers);
var ie4 = (document.all && !document.getElementById);
var ie5 = (document.all && document.getElementById);
var ns6 = (!document.all && document.getElementById);

function getElementGlob(dokkument, id) {
   var obj;
   if(ns4) obj = dokkument.layers[id];
   else if(ie4) obj = dokkument.all[id];
     else if(ie5 || ns6) obj = dokkument.getElementById(id);
   return obj;
}

function getElement(id) {
// alert ("gE" + document + " " + id + " ns4 " + ns4);
//   var obj;
//   if(ns4) obj = document.layers[id];
//   else if(ie4) obj = document.all[id];
//     else if(ie5 || ns6) obj = document.getElementById(id);
//   alert ("Returning " + obj);
//   return obj;

  return getElementGlob(document, id);
  }

function getValue(id) {
  return ns4? getElement(id).document : getElement(id).firstChild.nodeValue;
}

function getSelectedPage() {
  return Number(getElement("page-select-selection").selectedIndex + 1);
}

function updatePage() {
  var viewtype = getViewType();
  var currentpage = Number(getPage());
  getElement("back-button").disabled = (currentpage <= 1);
  if (currentpage != getSelectedPage()) {
    //getElement("page-select").options[currentpage - 1].selected = true;
    getElement("page-select-selection").selectedIndex = currentpage - 1;
    }
  var lastpage = Number(getValue("last-page"));
  getElement("next-button").disabled = (currentpage >= lastpage);
 
  if (viewtype=="text" || viewtype=="side") {
    var texttarget=getValue("text-target:"+currentpage);
    var textframeid = "frames:" + viewtype + "::txt-frame";
	  var textframe = getElementGlob(window.parent.document, textframeid);
	  textframe.src = texttarget;
	  }
  if (viewtype=="image" || viewtype=="side") {
	  var imagetarget=getValue("image-target:"+currentpage);
	  var imgframeid = "frames:" + viewtype + "::toolbar";
    var imageframe = parent.frames[imgframeid];
//	    alert ("imageframe: " + imageframe);
	    // + " document: " + imageframe.document);
//	  var viewedimage = getElementGlob(imageframe, "viewed_image");
//	  alert("viewedimg: " + viewedimage);
//  imageframe.viewed_image.src = imagetarget;
	  var image = new Image();
	  image.src = imagetarget;
//	  alert("imagetarget: " + imagetarget);
    imageframe.init(image);
//    imageframe.viewed_image.src = image.src;
	  //window.top.img_frame.document.viewed_image.src = image.src;
	  }
}


function setPage(num) {
  if (num <= 1) num = 1;
  var lastpage = Number(getValue("last-page"));
  if (num >= lastpage) num = lastpage;
  var currentpage = Number(getPage());
  if (currentpage == num) return;

  getElement("current-page").firstChild.nodeValue = num;
  updatePage();
}
function getPage() {
  return getValue("current-page");
}
function getViewType() {
  return getValue("view-type");
  }
function addPage(toadd) {
  page = getPage();
  setPage(Number(getPage()) + toadd);
  }

function onPageSelect() {
  var selected = getElement("page-select-selection").selectedIndex;
  setPage(selected + 1);
}