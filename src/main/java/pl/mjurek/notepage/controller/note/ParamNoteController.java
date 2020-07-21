package pl.mjurek.notepage.controller.note;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/param_note_list")
public class ParamNoteController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        session.setAttribute("search_by", "param");

        String typeNotes = request.getParameter("type");
        String sortBy = request.getParameter("sort_by");
        String order = request.getParameter("order");

        session.setAttribute("type",typeNotes);
        session.setAttribute("sort_by",sortBy);
        session.setAttribute("order",order);

        request.getRequestDispatcher("note_list").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("fragment", "search");
        request.getRequestDispatcher("WEB-INF/index.jsp").forward(request, response);
    }
}
