/*
 * Created on 12 Sep 2006
 */
package uk.org.ponder.darwin.rsf;

import java.io.FileInputStream;
import java.io.InputStream;

import uk.org.ponder.darwin.item.ItemCollection;
import uk.org.ponder.darwin.item.ItemDetails;
import uk.org.ponder.darwin.item.PageInfo;
import uk.org.ponder.darwin.parse.ContentParser;
import uk.org.ponder.darwin.parse.URLMapper;
import uk.org.ponder.darwin.rsf.params.TextBlockRenderParams;
import uk.org.ponder.rsf.viewstate.ViewStateHandler;
import uk.org.ponder.streamutil.StreamCloseUtil;
import uk.org.ponder.streamutil.write.PrintOutputStream;
import uk.org.ponder.util.UniversalRuntimeException;

public class PageRenderer {
  private ItemCollection collection;
  private URLMapper urlmapper;
  private ViewStateHandler viewstatehandler;

  public void setItemCollection(ItemCollection collection) {
    this.collection = collection;
  }
  
  public void setURLMapper(URLMapper urlmapper) {
    this.urlmapper = urlmapper;
  }

  public void setViewStateHandler(ViewStateHandler viewstatehandler) {
    this.viewstatehandler = viewstatehandler;
  }
  
  public void renderTextBlock(TextBlockRenderParams params,
      PrintOutputStream pos) {
    ItemDetails item = collection.getItem(params.itemID);
    PageInfo pi = (PageInfo) item.pages.get(params.basepage.intValue());
    String fullpath = pi.contentfile;

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
      is = new FileInputStream(fullpath);
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

}