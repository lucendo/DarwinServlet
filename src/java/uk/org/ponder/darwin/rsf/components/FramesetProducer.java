/*
 * Created on Nov 17, 2005
 */
package uk.org.ponder.darwin.rsf.components;

import uk.org.ponder.darwin.rsf.NavParams;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class FramesetProducer implements ViewComponentProducer {

  public String getViewID() {
    return "frameset";
  }

  public void fillComponents(UIContainer tofill, ViewParameters origviewparams, 
      ComponentChecker checker) {
    NavParams navparams = (NavParams) origviewparams.copyBase();
    navparams.viewID = NavProducer.VIEWID;
    UIInternalLink.make(tofill, ComponentIDs.NAV_FRAME, navparams);
  } 
 
}
