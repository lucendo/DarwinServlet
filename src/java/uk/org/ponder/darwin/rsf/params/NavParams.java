/*
 * Created on 14-Dec-2005
 */
package uk.org.ponder.darwin.rsf.params;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

/**
 * Specifies either the top frameset, or navigation frame.
 * @author Antranig Basman (amb26@ponder.org.uk)
 * 
 */
public class NavParams extends SimpleViewParameters {
  public String getParseSpec() {
    return super.getParseSpec() + ", itemID, viewtype, pageseq, keywords";
  }
  
  public static final String IMAGE_VIEW = "image";
  public static final String TEXT_VIEW = "text";
  public static final String SIDE_VIEW = "side";
  public String itemID;
  // Added to satisfy trollish edict 30/08/06 for text links to go to side view
  public String viewtype;
  public int pageseq;
  
  public String keywords;
}
