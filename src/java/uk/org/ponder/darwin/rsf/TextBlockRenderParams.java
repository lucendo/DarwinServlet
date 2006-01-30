/*
 * Created on 14-Dec-2005
 */
package uk.org.ponder.darwin.rsf;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.stringutil.StringList;

/**
 * @author Antranig Basman (amb26@ponder.org.uk)
 * 
 */
public class TextBlockRenderParams extends SimpleViewParameters {
  public static final String VIEWID = "contentblock";
  private static StringList attrnames = 
    StringList.fromString("flowtoken, endflow, errortoken, errorredirect, itemID, contentfile");
 
  public StringList getAttributeFields() {
    return attrnames;
   }
  
  public String getAnchorField() {
    return "pageseq";
  }

  public TextBlockRenderParams() {
    viewID = VIEWID;
  }
  
  public String itemID;
  public String contentfile;
  public int pageseq;
}