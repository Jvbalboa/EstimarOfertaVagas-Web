package springSecurity;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.UrlUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JsfAccessDeniedHandler implements AccessDeniedHandler {private String loginPath;
private boolean contextRelative;

public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
    String redirectUrl = calculateRedirectUrl(request.getContextPath(), loginPath);
    redirectUrl = response.encodeRedirectURL(redirectUrl);

    //we should redirect using ajax response if the case warrants
    boolean ajaxRedirect = request.getHeader("faces-request") != null
            && request.getHeader("faces-request").toLowerCase().indexOf("ajax") > -1;

    if(ajaxRedirect) {
        //javax.faces.context.FacesContext ctxt = javax.faces.context.FacesContext.getCurrentInstance();
        //ctxt.getExternalContext().redirect(redirectUrl);

        String ajaxRedirectXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<partial-response><redirect url=\""+redirectUrl+"\"></redirect></partial-response>";
        response.setContentType("text/xml");
        response.getWriter().write(ajaxRedirectXml);
    } else {
        response.sendRedirect(redirectUrl);
    }
}

private String calculateRedirectUrl(String contextPath, String url) {
    if (!UrlUtils.isAbsoluteUrl(url)) {
        if (!contextRelative) {
            return url;
        } else {
            return contextPath + url;
        }
    }

    // Full URL, including http(s)://

    if (!contextRelative) {
        return url;
    }

    // Calculate the relative URL from the fully qualified URL, minus the scheme and base context.
    url = url.substring(url.indexOf("://") + 3); // strip off scheme
    url = url.substring(url.indexOf(contextPath) + contextPath.length());

    if (url.length() > 1 && url.charAt(0) == '/') {
        url = url.substring(1);
    }

    return url;
}

/**
 * If <tt>true</tt>, causes any redirection URLs to be calculated minus the protocol
 * and context path (defaults to <tt>false</tt>).
 */
public void setContextRelative(boolean useRelativeContext) {
    this.contextRelative = useRelativeContext;
}

public String getLoginPath() {
    return loginPath;
}

public void setLoginPath(String loginPath) {
    this.loginPath = loginPath;
}
}