/*
 * Created on 07-May-2006
 */
package uk.org.ponder.darwin.rsf;

import uk.org.ponder.darwin.rsf.components.AdvancedSearchProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

/** The parameters for the search FORM itself, not for search results */
public class AdvancedSearchParams extends SimpleViewParameters {
  public String getParseSpec() {
    return super.getParseSpec() + ", manuscripts, published";
  }
  
  public AdvancedSearchParams() {
    super(AdvancedSearchProducer.VIEWID);
  }
  
  public boolean manuscripts;
  public boolean published;
}
