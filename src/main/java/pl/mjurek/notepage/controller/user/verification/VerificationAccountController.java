package pl.mjurek.notepage.controller.user.verification;

import pl.mjurek.notepage.exception.DeleteObjectException;
import pl.mjurek.notepage.exception.NotFoundVerificationKey;
import pl.mjurek.notepage.exception.UpdateObjectException;
import pl.mjurek.notepage.model.keyaction.KeyAction;
import pl.mjurek.notepage.service.key.KeyActionService;
import pl.mjurek.notepage.service.user.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/verification")
public class VerificationAccountController extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String key = request.getParameter("key");

    long userId;
    try {
      userId = getUserIdByKey(key);
    } catch (NotFoundVerificationKey e) {
      request.setAttribute("errorMessage", "Wrong link");
      request.getRequestDispatcher("WEB-INF/index.jsp").forward(request, response);
      return;
    }

    UserService userService = new UserService();
    KeyActionService service = new KeyActionService();

    try {
      userService.unblock(userId);
      service.delete(key);
    } catch (DeleteObjectException | UpdateObjectException e) {
      request.setAttribute("errorMessage", "Something went wrong with DB");
      request.getRequestDispatcher("WEB-INF/error.jsp").forward(request, response);
      return;
    }

    request.setAttribute("successMessage", "Verification successful");
    request.getRequestDispatcher("WEB-INF/index.jsp").forward(request, response);
  }

  private long getUserIdByKey(String key) throws NotFoundVerificationKey {
    KeyActionService service = new KeyActionService();
    var keyActionOpt = service.read(key);
    if (keyActionOpt.isEmpty()) {
      throw new NotFoundVerificationKey();
    }
    KeyAction keyAct = keyActionOpt.get();
    return keyAct.getUserId();
  }
}
