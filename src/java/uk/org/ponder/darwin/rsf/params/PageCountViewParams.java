/*
 * Created on 8 Oct 2006
 */
package uk.org.ponder.darwin.rsf.params;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class PageCountViewParams extends SimpleViewParameters{
  public String url;
  public String getParseSpec() {
    return super.getParseSpec() + ", url";
  }
}
