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
public class NavParams extends SimpleViewParameters {
  private static StringList attrnames = 
    StringList.fromString("flowtoken, endflow, errortoken, errorredirect, itemID, viewtype");
  
  public StringList getAttributeFields() {
    return attrnames;
   }  
  
  public static final String IMAGE_VIEW = "image";
  public static final String TEXT_VIEW = "text";
  public static final String SIDE_VIEW = "side";
  public String itemID;
  public String viewtype;

}
