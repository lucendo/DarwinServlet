/*
 * Created on 07-May-2006
 */
package uk.org.ponder.darwin.rsf.producers;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import uk.org.ponder.darwin.rsf.params.AdvancedSearchParams;
import uk.org.ponder.darwin.rsf.params.SearchResultsParams;
import uk.org.ponder.darwin.search.ItemFieldTables;
import uk.org.ponder.rsf.components.UIBoundBoolean;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

public class AdvancedSearchProducer implements ViewComponentProducer, ViewParamsReporter {
  public static final String VIEWID = "advanced-search";
  private Map subtablemap;
  
  public String getViewID() {
    return VIEWID;
  }

  public void setItemFieldTables(ItemFieldTables fieldtables) {
    this.subtablemap = fieldtables.getTableMap();
  }
  
  public void fillComponents(UIContainer tofill, ViewParameters viewparamso, 
      ComponentChecker checker) {
    AdvancedSearchParams viewparams = (AdvancedSearchParams) viewparamso;
    UIForm searchform = UIForm.make(tofill, "search-form", new SearchResultsParams());
    
    UIBoundBoolean manuscript = UIBoundBoolean.make(searchform, "manuscript", 
        viewparams.manuscript);
    manuscript.fossilize = false;
    UIBoundBoolean published = UIBoundBoolean.make(searchform, "published", 
        viewparams.published);
    published.fossilize = false;
    
    Collection languagec = ((TreeMap)subtablemap.get("Language")).values();
    String[] languages = (String[]) languagec.toArray(new String[languagec.size()]);
    
    UISelect language = UISelect.make(searchform, "language", languages, languages, null);
    
    Collection doctypec = ((TreeMap)subtablemap.get("PartOfDocument")).values();
    String[] doctypes = (String[]) doctypec.toArray(new String[doctypec.size()]);
    
    UISelect doctype = UISelect.make(searchform, "documenttype", doctypes, doctypes, null);
    
  }

  public ViewParameters getViewParameters() {
    return new AdvancedSearchParams();
  }

}
