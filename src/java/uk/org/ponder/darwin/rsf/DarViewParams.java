/*
 * Created on 14-Dec-2005
 */
package uk.org.ponder.darwin.rsf;

import uk.org.ponder.reflect.FieldHash;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

/**
 * @author Antranig Basman (amb26@ponder.org.uk)
 * 
 */
public class DarViewParams extends SimpleViewParameters {
  private static FieldHash fieldhash = new FieldHash(DarViewParams.class);
  static {
    fieldhash.addField("flowtoken");
    fieldhash.addField("endflow");
    fieldhash.addField("errortoken");
    fieldhash.addField("errorredirect");
    fieldhash.addField("itemID");
    fieldhash.addField("viewtype");
    fieldhash.addField("pageseq");
  }
  public FieldHash getFieldHash() {
    return fieldhash;
  }
  
  public static final String IMAGE_VIEW = "image";
  public static final String TEXT_VIEW = "text";
  public String itemID;
  public String viewtype;
  public int pageseq;
}
