/*
 * Created on Nov 17, 2005
 */
package uk.org.ponder.darwin.rsf.components;

import uk.org.ponder.darwin.item.ItemCollection;
import uk.org.ponder.darwin.rsf.NavParams;
import uk.org.ponder.darwin.rsf.TextBlockRenderParams;
import uk.org.ponder.darwin.rsf.ViewParamGetter;
import uk.org.ponder.rsac.RSACBeanLocator;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class FramesetProducer implements ViewComponentProducer {
  public static final String VIEWID = "frameset";
  private RSACBeanLocator rbl;
  public String getViewID() {
    return VIEWID;
  }
  public void setRSACBeanLocator(RSACBeanLocator rbl) {
    this.rbl = rbl;
  }

// View params for Frameset are the same as view params for Nav, except that
  // view id is different.
  public void fillComponents(UIContainer tofill, ViewParameters origviewparams, 
      ComponentChecker checker) {
    NavParams navparams = (NavParams) origviewparams.copyBase();
    navparams.viewID = NavFrameProducer.VIEWID;
    
// pass through our parameters to the Nav frame.
    UIInternalLink.make(tofill, ComponentIDs.NAV_FRAME, navparams);
    
// select the correct frameset type
    UIBranchContainer frameset = UIBranchContainer.make(tofill, 
        ComponentIDs.FRAMES_PREFIX + navparams.viewtype);
    if (navparams.viewtype.equals(NavParams.TEXT_VIEW) 
        || navparams.viewtype.equals(NavParams.SIDE_VIEW)) {
      TextBlockRenderParams params = new TextBlockRenderParams();
      params.itemID = navparams.itemID;
      params.pageseq = navparams.pageseq;

      ItemCollection collection = (ItemCollection) rbl.getBeanLocator().locateBean("itemCollection");
      ViewParamGetter.fillTextParams(collection, params);
      if (params.contentfile != null) {
        UIInternalLink.make(frameset, ComponentIDs.TEXT_FRAME, params);
      }
      else {
        SimpleViewParameters missingparam = new SimpleViewParameters();
        missingparam.viewID = "missing"; // currently a void producer
        UIInternalLink.make(frameset, ComponentIDs.TEXT_FRAME, missingparam);
      }
    }
    
  } 
 
}
