/*
 * Created on 25-Jan-2006
 */
package uk.org.ponder.darwin.rsf.components;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;

import uk.org.ponder.darwin.item.ItemDetails;
import uk.org.ponder.darwin.parse.ItemCollectionManager;
import uk.org.ponder.darwin.rsf.NavParams;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UIOutputMultiline;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.sortutil.ObjArraySortFacade;
import uk.org.ponder.sortutil.Sort;

public class StatusProducer implements ViewComponentProducer {
  public static final String VIEWID = "status";
  private ItemCollectionManager manager;
  public String getViewID() {
   return VIEWID;
  }

  public void setItemCollectionManager(ItemCollectionManager manager) {
    this.manager = manager;
  }
  public void fillComponents(UIContainer tofill, ViewParameters origviewparams, ComponentChecker checker) {
    if (manager.getItemCollection() != null) {
      Collection items = manager.getItemCollection().getItems();
      UIBranchContainer present = UIBranchContainer.make(tofill, "index-present:");
      UIOutput.make(present, "items", ""+items.size());
      UIOutput.make(present, "pages", ""+manager.statistics.pages);
      UIOutput.make(present, "images", ""+manager.statistics.images);
      UIOutput.make(present, "contents", ""+manager.statistics.contents);
      UIOutput.make(present, "scanned", manager.statistics.time + "ms");
      UIOutput.make(present, "scan-date", new SimpleDateFormat("HH:mm:ss EEE, d MMM yyyy Z").format(manager.statistics.scandate));
      if (manager.statistics.errors.isEmpty()) {
        UIOutput.make(present, "scan-errors-none");
      }
      else {
        UIOutputMultiline.make(present, "scan-errors", null, manager.statistics.errors);
      }
      String[] idlist = new String[items.size()];
      int i = 0;
      for (Iterator itemit = items.iterator(); itemit.hasNext();) {
        idlist[i++] = ((ItemDetails)itemit.next()).ID;
      }
      Sort.quicksort(new ObjArraySortFacade(idlist, null));
      for (i = 0; i < idlist.length; ++ i) {
        ItemDetails item = manager.getItemCollection().getItem(idlist[i]);
        UIBranchContainer line = UIBranchContainer.make(tofill, "linktable:imagetext");
        UIOutput.make(line, "itemid", item.ID);
        NavParams params = new NavParams();
        params.itemID = item.ID;
        params.viewID = FramesetProducer.VIEWID;
        params.pageseq = 1;
        if (item.hasimage) {
          NavParams imagep = (NavParams) params.copyBase();
          imagep.viewtype = NavParams.IMAGE_VIEW;
          UIInternalLink.make(line, "imagelink", imagep);
        }
        if (item.hastext) {
          NavParams imagep = (NavParams) params.copyBase();
          imagep.viewtype = NavParams.TEXT_VIEW;
          UIInternalLink.make(line, "textlink", imagep);
        }
        if (item.hasimage && item.hastext) {
          NavParams imagep = (NavParams) params.copyBase();
          imagep.viewtype = NavParams.SIDE_VIEW;
          UIInternalLink.make(line, "sidelink", imagep);
        }
      }
    }
    else {
      UIBranchContainer.make(tofill, "index-none:");
    }
    UIOutput.make(tofill, manager.busy? "status-busy" : "status-idle", null);
    UIForm form = UIForm.make((UIBranchContainer) tofill);
    UIInput.make(form, "password", "#{icmtrigger.password}", "");
    UICommand.make(form, "rescan", "#{icmtrigger.index}");
  
  } 
}
