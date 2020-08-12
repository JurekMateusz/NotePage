package pl.mjurek.notepage.controller.user.register;

import pl.mjurek.notepage.exception.AddObjectException;
import pl.mjurek.notepage.model.user.User;
import pl.mjurek.notepage.service.email.EmailService;
import pl.mjurek.notepage.service.email.map.RegisterEmailMapWithContent;
import pl.mjurek.notepage.service.executor.ThreadTask;
import pl.mjurek.notepage.service.function.key.Hash;
import pl.mjurek.notepage.service.key.KeyActionService;
import pl.mjurek.notepage.service.user.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/register")
public class RegisterController extends HttpServlet {

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.setCharacterEncoding("UTF-8");
    String username = request.getParameter("username").trim();
    String email = request.getParameter("email").trim();
    String password = request.getParameter("password");
    String repeatPassword = request.getParameter("confirm_password");

    User user = User.builder().name(username).email(email).password(password).build();

    if (isAnyParamNull(username, email, password, repeatPassword)) {
      request.setAttribute("errorMessage", "Fill all form");
      setAttributeAndForward(user, request, response);
      return;
    }
    if (!password.equals(repeatPassword)) {
      request.setAttribute("errorMessage", "Passwords not the same");
      setAttributeAndForward(user, request, response);
      return;
    }

    UserService userService = new UserService();

    if (userService.isNameOrEmailExisting(username, email)) {
      request.setAttribute("errorMessage", "User name or login already taken");
      setAttributeAndForward(user, request, response);
      return;
    }

    try {
      userService.addUser(user);
    } catch (AddObjectException e) {
      request.setAttribute("errorMessage", "add user to DB fail");
      request
          .getRequestDispatcher(request.getContextPath() + "/WEB-INF/error.jsp")
          .forward(request, response);
      return;
    }

    StringBuffer patch = request.getRequestURL();

    try {
      sendEmail(patch, user);
    } catch (AddObjectException e) {
      request.setAttribute("errorMessage", "add activate key to DB fail");
      request
          .getRequestDispatcher(request.getContextPath() + "/WEB-INF/error.jsp")
          .forward(request, response);
      return;
    }

    request.setAttribute("fragment", "");
    request.setAttribute("successMessage", "Verification link send to email");
    request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
  }

  private boolean isAnyParamNull(
      String username, String email, String password, String repeatPassword) {
    return username == null || email == null || password == null || repeatPassword == null;
  }

  private void setAttributeAndForward(
      User user, HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.setAttribute("fragment", "register");
    request.setAttribute("user", user);
    request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
  }

  private void sendEmail(StringBuffer patch, User user) throws AddObjectException {
    String activateKey = Hash.getKey();
    KeyActionService service = new KeyActionService();
    service.addKey(user.getId(), activateKey);
    Map<String, String> emailMap =
        RegisterEmailMapWithContent.getMap(user.getEmail(), patch, activateKey);
    ThreadTask.execute(new EmailService(emailMap));
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.setAttribute("fragment", "register");
    request.getSession().invalidate();
    request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
  }
}
