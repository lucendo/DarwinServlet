// Image Magnification Functions v2.0 (Feb 19, 2004)
// These functions are written to change the size of only one image:
// parent.mainframe.document.viewed_image
// but this could easily be changed by changing the functions so that
// they accept a value containing the image name

// Potential solution for non-sequential page numbers:
// ie. manuscripts where the text goes as follows, fo 1r, 1v, 15v, 2r, 2v, 16v, 3r ... 15r, 16r
// Include a 0 in the array position for the missing image - if array[pos]=0 then
// if next = page+1; previous = pag-1

// Solution for displaying the hi-rez image - clicks = -5

var mag_factor = 1.1;  // this is the magnification factor 1.1 = 10%
var clicks;

var bse, first_pag, final_pag, pag, first_time;


function change_rez()
{
rez == 0 ? rez = 1 : rez = 0;
parent.mainframe.document.viewed_image.src = "blackpx.gif";
display_page(pag);
orig_size();
}

function toggle_confirm()
{
	// Warn users the first time they attempt to view a hi-res image that they are VERY large.
	
	var msg;
	msg = "Hi resolution files are roughly 500 K long and take about 90 seconds to load over an average modem connection.\n\nDo you wish to continue?";
	
	if (first_time != 0) change_rez();
	 else {if (confirm(msg)) {first_time = 1; change_rez();}
	 	  }
}


function display_page(new_page)
{
  
curr_Img = parent.frames.mainframe.document.viewed_image;
curr_Img.src = "blackpx.gif";
curr_Img.width=1;
curr_Img.height=1;


if (rez == 0) {
	flname = bse + new_page + ".jpg";
	// The following conditionals determine the image size.
	// if the zero position in either array = 0, then the sizes for the sequence are unique
	// if the zero position in either array = 1, then the sizes for that array are constant
	if (top.mon.lo_h[0] == 1) original_h = top.mon.lo_h[1];
	else original_h=top.mon.lo_h[new_page];
		if (top.mon.lo_w[0] == 1) original_w = top.mon.lo_w[1];
	else original_w=top.mon.lo_w[new_page];
	}
if (rez == 1) {
	flname = bse + new_page + "x.jpg";
	if (top.mon.hi_h[0] == 1) original_h = top.mon.hi_h[1];
	else original_h=top.mon.hi_h[new_page];
	if (top.mon.hi_w[0] == 1) original_w = top.mon.hi_w[1];
	else original_w=top.mon.hi_w[new_page];
	}
	
newContent = '<html><head><title>main</title>';
newContent += '<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">';
newContent += '<META HTTP-EQUIV="imagetoolbar" CONTENT="no">';
newContent += '<script language="JavaScript" type="text/JavaScript">';
newContent += '<\/script>';
newContent += '</head><body bgcolor="#000000" text="#ffffff">';
newContent += '<P align=center><IMG SRC=';
newContent += '"'+ flname + '"';
newContent += ' name="viewed_image" width="' + original_w + '" height="' + original_h +'"></P>';
newContent += '<p align="right"><font size="-2" face="Verdana, Arial, Helvetica, sans-serif"><b><font color="#663300">&copy; 2004 The Newton Project</font></b> - Imperial College London - SW7 2AZ -<br><b> <font color="#663300">tel:</font></b> +44 (0)20 7594 9355 - <b><font color="#663300">fax:</font></b> +44 (0)20 7594 9353 - <b><font color="#663300">email:</font></b> <a href="mailto:j.young@imperial.ac.uk">j.young@imperial.ac.uk</a></font></p>';
newContent += '</body></html>';

parent.frames.mainframe.document.open();
parent.frames.mainframe.document.write(newContent);
parent.frames.mainframe.document.close();
 orig_size();
//window.setTimeout("orig_size();",100);

//curr_Img.src = flname;
document.form1.page.value = pag;
}

function next()
{
{if (final_pag - first_pag == 0) {
	alert("This tract is only 1 page long");
	return;
	}
}
{if (pag == final_pag) {
	alert("This is the last page");
	return;
}
}
temp = parseInt(pag);
pag = temp;
pag += 1;
if (top.mon.lo_h[pag] == 0) pag +=1;
display_page(pag);
}

function previous()
{
if (final_pag - first_pag == 0) {
	alert("This tract is only 1 page long");
	return;
	}
if (pag == first_pag) {
	alert("This is the first page");
	return;
	}
	temp = parseInt(pag);
pag = temp;
	pag -= 1;
	if (top.mon.lo_h[pag] == 0) pag -=1;
display_page(pag);
}

function jump()
{
 // This function verifies entered page information and either displays
 // the desired page or returns various error messages.
 
var f1, f2;
intended = document.form1.page.value;
if (intended == 26) {alert("Page 26 is blank and the image is currently not available.") ; display_page(pag); return;}
   

if ((intended <= final_pag) & (intended >= first_pag))
pag=intended;
   else
     {
         if (isNaN(intended)== true) 
              alert("Invalid Entry\n\nPlease check your typing and omit special characters like spaces and periods.");
           else {
              if (final_pag - first_pag == 0) alert("This tract is only 1 page long");
               else {
              	f1 = first_pag;
                f2 = final_pag;
                alert("Invalid Page Number:\n\n" +  "Valid page for this manuscript are between " + f1 + " and " + f2 + ".");
              }
      }}
display_page(pag);
}


function init()
{
//display_page(pag);
original_w = parent.document.viewed_image.width;
original_h = parent.document.viewed_image.height;
fit_width();
}

function bigger() {
    // this function increases the size of the image
    clicks +=1;
    // the following 4 lines apply the magnification
    zoom();
}

function smaller() {
    // this function decreases the image size
    clicks -= 1;
    // the next 4 lines apply the image zoom out
    
    zoom();
    }

function orig_size() {
    // reverts image to its original size
    clicks = -5;
    zoom();
}

function zoom() {
	// this function controls the resizing of the image
current_image = parent.document.viewed_image;

current_image.width = original_w*Math.pow(mag_factor,clicks);
current_image.height = original_h*Math.pow(mag_factor,clicks);
}

function fit_width() {
    // this function resizes the image to fill the current window width

var fct;
    // the next 4 lines determine the magnification factor to fill the window width and applies it to the height
mg = parent.document.body.clientWidth - 20;
fct = ((Math.log(mg/original_w)/Math.log(mag_factor)) - 0.5);
clicks = parseInt(fct);
zoom();
}
