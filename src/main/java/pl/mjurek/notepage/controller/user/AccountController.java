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
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/account")
public class AccountController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String repeatPassword = req.getParameter("confirm_password");

        if (email == null || password == null || repeatPassword == null) {
            doGet(req, resp);
            return;
        }

        if (password.equals(repeatPassword)) {
            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("loggedUser");
            user.setEmail(email);
            String encodedPassword = AccountActionService.encodePassword(password);
            user.setPassword(encodedPassword);
            UserService service = new UserService();

            try {
                service.update(user);
            } catch (UpdateObjectException e) {
                e.printStackTrace();
            }
            resp.sendRedirect(req.getContextPath()+"/logout");
        } else {
            req.setAttribute("errorMessage", "Passwords not the same");
            req.setAttribute("fragment", "account");
            req.getRequestDispatcher("WEB-INF/index.jsp").forward(req, resp);
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("fragment", "account");
        request.getRequestDispatcher("WEB-INF/index.jsp").forward(request, response);
    }
}

