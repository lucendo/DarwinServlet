/*
 * Created on 25-Jan-2006
 */
package uk.org.ponder.darwin.rsf.beans;

import uk.org.ponder.darwin.parse.ItemCollectionManager;

public class ICMTrigger {
  public static final String PASSWORD = "MisterDarwin";
  private ItemCollectionManager icm;
  private String password;

  public void setItemCollectionManager(ItemCollectionManager icm) {
    this.icm = icm;
  }
  
  public void setPassword(String password) {
    this.password = password;
  }
  
  public void index() {
    if (!this.password.equals(PASSWORD)) {
      icm.errors.add("Incorrect password - indexing not started");
    }
    else {
      icm.index();
    }
  }
}
