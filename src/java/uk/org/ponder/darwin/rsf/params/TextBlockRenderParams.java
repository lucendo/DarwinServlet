/*
 * Created on 14-Dec-2005
 */
package uk.org.ponder.darwin.rsf.params;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

/**
 * @author Antranig Basman (amb26@ponder.org.uk)
 * 
 */
public class TextBlockRenderParams extends SimpleViewParameters {
  public static final String VIEWID = "contentblock";
 
  public String getParseSpec() {
    return super.getParseSpec() + 
      ", itemID, basepage, keywords, viewtype, hitpage";
  }
  
  public String getAnchorField() {
    return "pageseq";
  }

  public TextBlockRenderParams() {
    viewID = VIEWID;
  }
  
  public String itemID;
  public Integer basepage;
  public String keywords;
  public String viewtype;
  public int hitpage;
  
  public Integer pageseq;
}