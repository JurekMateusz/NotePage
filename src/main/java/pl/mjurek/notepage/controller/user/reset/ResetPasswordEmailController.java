package pl.mjurek.notepage.controller.user.reset;

import pl.mjurek.notepage.exception.AddObjectException;
import pl.mjurek.notepage.model.user.User;
import pl.mjurek.notepage.service.email.EmailService;
import pl.mjurek.notepage.service.email.map.ResetPasswordEmailMapWithContent;
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

@WebServlet("/reset_password_email")
public class ResetPasswordEmailController extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String email = request.getParameter("email").trim();

    UserService service = new UserService();
    StringBuffer path = request.getRequestURL();

    var userOpt = service.getUserByEmail(email);

    if (userOpt.isPresent()) {
      User user = userOpt.get();
      String key = Hash.getKey();
      long id = user.getId();

      try {
        insertKeyToDd(id, key);
      } catch (AddObjectException e) {
        request.setAttribute("errorMessage", "Can't add new key to db");
        request.getRequestDispatcher("WEB-INF/index.jsp").forward(request, response);
        return;
      }

      sendEmailWithResetPasswordLink(email, path, key);
    }
    request.setAttribute("successMessage", "Check email");
    request.getRequestDispatcher("WEB-INF/index.jsp").forward(request, response);
  }

  private void insertKeyToDd(long id, String key) throws AddObjectException {
    KeyActionService keyActionService = new KeyActionService();
    keyActionService.addKey(id, key);
  }

  private void sendEmailWithResetPasswordLink(String email, StringBuffer path, String key) {
    Map<String, String> map = ResetPasswordEmailMapWithContent.getMap(email, path, key);
    ThreadTask.execute(new EmailService(map));
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.setAttribute("fragment", "resetPasswordEmail");
    request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
  }
}
