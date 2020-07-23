package pl.mjurek.notepage.controller.user;

import pl.mjurek.notepage.exception.AddObjectException;
import pl.mjurek.notepage.model.User;
import pl.mjurek.notepage.service.AccountActionService;
import pl.mjurek.notepage.service.KeyActionService;
import pl.mjurek.notepage.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/reset_password_email")
public class ResetPasswordEmailController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email").trim();

        UserService service = new UserService();
        KeyActionService keyActionService = new KeyActionService();
        StringBuffer patch = request.getRequestURL();

        var userOpt = service.getUserByEmail(email);

        if (userOpt.isEmpty()) {
            request.setAttribute("errorMessage", "Email don't exist in database");
        } else {
            AccountActionService actionService = new AccountActionService();
            User user = userOpt.get();
            String key = AccountActionService.getKey();
            long id = user.getId();

            try {
                keyActionService.addKey(id, key);
            } catch (AddObjectException e) {
                request.setAttribute("errorMessage", "Can't add new key to db");
                request.getRequestDispatcher("WEB-INF/index.jsp").forward(request, response);
                return;
            }
            actionService.sendEmailWithResetLink(email, patch, key);
            request.setAttribute("successMessage", "Check email");
        }


        request.getRequestDispatcher("WEB-INF/index.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("fragment", "resetPasswordEmail");
        request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
    }
}
