package pl.mjurek.notepage.controller.user;

import pl.mjurek.notepage.model.User;
import pl.mjurek.notepage.service.AccountActionService;
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
        String username = request.getParameter("username").trim();
        String password = request.getParameter("password");
        String destination = "/note_list";

        User user = User.builder()
                .name(username)
                .password(password)
                .build();

        UserService userService = new UserService();

        String encodedPassword = AccountActionService.encodePassword(password);

        User takenUser = userService.getUserByUserName(user.getName());
        boolean passwordsCorrect = false;
        boolean verificated = false;
        if (takenUser != null) {
            passwordsCorrect = encodedPassword.equals(takenUser.getPassword());
            verificated = takenUser.getVerification().equals("YES");
        }

        if (passwordsCorrect) {
            if (verificated) {
                saveUserInSession(request, user);
                request.setAttribute("fragment", "notes");
            }else {
                request.setAttribute("buffUser", user);
                request.setAttribute("fragment", "");//default login page
                request.setAttribute("errorMessage", "Check your email and click to link");
                destination = "WEB-INF/index.jsp";
            }
        } else {
            request.setAttribute("buffUser", user);
            request.setAttribute("fragment", "");//default login page
            request.setAttribute("errorMessage", "Incorrect username or password");
            destination = "WEB-INF/index.jsp";
        }

        request.getRequestDispatcher(destination).forward(request, response);
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
