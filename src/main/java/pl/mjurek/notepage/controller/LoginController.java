package pl.mjurek.notepage.controller;

import org.apache.commons.codec.digest.DigestUtils;
import pl.mjurek.notepage.model.User;
import pl.mjurek.notepage.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("inputUsername");
        String password = request.getParameter("inputPassword");
        String sha1hexPassword = DigestUtils.sha1Hex(password);

        User user = User.builder()
                .name(username)
                .password(sha1hexPassword)
                .build();
        if (isCorrectAndExist(user)) {
            //TODO redict and show latest notes by defould.
            saveUserInSession(request, user);
            request.setAttribute("fragment", "notes");
        } else {
            request.setAttribute("user", user);
            request.setAttribute("fragment", "");
        }
        request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
    }

    private boolean isCorrectAndExist(User user) {
        UserService userService = new UserService();
        User takenUser = userService.getUserByUserName(user.getName());
        if (user.equals(takenUser)) {
            return true;
        }
        return false;
    }

    private void saveUserInSession(HttpServletRequest request, User user) {
        UserService userService = new UserService();
        String username = user.getName();
        User loggedUser = userService.getUserByUserName(username);
        HttpSession session = request.getSession(true);
        session.setMaxInactiveInterval(15 * 60);
        session.setAttribute("loggedUser", loggedUser);
    }

//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        request.getSession().invalidate();
//        request.setAttribute("fragment", "");
//        request.getRequestDispatcher("index.jsp").forward(request, response);
//    }
}
