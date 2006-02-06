/*
 * Created on 06-Feb-2006
 */
package uk.org.ponder.darwin.rsf.components;

import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class ErrorProducer implements ViewComponentProducer {
  public static final String VIEWID = "missing";
  public String getViewID() {
    return VIEWID;
  }

  public void fillComponents(UIContainer tofill, ViewParameters origviewparams, ComponentChecker checker) {
    if (origviewparams.errortoken != null) {
      UIOutput.make(tofill, "error-ref:", origviewparams.errortoken);
    }
  }

}
