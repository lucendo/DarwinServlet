/*
 * Created on 24-Jan-2006
 */
package uk.org.ponder.darwin.rsf;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.org.ponder.darwin.item.ItemCollection;
import uk.org.ponder.darwin.item.ItemDetails;
import uk.org.ponder.darwin.item.PageInfo;
import uk.org.ponder.darwin.parse.Extensions;
import uk.org.ponder.darwin.rsf.params.PageRenderParams;
import uk.org.ponder.darwin.rsf.params.TextBlockRenderParams;
import uk.org.ponder.rsf.content.ContentTypeInfoRegistry;
import uk.org.ponder.rsf.processor.HandlerHook;
import uk.org.ponder.rsf.request.EarlyRequestParser;
import uk.org.ponder.rsf.servlet.RootHandlerBean;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.streamutil.StreamCopyUtil;
import uk.org.ponder.streamutil.write.PrintOutputStream;
import uk.org.ponder.util.UniversalRuntimeException;

public class DarwinHandlerHook implements HandlerHook {

  private static org.apache.log4j.Logger accesslog = org.apache.log4j.Logger.getLogger("accesslog");
  
  private HttpServletRequest request;
  private HttpServletResponse response;
  private String requesttype;
  private ViewParameters viewparams;
  private HandlerHook handlerhook;
  private ItemCollection collection;
  private PageRenderer pageRenderer;

  public void setHttpServletRequest(HttpServletRequest request) {
    this.request = request;
  }

  public void setPageRenderer(PageRenderer pageRenderer) {
    this.pageRenderer = pageRenderer;
  }
  
  public void setHttpServletResponse(HttpServletResponse response) {
    this.response = response;
  }

  public void setRequestType(String requesttype) {
    this.requesttype = requesttype;
  }

  public void setViewParameters(ViewParameters viewparams) {
    this.viewparams = viewparams;
  }

  public void setHandlerHook(HandlerHook handlerhook) {
    this.handlerhook = handlerhook;
  }

  public boolean handle() {
    logAccess();
    if (handlerhook == null || !handlerhook.handle()) {
      if (requesttype.equals(EarlyRequestParser.RENDER_REQUEST)) {
        if (viewparams instanceof PageRenderParams) {
          renderPage((PageRenderParams) viewparams);
          return true;
        }
        if (viewparams instanceof TextBlockRenderParams) {
          PrintOutputStream pos = RootHandlerBean.setupResponseWriter(
              ContentTypeInfoRegistry.HTML_CONTENTINFO.contentTypeHeader,
              request, response);
          pageRenderer.renderTextBlock((TextBlockRenderParams) viewparams, pos);
          return true;
        }
      }
      return false;
    }
    else {
      return true;
    }
  }

  private void logAccess() {
    String ip = request.getRemoteAddr();
    String url = request.getRequestURL().append('?').append(request.getQueryString()).toString();
    accesslog.info(request.getMethod() + " from " + ip + " " + url);
  }

  private void renderPage(PageRenderParams params) {
    ItemDetails item = collection.getItem(params.itemID);
    PageInfo pi = (PageInfo) item.pages.get(params.pageseq);
    if (params.viewtype.equals(PageRenderParams.IMAGE_VIEW)) {
      String fullpath = pi.imagefile;
      String extension = Extensions.getExtension(pi.imagefile);
      String mimetype = Extensions.getMIMEType(extension);
      if (mimetype == null) {
        throw UniversalRuntimeException.accumulate(
            new IllegalArgumentException(), "Unrecognised image extension "
                + extension);
      }
      try {
        response.setContentType(mimetype);
        InputStream imagestream = new FileInputStream(fullpath);
        OutputStream os = response.getOutputStream();
        StreamCopyUtil.inputToOutput(imagestream, os, true, true, null);
      }
      catch (IOException ioe) {
        throw UniversalRuntimeException.accumulate(ioe,
            "Error serving content for " + params + " from path " + fullpath);
      }
    }

  }

}
