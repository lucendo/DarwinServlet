/*
 * Created on 24 Aug 2006
 */
package uk.org.ponder.darwin.rsf.params;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class RecordParams extends SimpleViewParameters {
  public String itemID;
  public String getParseSpec() {
    return super.getParseSpec() + ", itemID";
  }
}
