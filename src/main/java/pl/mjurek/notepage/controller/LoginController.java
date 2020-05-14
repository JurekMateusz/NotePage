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

        User buffUser = User.builder()
                .name(username)
                .password(password)
                .build();

        if (isCorrectAndExist(buffUser)) {
            saveUserInSession(request, buffUser);
            request.setAttribute("fragment", "notes");
        } else {
            request.setAttribute("buffUser", buffUser);
            request.setAttribute("message","Incorrect username or password");
            request.setAttribute("fragment", "");
        }
        request.getRequestDispatcher("/note").include(request,response);
        request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
    }

    private boolean isCorrectAndExist(User user) {
        UserService userService = new UserService();
        String sha1hexPassword = DigestUtils.sha1Hex(user.getPassword());

        User userWithCodedPass = User.builder()
                .name(user.getName())
                .password(sha1hexPassword)
                .build();

        User takenUser = userService.getUserByUserName(user.getName());
        if (userWithCodedPass.equals(takenUser)) {
            return true;
        }
        return false;
    }

    private void saveUserInSession(HttpServletRequest request, User user) {
        UserService userService = new UserService();
        String username = user.getName();
        User loggedUser = userService.getUserByUserName(username);
        HttpSession session = request.getSession(true);
        session.setMaxInactiveInterval(1 * 60);
        session.setAttribute("loggedUser", loggedUser);
    }
}
