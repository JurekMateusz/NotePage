package pl.mjurek.notepage.controller;

import pl.mjurek.notepage.service.AccountActionService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/verification")
public class VerificationController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String destination = "WEB-INF/index.jsp";

        String key = request.getParameter("key");
        AccountActionService service = new AccountActionService();
        boolean success = service.verification(key);
        if (!success) {
            destination = "WEB-INF/error.jsp";
        }
        request.getRequestDispatcher(destination).forward(request, response);
    }
}
