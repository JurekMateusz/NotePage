package pl.mjurek.notepage.controller;

import org.apache.commons.codec.digest.DigestUtils;
import pl.mjurek.notepage.exception.AddObjectException;
import pl.mjurek.notepage.model.User;
import pl.mjurek.notepage.service.UserService;

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
        String username = request.getParameter("inputUsername").trim();
        String email = request.getParameter("inputEmail").trim();
        String password = request.getParameter("inputPassword");
        String repeatPassword = request.getParameter("inputRepeatPassword");

        String sha1hexPassword = DigestUtils.sha1Hex(password);

        User user = User.builder()
                .name(username)
                .email(email)
                .password(sha1hexPassword)
                .build();

        if (isAnyParamNull(username, email, password, repeatPassword)) {
            String message = "Fill all form";
            setAttributeAndForward(message, user, request, response);
        }
        if (!password.equals(repeatPassword)) {
            String message = "Passwords not the same";
            setAttributeAndForward(message, user, request, response);
        }

        UserService userService = new UserService();

        if (userService.isNameExisting(username)) {
            String message = "User exist";
            setAttributeAndForward(message, user, request, response);
        }

        if (userService.isEmailExisting(email)) {
            String message = "Email exist";
            setAttributeAndForward(message, user, request, response);
        }


        User resultUser = null;
        try {
            resultUser = userService.addUser(user);
        } catch (AddObjectException e) {
            e.printStackTrace();
        }

        if (resultUser == null) {
            request.setAttribute("fragment","error");
            request.getRequestDispatcher(request.getContextPath()+"/WEB-INF/index.jsp").forward(request, response);
        }
      //  request.setAttribute("fragment","login");
        response.sendRedirect(request.getContextPath() + "/WEB-INF/index.jsp");
    }

    private boolean isAnyParamNull(String username, String email, String password, String repeatPassword) {
        return username == null || email == null || password == null || repeatPassword == null;
    }

    private void setAttributeAndForward(String message, User user, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("fragment","register");
        request.setAttribute("message", message);
        request.setAttribute("user", user);
        request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("fragment","register");
        request.getSession().invalidate();
        request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
    }
}
