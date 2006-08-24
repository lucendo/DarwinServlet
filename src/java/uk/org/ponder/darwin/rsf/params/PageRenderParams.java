/*
 * Created on 14-Dec-2005
 */
package uk.org.ponder.darwin.rsf.params;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

/**
 * View parameters that will fetch content for a single page.
 * @author Antranig Basman (amb26@ponder.org.uk)
 * 
 */
public class PageRenderParams extends SimpleViewParameters {
  public static final String VIEWID = "content";
 
  public String getParseSpec() {
    return super.getParseSpec() + ", itemID, viewtype, pageseq";
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