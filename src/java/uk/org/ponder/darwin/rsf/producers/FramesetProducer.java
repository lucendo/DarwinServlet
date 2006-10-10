/*
 * Created on Nov 17, 2005
 */
package uk.org.ponder.darwin.rsf.producers;

import uk.org.ponder.darwin.item.ItemCollection;
import uk.org.ponder.darwin.rsf.ViewParamGetter;
import uk.org.ponder.darwin.rsf.params.NavParams;
import uk.org.ponder.darwin.rsf.params.TextBlockRenderParams;
import uk.org.ponder.rsac.RSACBeanLocator;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

public class FramesetProducer implements ViewComponentProducer,
    ViewParamsReporter {
  public static final String VIEWID = "frameset";

  public String getViewID() {
    return VIEWID;
  }

  public ViewParameters getViewParameters() {
    return new NavParams();
  }

  private RSACBeanLocator rbl;

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
      params.pageseq = new Integer(navparams.pageseq);
      params.keywords = navparams.keywords;
      params.viewtype = navparams.viewtype;
      params.hitpage = navparams.pageseq;

      ItemCollection collection = (ItemCollection) rbl.getBeanLocator()
          .locateBean("itemCollection");
      ViewParamGetter.fillTextParams(collection, params);
      // This line ridiculous fix for initial anchor issue problem
      params.pageseq = null;
      if (params.basepage != null) {
        UIInternalLink.make(frameset, ComponentIDs.TEXT_FRAME, params);
//        StringPOS contentpos = new StringPOS();
//        pageRenderer.renderTextBlock(params, contentpos);
//        UIVerbatim.make(tofill, "noframes", contentpos.toString());
        UIInternalLink.make(tofill, "noframes", params);
        }
      else {
        SimpleViewParameters missingparam = new SimpleViewParameters();
        missingparam.viewID = "missing"; // currently a void producer
        UIInternalLink.make(frameset, ComponentIDs.TEXT_FRAME, missingparam);
      }
    }

  }

}
