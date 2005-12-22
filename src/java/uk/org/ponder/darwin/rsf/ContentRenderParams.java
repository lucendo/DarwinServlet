/*
 * Created on 14-Dec-2005
 */
package uk.org.ponder.darwin.rsf;

import uk.org.ponder.reflect.FieldHash;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

/**
 * @author Antranig Basman (amb26@ponder.org.uk)
 * 
 */
public class ContentRenderParams extends ViewParameters {
  private static FieldHash fieldhash = new FieldHash(SimpleViewParameters.class);
  static {
    fieldhash.addField("itemID");
    fieldhash.addField("pageseq");
  }
  public String itemID;
  public int pageseq;
  public FieldHash getFieldHash() {
    return fieldhash;
  }
  public void clearParams() {
  }
  public void parsePathInfo(String pathinfo) {
    // no pathinfo for crp
  }
  public String toPathInfo() {
    return "";
  }
}
