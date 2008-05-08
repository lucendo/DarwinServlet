/*
 * Created on 23 Aug 2007
 */
package uk.org.ponder.darwin.rsf.producers;

import uk.org.ponder.darwin.item.ItemCollection;
import uk.org.ponder.darwin.item.ItemDetails;
import uk.org.ponder.darwin.rsf.params.NavParams;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIInternalLink;

public class LinkBlockProducer {
  private ItemCollection itemcollection;

  public void setItemCollection(ItemCollection itemcollection) {
    this.itemcollection = itemcollection;
  }
  
  private static NavParams getNavParams(String itemID) {
    NavParams navparams = new NavParams();
    navparams.viewID = FramesetProducer.VIEWID;
    navparams.itemID = itemID;
    navparams.pageseq = 1;
    return navparams;
  }
  
  public void makeLinkBlock(UIContainer tofill, String itemID) {
    ItemDetails item = itemcollection.getItem(itemID);
    if (item != null) {
    if (item.hasimage && item.hastext) {
      NavParams sideparams = getNavParams(itemID);
      sideparams.viewtype = NavParams.SIDE_VIEW;
      UIInternalLink.make(tofill, "switch-side", sideparams);
    }
    if (item.hasimage) {
      NavParams sideparams = getNavParams(itemID);
      sideparams.viewtype = NavParams.IMAGE_VIEW;
      UIInternalLink.make(tofill, "switch-image", sideparams);
    }
    if (item.hastext) {
      NavParams sideparams = getNavParams(itemID);
      sideparams.viewtype = NavParams.TEXT_VIEW;
      UIInternalLink.make(tofill, "switch-text", sideparams);
    }
  }
  }
}
