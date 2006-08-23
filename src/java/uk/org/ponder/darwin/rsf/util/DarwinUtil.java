/*
 * Created on 18-May-2006
 */
package uk.org.ponder.darwin.rsf.util;

import uk.org.ponder.darwin.item.ItemCollection;
import uk.org.ponder.darwin.item.ItemDetails;
import uk.org.ponder.darwin.rsf.NavParams;

public class DarwinUtil {
  public static void chooseBestView(NavParams tofill, ItemCollection collection) {
    ItemDetails id = collection.getItem(tofill.itemID);
    if (id.hasimage) {
      if (id.hastext) {
//        tofill.viewtype = NavParams.SIDE_VIEW;
        // Trollish edict 19/08/06
        tofill.viewtype = NavParams.TEXT_VIEW;
      }
      else {
        tofill.viewtype = NavParams.IMAGE_VIEW;
      }
    }
    else tofill.viewtype = NavParams.TEXT_VIEW;
  }
}
