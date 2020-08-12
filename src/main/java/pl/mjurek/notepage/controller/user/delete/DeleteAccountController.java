package pl.mjurek.notepage.controller.user.delete;

import pl.mjurek.notepage.model.user.User;
import pl.mjurek.notepage.service.executor.ThreadTask;
import pl.mjurek.notepage.service.user.delete.DeleteAccountService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

@WebServlet("/deleteAccount")
public class DeleteAccountController extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession(false);
    User user = (User) session.getAttribute("loggedUser");
    String deleteText = request.getParameter("deleteText");
    String correctText = "NotePage/" + user.getName();

    if (!Objects.equals(deleteText, correctText)) {
      doGet(request, response);
      return;
    }
    ThreadTask.execute(new DeleteAccountService(user));

    response.sendRedirect(request.getContextPath() + "/logout");
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.setAttribute("fragment", "deleteAccount");
    request.getRequestDispatcher("WEB-INF/index.jsp").forward(request, response);
  }
}
