package pl.mjurek.notepage.controller;

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

@WebServlet("/deleteAccount")
public class DeleteAccountController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("loggedUser");
        String deleteText = request.getParameter("deleteText");
        String correctText = "NotePage/" + user.getName();
        if (deleteText.equals(correctText)){
            Thread thread = new Thread(() -> {
                UserService service = new UserService();
                service.delete(user);
            });
            thread.start();
        }else {
            doGet(request,response);
            return;
        }
        response.sendRedirect(request.getContextPath()+"/logout");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("fragment", "deleteAccount");
        request.getRequestDispatcher("WEB-INF/index.jsp").forward(request, response);
    }
}
