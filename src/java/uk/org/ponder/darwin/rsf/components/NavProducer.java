/*
 * Created on 14-Dec-2005
 */
package uk.org.ponder.darwin.rsf.components;

import uk.org.ponder.darwin.item.ItemCollection;
import uk.org.ponder.darwin.item.ItemDetails;
import uk.org.ponder.darwin.item.PageInfo;
import uk.org.ponder.darwin.rsf.ContentRenderParams;
import uk.org.ponder.darwin.rsf.NavParams;
import uk.org.ponder.rsac.RSACBeanLocator;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewStateHandler;

/**
 * @author Antranig Basman (amb26@ponder.org.uk)
 * 
 */
public class NavProducer implements ViewComponentProducer {
  public static final String VIEWID = "nav-frame";
  private ItemCollection collection;
  private RSACBeanLocator rbl;

  public String getViewID() {
    return VIEWID;
  }

  public void setItemCollection(ItemCollection collection) {
    this.collection = collection;
  }

  public void setRSACBeanLocator(RSACBeanLocator rbl) {
    this.rbl = rbl;
  }

  public void fillComponents(UIContainer tofill, ViewParameters origviewparams,
      ComponentChecker checker) {

    NavParams navparams = (NavParams) origviewparams;
    ContentRenderParams contentparams = new ContentRenderParams();
    contentparams.itemID = navparams.itemID;

    ItemDetails item = collection.getItem(navparams.itemID);
    ViewStateHandler vsh = (ViewStateHandler) rbl.getBeanLocator().locateBean(
        "viewStateHandler");
    if (navparams.viewtype.equals(NavParams.TEXT_VIEW)
        || navparams.viewtype.equals(NavParams.SIDE_VIEW)) {
      contentparams.viewtype = ContentRenderParams.TEXT_VIEW;
      for (int i = 0; i < item.pages.size(); ++i) {
        PageInfo page = (PageInfo) item.pages.get(i);
        contentparams.pageseq = page.sequence;
        String texturl = vsh.getFullURL(contentparams);
        UIOutput.make(tofill, ComponentIDs.TEXT_TARGET, texturl);
      }
    }
    if (navparams.viewtype.equals(NavParams.IMAGE_VIEW)
        || navparams.viewtype.equals(NavParams.SIDE_VIEW)) {
      for (int i = 0; i < item.pages.size(); ++i) {
        contentparams.viewtype = ContentRenderParams.IMAGE_VIEW;
        PageInfo page = (PageInfo) item.pages.get(i);
        contentparams.pageseq = page.sequence;
        String imageurl = vsh.getFullURL(contentparams);
        UIOutput.make(tofill, ComponentIDs.IMAGE_TARGET, imageurl);
      }
    }

    String[] values = new String[item.pages.size()];
    String[] labels = new String[item.pages.size()];

    for (int i = 0; i < item.pages.size(); ++i) {
      PageInfo page = (PageInfo) item.pages.get(i);
      values[i] = Integer.toString(page.sequence);
      labels[i] = page.text;
    }

    UISelect.make(tofill, ComponentIDs.PAGE_SELECT, values, labels, values[0]);
  }

}
