/*
 * Created on Jan 18, 2006
 */
package uk.org.ponder.darwin.rsf;

import java.io.InputStream;
import java.util.HashMap;

import org.springframework.core.io.InputStreamSource;
import org.xmlpull.v1.XmlPullParser;

import uk.org.ponder.darwin.item.ContentInfo;
import uk.org.ponder.darwin.item.ItemCollection;
import uk.org.ponder.darwin.item.ItemDetails;
import uk.org.ponder.darwin.parse.Attributes;
import uk.org.ponder.darwin.parse.BaseParser;
import uk.org.ponder.darwin.parse.Constants;
import uk.org.ponder.darwin.parse.DocumentTag;
import uk.org.ponder.darwin.parse.ParseReceiver;
import uk.org.ponder.darwin.parse.URLMapper;
import uk.org.ponder.darwin.rsf.components.FramesetProducer;
import uk.org.ponder.htmlutil.HTMLConstants;
import uk.org.ponder.rsf.viewstate.ViewStateHandler;
import uk.org.ponder.streamutil.StreamCopyUtil;
import uk.org.ponder.streamutil.write.PrintOutputStream;
import uk.org.ponder.stringutil.CharWrap;
import uk.org.ponder.util.Logger;
import uk.org.ponder.util.UniversalRuntimeException;
import uk.org.ponder.xml.XMLUtil;
import uk.org.ponder.xml.XMLWriter;

/**
 * Receives character events from both an upstream CONTENT ContentParser, and
 * ALSO operates its own BaseParser to parser a Template. At the same time,
 * rewrites page links to include Javascript and anchors.
 * 
 * @author Antranig Basman (amb26@ponder.org.uk)
 */
// This class is written in inestimably poor style. I apologise.
// It is in this package because of dependence on ViewStateHandler to render links.
public class RenderingParseReceiver extends BaseParser implements ParseReceiver {

  private PrintOutputStream pos;
  private XMLWriter xmlw;
  private CharWrap buffer = new CharWrap();
  // The status of the CONTENT reader.
  boolean editable;
  // currenteditableclass is set ABOVE to reflect TEMPLATE reader, which we
  // are reading with our own tokens.
  private InputStreamSource templatesource;
  private URLMapper urlmapper;
  private String contentpath;
  private ItemCollection collection;
  private ViewStateHandler vsh;

  public void setOutputStream(PrintOutputStream pos) {
    this.pos = pos;
    xmlw = new XMLWriter(pos);
  }

  public void setTemplateSource(InputStreamSource templatesource) {
    this.templatesource = templatesource;
  }

  public void setURLMapper(URLMapper urlmapper) {
    this.urlmapper = urlmapper;
  }

  public void setItemCollection(ItemCollection collection) {
    this.collection = collection;
  }
  
  public void setViewStateHandler(ViewStateHandler vsh) {
    this.vsh = vsh;
  }
  
  int currentpage = -1;

  public void metObject(Object tagobj) {
    if (tagobj instanceof DocumentTag) {
      currentpage = ((DocumentTag) tagobj).firstpage;
    }
  }

  boolean inpagetag = false;

  public static String getURLAttr(String tagname) {
    for (int i = 0; i < HTMLConstants.tagtoURL.length; ++i) {
      String[] tags = HTMLConstants.tagtoURL[i];
      String tag = tags[0];
      for (int j = 1; j < tags.length; ++j) {
        if (tagname.equals(tags[j])) {
          return tag;
        }
      }
    }
    return null;
  }

