package pl.mjurek.notepage.controller.note;

import pl.mjurek.notepage.model.Note;
import pl.mjurek.notepage.model.User;
import pl.mjurek.notepage.model.states.SortOptions;
import pl.mjurek.notepage.model.states.StatusNote;
import pl.mjurek.notepage.service.NoteService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
                String typeNotes = request.getParameter("type");
                String sortBy = request.getParameter("sort_by");
                String order = request.getParameter("order");

                SortOptions sortByEnum = SortOptions.valueOf(sortBy);

                notes = service.getAll(id, typeNotes, sortByEnum, order);
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
