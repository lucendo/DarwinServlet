/*
 * Created on 14-Dec-2005
 */
package uk.org.ponder.darwin.rsf;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.stringutil.StringList;

/**
 * View parameters that will fetch content for a single page.
 * @author Antranig Basman (amb26@ponder.org.uk)
 * 
 */
public class PageRenderParams extends SimpleViewParameters {
  public static final String VIEWID = "content";
  private static StringList attrnames = 
    StringList.fromString("flowtoken, endflow, errortoken, errorredirect, itemID, viewtype, pageseq");
 
  public StringList getAttributeFields() {
    return attrnames;
   }

  public PageRenderParams() {
    viewID = VIEWID;
  }
  
  public static final String IMAGE_VIEW = "image";
  public static final String TEXT_VIEW = "text";
  public String itemID;
  public String viewtype;
  public int pageseq;
}