  // called by the CONTENT reader when tag is received.
  public void protoTag(String tagname, String clazz, HashMap attrmap, boolean isempty) {
    flushBuffer(true);
    if (Attributes.PAGE_CLASS.equals(clazz)) {
      // I am without style
      pos.print("<p>");
      attrmap.put("onClick",
          "onPageClick(this.getAttribute('dar:pageseq')); return false;");
      String pageseq = (String) attrmap.get(Attributes.PAGESEQ_ATTR);
      if (pageseq == null) {
        pageseq = Integer.toString(currentpage);
        ++currentpage;
      }
      else {
        currentpage = Integer.parseInt(pageseq);
      }
      attrmap.put("name", // "pageseq-" +
          "" + pageseq);
      attrmap.put("href", "#");
      attrmap.put(Attributes.PAGESEQ_ATTR, "" + pageseq);
      tagname = "a";
      inpagetag = true;
    }
    String exptag = "<" + tagname + " ";
    // Deal with rewriting for embedded links and images - need to resynthesize top &c
    String urlattr = getURLAttr(exptag);
    if (urlattr != null) {
      String url = (String) attrmap.get(urlattr);
      if (url != null && !url.startsWith("http://") && !url.equals("#")) {
        if (urlattr.equals("href") && tagname.equals("a") && 
            !attrmap.containsKey("target")) {
          // It's an inter-book link
          String targetpath = urlmapper.relURLToAbsolute(url, contentpath);
          ContentInfo ci = collection.getContentInfo(targetpath);
          if (ci == null) {
            Logger.log.warn("Unable to find ContentInfo for target path " + targetpath + 
                " arising from relative link URL " + url + " for content path " + contentpath);
          }
          else {
            NavParams frameparams = new NavParams();
            frameparams.viewID = FramesetProducer.VIEWID;
            frameparams.itemID = ci.itemID;
            frameparams.pageseq = ci.firstpage;
            ItemDetails id = collection.getItem(ci.itemID);
            if (id.hasimage) {
              if (id.hastext) {
                frameparams.viewtype = NavParams.SIDE_VIEW;
              }
              else {
                frameparams.viewtype = NavParams.IMAGE_VIEW;
              }
            }
            else frameparams.viewtype = NavParams.TEXT_VIEW;
            
            String globalurl = vsh.getFullURL(frameparams);
            attrmap.put(urlattr, globalurl);
            attrmap.put("target", "_top");
          }
        }
        else {
          // It's a leaf link - an image (either popup or inline)
          String globalurl = urlmapper.relURLToExternal(url, contentpath);
          attrmap.put(urlattr, globalurl);
        }
      }
    }
    pos.print("<" + tagname + " ");
    XMLUtil.dumpAttributes(attrmap, xmlw);

    pos.print(isempty? "/>" :">");
  }

  // text received from CONTENT.
  public void text(XmlPullParser parser, int token, CharWrap text) {
    // Replace the end of a pageno tag with </a>, skipping </p>
    if (token == XmlPullParser.END_TAG && inpagetag) {
      if (text.size() > 0) {
        buffer.append("</a>");
      }
      inpagetag = false;
    }
    if ((templatesource == null || editable)
        && token != XmlPullParser.START_TAG) {
      buffer.append(text);
    }
  }

  // status change from CONTENT
  public void beginEditable(String editclass) {
    editable = true;
  }

  // status change from CONTENT
  public void endEditable(String editclass) {
    if (templatesource != null) {
      if (editclass.equals(Constants.TITLE)) {
        scanTemplate(Constants.BODY);
      }
      else {
        scanTemplate(null);
      }
    }
  }

  // skim along the TEMPLATE until we hit an editable section, then since we
  // stop scanning, the CONTENT will continue to be delivered from the outside.
  private void scanTemplate(String required) {
    while (true) {
      try {
        int token = parser.nextToken();
        if (token == XmlPullParser.COMMENT) {
          if (token == XmlPullParser.END_DOCUMENT)
            break;
          if (required != null && testComment()) {
            if (currenteditableclass.equals(required))
              return;
          }
          if (currenteditableclass == null) {
            CharWrap text = renderToken(token);
            buffer.append(text);
            flushBuffer(false);
          }

        }
      }
      catch (Throwable t) {
        throw UniversalRuntimeException.accumulate(t, "Error parsing file: ");
      }
    }
  }

  public void beginFile(String contentpath) {
    this.contentpath = contentpath;
    editable = false;
    currenteditableclass = null;
    currentpage = -1;
    inpagetag = false;
    // parser.setFeature(FEATURE_XML_ROUNDTRIP, true);
    if (templatesource != null) {
      try {
        InputStream xmlstream = templatesource.getInputStream();
        parser.setInput(xmlstream, null);
      }
      catch (Exception e) {
        throw UniversalRuntimeException.accumulate(e,
            "Error opening template file");
      }
      scanTemplate(Constants.TITLE);
    }
  }

  public void endFile() {
    flushBuffer(true);
  }

  private void flushBuffer(boolean unconditional) {
    // TODO: At this point we will highlight search hits, since we will
    // have ViewParameters as a dependence. Before this can happen, we need
    // to refactor ViewParameters so that the payload has no further RSF
    // dependence.
    if (unconditional || buffer.size > StreamCopyUtil.PROCESS_BUFFER_SIZE) {
      pos.write(buffer.storage, 0, buffer.size);
      buffer.clear();
    }
  }

  public void endTag(String tagname) {
  }

}
