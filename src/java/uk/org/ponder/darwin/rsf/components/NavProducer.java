/*
 * Created on 14-Dec-2005
 */
package uk.org.ponder.darwin.rsf.components;

import uk.org.ponder.darwin.parse.ItemCollection;
import uk.org.ponder.darwin.parse.ItemDetails;
import uk.org.ponder.darwin.parse.PageInfo;
import uk.org.ponder.darwin.rsf.DarViewParams;
import uk.org.ponder.rsac.RSACBeanLocator;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.BaseURLProvider;
import uk.org.ponder.rsf.viewstate.ViewParameters;

/**
 * @author Antranig Basman (amb26@ponder.org.uk)
 * 
 */
public class NavProducer implements ViewComponentProducer {
  public static final String VIEWID = "nav-frame";
  private ItemCollection collection;
  private RSACBeanLocator rbl;
  private String renderpath;
  public String getViewID() {
    return VIEWID;
  }

  public void setItemCollection(ItemCollection collection) {
    this.collection = collection;
  }
  // TODO: Move producers into request scope!!
  public void setRSACBeanLocator(RSACBeanLocator rbl) {
    this.rbl = rbl;
  }
  // in general "render/", corresponding with web.xml
  public void setContentRenderPath(String renderpath) {
    this.renderpath = renderpath;
  }
  
  public void fillComponents(UIContainer tofill, 
      ViewParameters origviewparams, ComponentChecker checker) {
    BaseURLProvider bup = 
      (BaseURLProvider) rbl.getBeanLocator().locateBean("baseURLProvider");
    String resourcebase = bup.getResourceBaseURL();
    DarViewParams darview = (DarViewParams) origviewparams;
    ItemDetails item = collection.getItem(darview.itemID);
    for (int i = 0; i < item.pages.size(); ++ i) {
      PageInfo page = (PageInfo) item.pages.get(i);
      String texturl = 
    }
    // TODO Auto-generated method stub
    
  }

}
