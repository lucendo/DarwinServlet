/*
 * Created on 07-May-2006
 */
package uk.org.ponder.darwin.rsf.producers;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import uk.org.ponder.darwin.rsf.beans.UIChoices;
import uk.org.ponder.darwin.rsf.params.AdvancedSearchParams;
import uk.org.ponder.darwin.rsf.params.SearchResultsParams;
import uk.org.ponder.darwin.rsf.util.DarwinUtil;
import uk.org.ponder.darwin.search.DocTypeInterpreter;
import uk.org.ponder.darwin.search.ItemFieldTables;
import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UIBoundBoolean;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIInputMany;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;
import uk.org.ponder.stringutil.StringList;

public class AdvancedSearchProducer implements ViewComponentProducer,
    ViewParamsReporter {
  public static final String VIEWID = "advanced-search";
  private Map subtablemap;
  private DocTypeInterpreter doctypeinterpreter;
  private UIChoices uiChoices;
  private MessageLocator messageLocator;

  public String getViewID() {
    return VIEWID;
  }

  public void setItemFieldTables(ItemFieldTables fieldtables) {
    this.subtablemap = fieldtables.getTableMap();
  }

  public void setDocTypeInterpreter(DocTypeInterpreter doctypeinterpreter) {
    this.doctypeinterpreter = doctypeinterpreter;
  }
  
  public void setUiChoices(UIChoices uiChoices) {
    this.uiChoices = uiChoices;
  }

  public void setMessageLocator(MessageLocator messageLocator) {
    this.messageLocator = messageLocator;
  }
  
  public void fillComponents(UIContainer tofill, ViewParameters viewparamso,
      ComponentChecker checker) {
    DarwinUtil.addStandardComponents(tofill);
    UIMessage.make(tofill, "page-title", "search.page.title");
    
    AdvancedSearchParams viewparams = (AdvancedSearchParams) viewparamso;
    UIForm searchform = UIForm.make(tofill, "search-form",
        new SearchResultsParams());
    boolean advanced = !(viewparams.manuscript ^ viewparams.published);

    String titlekey = "default.title";
    String identifierexamplekey = "default.identifier.example";
    if (viewparams.manuscript) {
      titlekey = "manuscript.title";
      identifierexamplekey = "manuscript.identifier.example";
    }
    if (viewparams.published) {
      titlekey = "published.title";
      identifierexamplekey = "published.identifier.example";
    }

    UIMessage.make(tofill, "title", titlekey);
    UIMessage.make(tofill, "identifier-example", identifierexamplekey);
    UIInput.make(searchform, "name", "params.name", (viewparams.manuscript && !viewparams.published || viewparams.beaglelibrary)? 
        "" : messageLocator.getMessage("default.search.name"));
    
    UIMessage.make(tofill, "title-example", "title.example");
    UIMessage.make(tofill, "periodical-example", "periodical.example");

    if (advanced) {
      UIBranchContainer.make(searchform, "freetextbranch:");
      UIBranchContainer itembranch = UIBranchContainer.make(searchform,
          "itemtypebranch:");
      UIBoundBoolean manuscript = UIBoundBoolean.make(itembranch, "manuscript", "params.manuscript",
          viewparams.manuscript);
      manuscript.fossilize = false;
      UIBoundBoolean published = UIBoundBoolean.make(itembranch, "published", "params.published", 
          viewparams.published);
      published.fossilize = false;
    }
    else {
      if (viewparams.published) {
        UIBoundBoolean.make(searchform, "published");
      }
      if (viewparams.manuscript) {
        UIBoundBoolean.make(searchform, "manuscript");
      }
    }

    if ((viewparams.published || advanced)) {
      UIBranchContainer olangbranch = UIBranchContainer.make(searchform, "languagebranch:");
      if (uiChoices.isSearchLanguages()) {
        UIBranchContainer langbranch = UIBranchContainer.make(olangbranch, "innerlanguagebranch:");
        Collection languagec = ((TreeMap) subtablemap.get("Language")).values();
        String[] languages = (String[]) languagec.toArray(new String[languagec
            .size()]);
        Arrays.sort(languages);

        // UISelect.makeMultiple(langbranch, "language", languages, languages,
        // "params.language", new String[] {"English"});
        UISelect langsel = UISelect.make(langbranch, "language", languages,
            languages, null, false);
        langsel.selection = new UIInputMany();
        langsel.selection.willinput = false;
// Removed by troll edict 02/07/12
//        if (viewparams.manuscript || !viewparams.published) {
//          langsel.selection.updateValue(new String[] { "English" });
//        }
      }
    }
    if (viewparams.manuscript || advanced) {
      UIBranchContainer descriptionbranch = UIBranchContainer.make(searchform, "descriptionbranch:");
      UIMessage.make(descriptionbranch, "description-example", "description.example");
    }

    TreeMap doctypemap = (TreeMap) subtablemap.get("PartOfDocument");
    String[] doctypes = getDocTypes(doctypemap, viewparams.manuscript,
        viewparams.published);
    Arrays.sort(doctypes);
    String initvalue = viewparams.beaglelibrary ? (String)doctypemap.get(Integer.toString(DocTypeInterpreter.BEAGLE_LIBRARY)) : null;

    UISelect.make(searchform, "documenttype", doctypes, doctypes, initvalue, false);
  }

  private String[] getDocTypes(TreeMap doctypemap, boolean manuscript,
      boolean published) {
    StringList togo = new StringList();
    for (Iterator kit = doctypemap.keySet().iterator(); kit.hasNext();) {
      String key = (String) kit.next();
      String value = (String) doctypemap.get(key);
      boolean isconcise = doctypeinterpreter.isConciseType(value);
      if (published && isconcise || manuscript && !isconcise
          || !(manuscript ^ published)) {
        togo.add(value);
      }
    }
    return togo.toStringArray();
  }

  public ViewParameters getViewParameters() {
    return new AdvancedSearchParams();
  }

}
