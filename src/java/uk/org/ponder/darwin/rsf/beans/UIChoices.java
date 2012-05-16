/*
 * Created on 15 May 2012
 */
package uk.org.ponder.darwin.rsf.beans;

public class UIChoices {
  // Since this disagreeable *BEAN* is managed by Spring's parsing facilities,
  // we need to give it accessors in this stupid standard way
    private boolean searchLanguages;

    public boolean isSearchLanguages() {
      return searchLanguages;
    }

    public void setSearchLanguages(boolean searchLanguages) {
      this.searchLanguages = searchLanguages;
    }
}
