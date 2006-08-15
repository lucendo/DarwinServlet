/*
 * Created on 15-Aug-2006
 */
package uk.org.ponder.darwin.rsf;

public class TermColours {
  public static final String[] TERM_COLOURS = new String[] {
    //"#ffff66", // avoid yellow for yellow background 
    "#a0ffff", "#99ff99", "#ff9999", "#ff66ff", "#880000", "#00aa00", "#886800", "#004699", "#990099"
  };
  
  public static String getContrastColour(int i) {
    return i < 4? "black" : "white";
  }
}
