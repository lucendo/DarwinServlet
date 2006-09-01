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
import uk.org.ponder.darwin.parse.ContentParser;
import uk.org.ponder.darwin.parse.Extensions;
import uk.org.ponder.darwin.parse.URLMapper;
import uk.org.ponder.darwin.rsf.params.PageRenderParams;
import uk.org.ponder.darwin.rsf.params.TextBlockRenderParams;
import uk.org.ponder.rsf.content.ContentTypeInfoRegistry;
import uk.org.ponder.rsf.processor.HandlerHook;
import uk.org.ponder.rsf.request.EarlyRequestParser;
import uk.org.ponder.rsf.servlet.RootHandlerBean;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewStateHandler;
import uk.org.ponder.streamutil.StreamCloseUtil;
import uk.org.ponder.streamutil.StreamCopyUtil;
import uk.org.ponder.streamutil.write.PrintOutputStream;
import uk.org.ponder.util.UniversalRuntimeException;

public class DarwinHandlerHook implements HandlerHook {

  private HttpServletRequest request;
  private HttpServletResponse response;
  private String requesttype;
  private ViewParameters viewparams;
  private HandlerHook handlerhook;
  private ItemCollection collection;
  private URLMapper urlmapper;
  private ViewStateHandler viewstatehandler;

  public void setHttpServletRequest(HttpServletRequest request) {
    this.request = request;
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
  
  public void setItemCollection(ItemCollection collection) {
    this.collection = collection;
  }
  
  public void setURLMapper(URLMapper urlmapper) {
    this.urlmapper = urlmapper;
  }
  
  public void setViewStateHandler(ViewStateHandler viewstatehandler) {
    this.viewstatehandler = viewstatehandler;
  }
  
  public boolean handle() {
    if (handlerhook == null || !handlerhook.handle()) {
      if (requesttype.equals(EarlyRequestParser.RENDER_REQUEST)) {
        if (viewparams instanceof PageRenderParams) {
          renderPage((PageRenderParams) viewparams);
          return true;
        }
        if (viewparams instanceof TextBlockRenderParams) {
          renderTextBlock((TextBlockRenderParams) viewparams);
          return true;
        }
      }
      return false; 
    }
    else {
      return true;
    }
  }

  private void renderTextBlock(TextBlockRenderParams params) {
    ItemDetails item = collection.getItem(params.itemID);
    //PageInfo pi = (PageInfo) item.pages.get(params.pageseq.intValue());
    // TODO: This is probably quite a security risk!!
    String fullpath = params.contentfile;
    
    PrintOutputStream pos = RootHandlerBean.setupResponseWriter(
        ContentTypeInfoRegistry.HTML_CONTENTINFO.contentTypeHeader,
        request, response);
    RenderingParseReceiver rpr = new RenderingParseReceiver();
    rpr.setURLMapper(urlmapper);
    rpr.setOutputStream(pos);
    rpr.setItemCollection(collection);
    rpr.setViewStateHandler(viewstatehandler);
    rpr.setViewParams(params);
    rpr.setHitPage(params.hitpage);
    ContentParser cp = new ContentParser();
    InputStream is = null;
    try {
      is =  new FileInputStream(fullpath);
      cp.parse(is, fullpath, rpr);
    
    }
    catch (Exception e) {
      throw UniversalRuntimeException.accumulate(e, 
          "Error rendering content for " + params + " from path " + fullpath);
    }
    finally {
      StreamCloseUtil.closeInputStream(is);
      pos.close();
    }
    
  }

  private void renderPage(PageRenderParams params) {
    ItemDetails item = collection.getItem(params.itemID);
    PageInfo pi = (PageInfo) item.pages.get(params.pageseq);
    if (params.viewtype.equals(PageRenderParams.IMAGE_VIEW)) {
      String fullpath = pi.imagefile;
      String extension = Extensions.getExtension(pi.imagefile);
      String mimetype = Extensions.getMIMEType(extension);
      if (mimetype == null) {
        throw UniversalRuntimeException.accumulate(new IllegalArgumentException(), 
            "Unrecognised image extension " + extension);
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
