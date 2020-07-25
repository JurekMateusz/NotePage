package pl.mjurek.notepage.controller.user;

import pl.mjurek.notepage.exception.AddObjectException;
import pl.mjurek.notepage.model.User;
import pl.mjurek.notepage.service.EmailService;
import pl.mjurek.notepage.service.UserService;
import pl.mjurek.notepage.service.fun.Hash;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/register")
public class RegisterController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username").trim();
        String email = request.getParameter("email").trim();
        String password = request.getParameter("password");
        String repeatPassword = request.getParameter("confirm_password");

        String encodePassword = Hash.encodePassword(password);

        User user = User.builder()
                .name(username)
                .email(email)
                .password(password)
                .build();

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

        // Nie podawaj dokładnie co jest nie tak (lepiej nie zdradzać tak na wszelki wypadek osobom które chcą Ci popsuć apkę)
        // zgłoś błąd podczas rejestracji lub bardziej ogólnie: 'User name or login already taken.'
        if (userService.isNameExisting(username)) {
            request.setAttribute("errorMessage", "User exist");
            setAttributeAndForward(user, request, response);
            return;
        }

        if (userService.isEmailExisting(email)) {
            request.setAttribute("errorMessage", "Email exist");
            setAttributeAndForward(user, request, response);
            return;
        }
        User registerUser;
        try {
            user.setPassword(encodePassword);
            registerUser = userService.addUser(user);
        } catch (AddObjectException e) {
            request.setAttribute("errorMessage", "add user to DB fail");
            request.getRequestDispatcher(request.getContextPath() + "/WEB-INF/error.jsp").forward(request, response);
            return;
        }

        StringBuffer patch = request.getRequestURL();

        Thread thread = new Thread(() -> {
            EmailService action = new EmailService();
            try {
                action.makeActivateKeyAndSendEmail(registerUser, patch);
            } catch (AddObjectException e) {
                e.printStackTrace();
            }
        });
        thread.start();

        request.setAttribute("fragment", "");
        request.setAttribute("successMessage", "Verification link send to email");
        request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
    }

    private boolean isAnyParamNull(String username, String email, String password, String repeatPassword) {
        return username == null || email == null || password == null || repeatPassword == null;
    }

    private void setAttributeAndForward(User user, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("fragment", "register");
        request.setAttribute("user", user);
        request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("fragment", "register");
        request.getSession().invalidate();
        request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
    }
}
