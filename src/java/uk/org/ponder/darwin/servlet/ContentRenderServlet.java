/*
 * Created on 14-Dec-2005
 */
package uk.org.ponder.darwin.servlet;

import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.org.ponder.darwin.rsf.ContentRenderParams;

/**
 * @author Antranig Basman (amb26@ponder.org.uk)
 * 
 */
public class ContentRenderServlet extends HttpServlet {
  public void doGet(HttpServletRequest request, HttpServletResponse response) {
    Map params = request.getParameterMap();
    ContentRenderParams renderparams = new ContentRenderParams();
    renderparams.fromRequest("", params);
    response.
  }
}
