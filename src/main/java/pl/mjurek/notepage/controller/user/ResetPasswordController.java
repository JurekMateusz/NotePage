package pl.mjurek.notepage.controller.user;

import pl.mjurek.notepage.exception.UpdateObjectException;
import pl.mjurek.notepage.model.User;
import pl.mjurek.notepage.service.AccountActionService;
import pl.mjurek.notepage.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/reset_password")
public class ResetPasswordController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String mail = request.getParameter("mail").trim();
        String message = "Check email";

        UserService service = new UserService();
        User user = service.getUserByEmail(mail);
        if (user != null) {
            AccountActionService actionService = new AccountActionService();
            String newPassword = actionService.getNewPassword();
            actionService.sendEmail(user.getEmail(), newPassword);

            newPassword = AccountActionService.encodePassword(newPassword);
            user.setPassword(newPassword);
            try {
                service.update(user);
            } catch (UpdateObjectException e) {
                e.printStackTrace();
            }

        } else {
            message = "Email don't exist in database";
        }
        request.setAttribute("errorMessage", message);
        request.getRequestDispatcher("WEB-INF/index.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("fragment", "resetPassword");
        request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
    }
}
