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
        String username = request.getParameter("inputUsername").trim();
        String password = request.getParameter("inputPassword");
        String destination = "/note_list";

        User buffUser = User.builder()
                .name(username)
                .password(password)
                .build();

        if (isCorrectAndExist(buffUser)) {
            saveUserInSession(request, buffUser);
            request.setAttribute("fragment", "notes");
        } else {
            request.setAttribute("buffUser", buffUser);
            request.setAttribute("fragment", "");//default login page
            request.setAttribute("errorMessage", "Incorrect username or password");

            destination = "WEB-INF/index.jsp";
        }

        request.getRequestDispatcher(destination).forward(request, response);
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
        session.setMaxInactiveInterval(10 * 60);
        session.setAttribute("loggedUser", loggedUser);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/index.jsp").forward(request, response);
    }
}
