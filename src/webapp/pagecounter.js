function updatePageCounter() {
var loc = window.location.href;
if (loc.match(/^http:\/\/[^\/]+\/Darwin\/content\/.*$/) == null) {
  var url = '/Darwin/content/page-counter';
  var params = 'url=' + encodeURIComponent(loc);
  var ajax = new Ajax.Updater(
    {success: 'page-counter'},
     url,
    {method: 'get', parameters: params});

  }
}


function popup(mylink, windowname)
{
if (! window.focus)return true;
var href;
if (typeof(mylink) == 'string')
   href=mylink;
else
   href=mylink.href;
window.open(href, windowname, 'width=700,height=500,scrollbars=yes');
return false;
}
