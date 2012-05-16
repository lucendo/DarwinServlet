// Image Magnification Functions v2.0 (Feb 19, 2004)
// Written by Dr Michael Hawkins, The Newton Project http://www.newtonproject.ic.ac.uk
// Reorganised, with additions by Dr. Antranig Basman, The Complete Work of
// Charles Darwin Online http://darwin-online.org.uk

// These functions are written to change the size of only one image:
// parent.mainframe.document.viewed_image
// but this could easily be changed by changing the functions so that
// they accept a value containing the image name

var mag_factor = 1.1;  // this is the magnification factor 1.1 = 10%
var clicks = 0;

var bse, first_pag, final_pag, pag, first_time;
var fit_width_mode = 0, fit_both_mode = 0;

var new_page = 0;
var currentimage;

var overlay;

function imageloaded() {
  //alert("imageloaded "+currentimage.width+"x"+currentimage.height);

  original_w = currentimage.width;
  original_h = currentimage.height;

  if (new_page == 1) {
    new_page = 0;
    clear_modes();
    set_initial_zoom();
    }
  if (fit_width_mode) {
    fit_width();
    }
  else if (fit_both_mode) {
    fit_both();
    }
  zoom();
  parent.frames.imageframe.document.viewed_image.src = currentimage.src;
  overlay.style.display = "none";
  //alert("rightsrc: " + parent.frames.imageframe.document.viewed_image.src);
  }

function pageinit() {
  overlay = parent.frames.imageframe.document.getElementById("overlay");
  new_page = 1;
  }

function init(imagetarget) {
  var image = new Image();
  overlay.style.display = "block";
  currentimage = image;
  image.onload = imageloaded;
  image.src = imagetarget;

  original_w = image.width;
  original_h = image.height;
  //parent.document.viewed_image.src = image.src;
  }

function clear_modes() {
  fit_width_mode = 0;
  fit_both_mode = 0;
}

function bigger() {
    // this function increases the size of the image
  clicks +=1;
  clear_modes();
  zoom();
}

function smaller() {
  // this function decreases the image size
  clicks -= 1;
  clear_modes();  
  zoom();
  }

function orig_size() {
  // reverts image to its original size
  clicks = 0;
  clear_modes();
  zoom();
}

function zoom() {
  // this function controls the resizing of the image
  current_image = parent.frames.imageframe.document.viewed_image;

  current_image.width = original_w*Math.pow(mag_factor,clicks);
  current_image.height = original_h*Math.pow(mag_factor,clicks);
}


function set_initial_zoom() {
  var fct = compute_fit_width();
  if (fct < 0) {
    fit_width();
    }
  else {
    orig_size();
    }
  }

function compute_fit_width() {

  var fct;
    // the next 4 lines determine the magnification factor to fill the window width and applies it to the height
  mg = document.body.clientWidth - 20;
  fct = ((Math.log(mg/original_w)/Math.log(mag_factor)));
  return fct;
  }

function fit_width() {
    // this function resizes the image to fill the current window width
  clicks = compute_fit_width();
  if (clicks > 0) {
    clicks = 0;
    }
  
  clear_modes();
  fit_width_mode = 1;
  zoom();

}

function fit_both() {
    // this function resizes the image to fill the current window width

    // the next 4 lines determine the magnification factor to fill the window width and applies it to the height
  mw = parent.frames.imageframe.document.body.clientWidth - 20;
  fctw = ((Math.log(mw/original_w)/Math.log(mag_factor)) );

  mh = parent.frames.imageframe.document.body.clientHeight - 20;
  fcth = ((Math.log(mh/original_h)/Math.log(mag_factor)) );

  fct = Math.min(fctw, fcth);

  clicks = fct;

  if (clicks > 0) {
    clicks = 0;
    }
  clear_modes();
  fit_both_mode = 1;
  zoom();
}
