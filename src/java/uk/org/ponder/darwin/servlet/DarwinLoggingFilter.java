/*
 * Created on 19 Oct 2006
 */
package uk.org.ponder.darwin.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class DarwinLoggingFilter implements Filter {

  private static org.apache.log4j.Logger accesslog = org.apache.log4j.Logger
      .getLogger("accesslog");

  public void destroy() {
  }

  public void init(FilterConfig filterConfig) {
  }

  private void logAccess(HttpServletRequest request) {
    String uri = request.getRequestURI();
    int slashpos = uri.lastIndexOf('/');
    if (slashpos == -1) slashpos = 0;
    int dotpos = uri.lastIndexOf('.');
    if (dotpos < slashpos || uri.substring(dotpos + 1).equals("html")) {
//      if (pathinfo.indexOf("page-counter") != -1) return;
      String ip = request.getRemoteAddr();
      StringBuffer urlb = request.getRequestURL();
      String query = request.getQueryString();
      if (query != null) {   
        urlb.append('?').append(query);
      }
      String url = urlb.toString();

      String referrer = request.getHeader("Referer");
      if (referrer == null)
        referrer = "";
      accesslog.info(request.getMethod() + " from " + ip + " " + url + " "
          + referrer);
    }

  }

  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain) throws IOException, ServletException {
    try {
      logAccess((HttpServletRequest) request);
    }
    catch (Exception e) {
    }
    chain.doFilter(request, response);
  }

}
