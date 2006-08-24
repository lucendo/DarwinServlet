/*
 * Created on 24 Aug 2006
 */
package uk.org.ponder.darwin.rsf.producers;

import org.apache.lucene.document.Document;

import uk.org.ponder.darwin.item.ItemCollection;
import uk.org.ponder.darwin.item.ItemDetails;
import uk.org.ponder.darwin.lucene.DocHit;
import uk.org.ponder.darwin.lucene.IndexItemSearcher;
import uk.org.ponder.darwin.rsf.params.NavParams;
import uk.org.ponder.darwin.rsf.params.RecordParams;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;
import uk.org.ponder.util.Logger;

public class RecordProducer implements ViewComponentProducer,
    ViewParamsReporter {
  public static final String VIEWID = "record";
  private ItemCollection itemcollection;
  private IndexItemSearcher indexItemSearcher;

  public String getViewID() {
    return VIEWID;
  }

  public ViewParameters getViewParameters() {
    return new RecordParams();
  }


  public void setItemCollection(ItemCollection itemcollection) {
    this.itemcollection = itemcollection;
  }
  
  public void setIndexItemSearcher(IndexItemSearcher indexItemSearcher) {
    this.indexItemSearcher = indexItemSearcher;
  }
  
  public void fillComponents(UIContainer tofill, ViewParameters viewparams, 
      ComponentChecker checker) {
    RecordParams recparams = (RecordParams) viewparams;
    ItemDetails item = itemcollection.getItem(recparams.itemID);
    
    UIOutput.make(tofill, "identifier", recparams.itemID);
    
    
   
    if (item.hasimage && item.hastext) {
      NavParams sideparams = new NavParams();
      sideparams.viewtype = NavParams.SIDE_VIEW;
      sideparams.viewID = FramesetProducer.VIEWID;
      UIInternalLink.make(tofill, "switch-side", sideparams);
    }
    if (item.hasimage) {
      NavParams sideparams = new NavParams();
      sideparams.viewtype = NavParams.IMAGE_VIEW;
      sideparams.viewID = FramesetProducer.VIEWID;
      UIInternalLink.make(tofill, "switch-image", sideparams);
    }
    if (item.hastext) {
      NavParams sideparams = new NavParams();
      sideparams.viewtype = NavParams.TEXT_VIEW;
      sideparams.viewID = FramesetProducer.VIEWID;
      UIInternalLink.make(tofill, "switch-text", sideparams);
    }
    
    try {
      DocHit[] hits = indexItemSearcher.getItemHit(recparams.itemID);
      Document hit = hits[0].document;
      String concise = hit.get("reference");
      UIOutput.make(tofill, "concise", concise);
      String notes = hit.get("notes");
      UIOutput.make(tofill, "detailed", notes);
    }
    catch (Exception e) {
      Logger.log.warn("Error performing search", e);
    }
  }

}
