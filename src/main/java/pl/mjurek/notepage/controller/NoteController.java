package pl.mjurek.notepage.controller;

import pl.mjurek.notepage.model.Note;
import pl.mjurek.notepage.model.StatusNote;
import pl.mjurek.notepage.model.User;
import pl.mjurek.notepage.service.NoteService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/note_list")
public class NoteController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        User loggedUser = (User) session.getAttribute("loggedUser");
        long id = loggedUser.getId();
        String searchByFromSession = (String) session.getAttribute("search_by");

        String searchByFromRequest = request.getParameter("search_by");//work only for All Notes
        NoteService service = new NoteService();
        List<Note> notes;


        if (searchByFromRequest != null) {
            notes = service.getAll(id);
            session.setAttribute("search_by", "all");
        } else if (searchByFromSession != null) {
            if (searchByFromSession.equals("param")) {
                String typeNotes = (String) session.getAttribute("type");
                String columnSearch = (String) session.getAttribute("sort_by_column");
                String order = (String) session.getAttribute("order");

                notes = service.getAll(id, typeNotes, columnSearch, order);
            } else {
                notes = service.getAll(id);
            }
        } else {
            notes = service.getAll(id, StatusNote.TODO);
        }


        request.setAttribute("notesList", notes);
        request.setAttribute("fragment", "notes");
        request.getRequestDispatcher("WEB-INF/index.jsp").forward(request, response);
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
