/*
 * Created on 07-May-2006
 */
package uk.org.ponder.darwin.rsf.params;

import uk.org.ponder.darwin.rsf.producers.AdvancedSearchProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

/** The parameters for the search FORM itself, not for search results */
public class AdvancedSearchParams extends SimpleViewParameters {
  public String getParseSpec() {
    return super.getParseSpec() + ", manuscript, published, beaglelibrary";
  }
  
  public AdvancedSearchParams() {
    super(AdvancedSearchProducer.VIEWID);
  }
  
  public boolean manuscript;
  public boolean published;
  public boolean beaglelibrary;
}
