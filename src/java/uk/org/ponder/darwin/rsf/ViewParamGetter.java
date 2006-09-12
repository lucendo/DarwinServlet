/*
 * Created on 23-Jan-2006
 */
package uk.org.ponder.darwin.rsf;

import uk.org.ponder.darwin.item.ContentInfo;
import uk.org.ponder.darwin.item.ItemCollection;
import uk.org.ponder.darwin.item.ItemDetails;
import uk.org.ponder.darwin.item.PageInfo;
import uk.org.ponder.darwin.rsf.params.TextBlockRenderParams;

public class ViewParamGetter {
  public static TextBlockRenderParams fillTextParams(ItemCollection coll, 
      TextBlockRenderParams tofill) {
    ItemDetails details = coll.getItem(tofill.itemID);
    if (details != null) {
      PageInfo pageinfo = (PageInfo) details.pages.get(tofill.pageseq.intValue());
      for (int i = 0; i < details.contents.size(); ++ i) {
        ContentInfo ci = (ContentInfo) details.contents.get(i);
        if (ci.filename.equals(pageinfo.contentfile)) {
          tofill.basepage = new Integer(ci.firstpage);
          break;
        }
      }
    }
    return tofill;
  }
}
