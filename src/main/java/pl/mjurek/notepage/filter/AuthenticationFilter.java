package pl.mjurek.notepage.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/add", "/note", "/note_list", "/wrench",
        "/param_note_list", "/button_control", "/account", "/deleteAccount"})
public class AuthenticationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (isUserLogged(request)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.sendRedirect(request.getContextPath() + "/");
        }
    }

    private boolean isUserLogged(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        // return session != null ? session.getAttribute("loggedUser") != null : false;
        if (session != null) {
            return session.getAttribute("loggedUser") != null;
        }
        return false;
    }
}
