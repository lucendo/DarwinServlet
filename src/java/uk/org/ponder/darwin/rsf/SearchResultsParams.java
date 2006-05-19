/*
 * Created on 07-May-2006
 */
package uk.org.ponder.darwin.rsf;

import uk.org.ponder.darwin.search.SearchParams;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class SearchResultsParams extends SimpleViewParameters {
  public Integer pageno;
  public Integer pagesize = new Integer(20);
  public static final String[] pagesizes = new String[] {"10", "20", "50", "100"};
  
  public SearchParams params = new SearchParams();
  
  public String getParseSpec() {
    return super.getParseSpec() + ", pageno, pagesize, params.*:";
  }
}
