/*
 * Created on 7 Oct 2006
 */
package uk.org.ponder.darwin.rsf.producers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.org.ponder.darwin.pages.PageCountDAO;
import uk.org.ponder.darwin.rsf.params.PageCountViewParams;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

public class PageCounterProducer implements ViewComponentProducer, ViewParamsReporter {
  public static final String VIEWID = "page-counter";
  private PageCountDAO pagecountDAO;

  public String getViewID() {
    return VIEWID;
  }

  public void setPageCountDAO(PageCountDAO pagecountDAO) {
    this.pagecountDAO = pagecountDAO;
  }
  
  public void fillComponents(UIContainer tofill, ViewParameters viewparams,
      ComponentChecker checker) {
    PageCountViewParams pcvp = (PageCountViewParams) viewparams;
    int count = pagecountDAO.registerAccess(pcvp.url);
    Date date = pagecountDAO.getStartDate();
    DateFormat format = SimpleDateFormat.getDateInstance(DateFormat.LONG);
    UIOutput.make(tofill, "count", count + (count == 1? " time":" times"));
    UIOutput.make(tofill, "startdate", format.format(date));
  }

  public ViewParameters getViewParameters() {
    return new PageCountViewParams();
  }

}
