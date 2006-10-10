function updatePageCounter() {
var loc = window.location.href;
if (loc.match(/^http:\/\/[^\/]+\/content\/.*$/) == null) {
  var url = '/Darwin/content/page-counter';
  var params = 'url=' + encodeURIComponent(loc);
  var ajax = new Ajax.Updater(
    {success: 'page-counter'},
     url,
    {method: 'get', parameters: params});

  }

}