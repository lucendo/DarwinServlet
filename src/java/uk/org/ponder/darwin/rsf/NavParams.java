/*
 * Created on 14-Dec-2005
 */
package uk.org.ponder.darwin.rsf;

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
  public String viewtype;
  public int pageseq;
  
  public String keywords;
}
