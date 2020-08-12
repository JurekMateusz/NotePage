package pl.mjurek.notepage.controller.user.auth;

import pl.mjurek.notepage.model.user.User;
import pl.mjurek.notepage.service.user.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/login")
public class LoginController extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String username = request.getParameter("username").trim();
    String password = request.getParameter("password");
    String destination = "/note_list";

    User user = User.builder().name(username).password(password).build();

    UserService userService = new UserService();

    Optional<User> userOpt = userService.getUserByCredential(username, password);

    if (userOpt.isPresent()) {
      saveUserInSession(request, userOpt.get());
      request.setAttribute("fragment", "notes");
    } else {
      request.setAttribute("buffUser", user);
      request.setAttribute("fragment", ""); // default login page
      request.setAttribute("errorMessage", "Incorrect username or password");
      destination = "WEB-INF/index.jsp";
    }

    request.getRequestDispatcher(destination).forward(request, response);
  }

  private void saveUserInSession(HttpServletRequest request, User user) {
    HttpSession session = request.getSession(true);
    session.setMaxInactiveInterval(10 * 60);
    session.setAttribute("loggedUser", user);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.getRequestDispatcher("WEB-INF/index.jsp").forward(request, response);
  }
}
