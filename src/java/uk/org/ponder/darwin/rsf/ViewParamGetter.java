/*
 * Created on 23-Jan-2006
 */
package uk.org.ponder.darwin.rsf;

import uk.org.ponder.darwin.item.ItemCollection;
import uk.org.ponder.darwin.item.ItemDetails;
import uk.org.ponder.darwin.item.PageInfo;

public class ViewParamGetter {
  public static TextBlockRenderParams fillTextParams(ItemCollection coll, 
      TextBlockRenderParams tofill) {
    ItemDetails details = coll.getItem(tofill.itemID);
    if (details != null) {
      PageInfo pageinfo = (PageInfo) details.pages.get(tofill.pageseq);
      tofill.contentfile = pageinfo.contentfile;
    }
    return tofill;
  }
}