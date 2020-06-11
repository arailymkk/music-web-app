package backend.web.controller;

import javax.servlet.*;
import javax.servlet.Filter;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = {"/*"})
public class LocaleFilter implements Filter {
    //public void destroy() {}

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        if (req.getParameter("locale") != null) {
            req.getSession().setAttribute("lang", req.getParameter("locale"));
        }

        chain.doFilter(request, response);
    }

    public void init(FilterConfig config) throws ServletException {}

    @Override
    public void destroy() {

    }
}
