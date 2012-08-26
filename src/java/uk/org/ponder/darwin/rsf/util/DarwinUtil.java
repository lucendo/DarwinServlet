/*
 * Created on 18-May-2006
 */
package uk.org.ponder.darwin.rsf.util;

import uk.org.ponder.darwin.item.ItemCollection;
import uk.org.ponder.darwin.item.ItemDetails;
import uk.org.ponder.darwin.rsf.params.NavParams;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.util.Logger;

public class DarwinUtil {
  public static void chooseBestView(NavParams tofill, ItemCollection collection) {
    ItemDetails id = collection.getItem(tofill.itemID);
    if (id == null) {
      Logger.log.warn("Item not found for ID " + tofill.itemID + ": defaulting to text view");
      tofill.viewtype = NavParams.TEXT_VIEW;
      return;
    }
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
  public static void addStandardComponents(UIContainer tofill) {
      new UIJointContainer(tofill, "header:", "header-target:");
      new UIJointContainer(tofill, "footer:", "footer-target:");
  }
}
