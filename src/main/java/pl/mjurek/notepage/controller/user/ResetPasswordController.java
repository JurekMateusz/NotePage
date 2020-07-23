package pl.mjurek.notepage.controller.user;

import pl.mjurek.notepage.exception.AddObjectException;
import pl.mjurek.notepage.exception.DeleteObjectException;
import pl.mjurek.notepage.exception.UpdateObjectException;
import pl.mjurek.notepage.model.KeyAction;
import pl.mjurek.notepage.service.AccountActionService;
import pl.mjurek.notepage.service.KeyActionService;
import pl.mjurek.notepage.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@WebServlet("/reset_password")
public class ResetPasswordController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String key = req.getParameter("key");
        KeyActionService keyService = new KeyActionService();

        if (key == null) {
            req.getRequestDispatcher("WEB-INF/index.jsp").forward(req, resp);
            return;
        }

        var keyActionOpt = keyService.read(key);
        if (keyActionOpt.isEmpty()) {
            req.setAttribute("errorMessage", "Wrong reset key");
            req.getRequestDispatcher("WEB-INF/index.jsp").forward(req, resp);
            return;
        }
        KeyAction keyAct = keyActionOpt.get();
        long userId = keyAct.getUserId();
        String resetPasswordKey = AccountActionService.getKey();

        try {
            keyService.addKey(userId, resetPasswordKey);
            keyService.delete(key);
        } catch (DeleteObjectException | AddObjectException e) {
            req.setAttribute("errorMessage", "Something went wrong with DB");
            req.getRequestDispatcher("WEB-INF/index.jsp").forward(req, resp);
            return;
        }

        req.setAttribute("key", resetPasswordKey);
        req.setAttribute("fragment", "resetPasswordForm");
        req.getRequestDispatcher("WEB-INF/index.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String key = req.getParameter("key");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirm_password");

        if (key == null) {
            req.getRequestDispatcher("WEB-INF/index.jsp").forward(req, resp);
            return;
        }

        if (!Objects.equals(password, confirmPassword)) {
            req.setAttribute("key", key);
            req.setAttribute("errorMessage", "Passwords not equals");
            req.setAttribute("fragment", "resetPasswordForm");
            req.getRequestDispatcher("WEB-INF/index.jsp").forward(req, resp);
            return;
        }

        KeyActionService keyService = new KeyActionService();

        var keyActionOpt = keyService.read(key);
        if (keyActionOpt.isEmpty()) {
            req.setAttribute("errorMessage", "Wrong reset key");
            req.getRequestDispatcher("WEB-INF/index.jsp").forward(req, resp);
            return;
        }

        KeyAction keyAct = keyActionOpt.get();
        long userId = keyAct.getUserId();
        password = AccountActionService.encodePassword(password);

        UserService userService = new UserService();
        try {
            userService.updatePassword(userId, password);
        } catch (UpdateObjectException e) {
            req.setAttribute("errorMessage", "Can't update password");
            req.getRequestDispatcher("WEB-INF/index.jsp").forward(req, resp);
            return;
        }
        req.setAttribute("successMessage", "New password has been set");
        req.getRequestDispatcher("WEB-INF/index.jsp").forward(req, resp);
    }
}
