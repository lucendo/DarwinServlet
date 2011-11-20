/*
 * Created on 24 Aug 2006
 */
package uk.org.ponder.darwin.rsf.producers;

import org.apache.lucene.document.Document;

import uk.org.ponder.darwin.item.ItemCollection;
import uk.org.ponder.darwin.lucene.DocHit;
import uk.org.ponder.darwin.lucene.IndexItemSearcher;
import uk.org.ponder.darwin.rsf.params.AdvancedSearchParams;
import uk.org.ponder.darwin.rsf.params.RecordParams;
import uk.org.ponder.darwin.search.DocTypeInterpreter;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UIVerbatim;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;
import uk.org.ponder.util.Logger;
import uk.org.ponder.xml.XMLUtil;

public class RecordProducer implements ViewComponentProducer,
    ViewParamsReporter {
  public static final String VIEWID = "record";
  private ItemCollection itemcollection;
  private IndexItemSearcher indexItemSearcher;
  private DocTypeInterpreter doctypeinterpreter;
  private LinkBlockProducer linkBlockProducer;

  public String getViewID() {
    return VIEWID;
  }

  public ViewParameters getViewParameters() {
    return new RecordParams();
  }

  public void setIndexItemSearcher(IndexItemSearcher indexItemSearcher) {
    this.indexItemSearcher = indexItemSearcher;
  }

  public void setDocTypeInterpreter(DocTypeInterpreter doctypeinterpreter) {
    this.doctypeinterpreter = doctypeinterpreter;
  }
  
  public void setLinkBlockProducer(LinkBlockProducer linkBlockProducer) {
    this.linkBlockProducer = linkBlockProducer;
  }
  
  public void setItemCollection(ItemCollection itemcollection) {
    this.itemcollection = itemcollection;
  }
  
  private static class FieldAdder {
    private UIContainer tofill;
    private Document hit;
    public FieldAdder(UIContainer tofill, Document hit) {
      this.tofill = tofill;
      this.hit = hit;
    }
    public void add(String fieldname, String field) {
      String fieldvalue = hit.get(field);
      if (fieldvalue != null) {
        UIBranchContainer row = UIBranchContainer.make(tofill, "field-row:");
        UIOutput.make(row, "field-name", fieldname);
        UIOutput.make(row, "field", fieldvalue);
      }
    }
  }
  
  public void fillComponents(UIContainer tofill, ViewParameters viewparams,
      ComponentChecker checker) {
    RecordParams recparams = (RecordParams) viewparams;
   
    UIOutput.make(tofill, "identifier", recparams.itemID);
    linkBlockProducer.makeLinkBlock(tofill, recparams.itemID);
       
    String prev = itemcollection.adjust(recparams.itemID, ItemCollection.PREV);
    if (prev != null) {
      UIInternalLink.make(tofill, "prev", new RecordParams(prev));
    }
    
    String next = itemcollection.adjust(recparams.itemID, ItemCollection.NEXT);
    if (next != null) {
      UIInternalLink.make(tofill, "next", new RecordParams(next));
    }
    
    UIBranchContainer row = UIBranchContainer.make(tofill, "field-row:");
    UIOutput.make(row, "field-name", "Identifier:");
    UIOutput.make(row, "field", recparams.itemID);
    
    UIInternalLink.make(tofill, "advanced-search", new AdvancedSearchParams());
    
    try {
      DocHit[] hits = indexItemSearcher.getItemHit(recparams.itemID);
      Document hit = hits[0].document;
      FieldAdder adder = new FieldAdder(tofill, hit);
      adder.add("Date:", "displaydate");
      String doctype = hit.get("documenttype");
      AdvancedSearchParams againparams = new AdvancedSearchParams();
      
      if (doctypeinterpreter.isConciseType(doctype)) {
        adder.add("Concise reference:", "reference");
        adder.add("Detailed reference:", "notes");
        UIOutput.make(tofill, "title", "The Freeman Bibliographical Database");
        againparams.published = true;
        UIInternalLink.make(tofill, "same-search", "Search Bibliography Again", againparams);
      }
      else {
        UIOutput.make(tofill, "title", "The Darwin Manuscript Catalogue");
        againparams.manuscript = true;
        UIInternalLink.make(tofill, "same-search", "Search Manuscripts Again", againparams);
        adder.add("Name:", "name");
        adder.add("Attributed title:", "attributedtitle");
        //adder.add("Source date:", "sourcedate");
        adder.add("Description:", "description");
        adder.add("Document type:", "documenttype");
        adder.add("Place created:", "place");
        adder.add("Cross reference:", "xref");
      }
    }
    catch (Exception e) {
      Logger.log.warn("Error performing search", e);
    }
  }

}
