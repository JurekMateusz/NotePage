package pl.mjurek.notepage.controller;

import pl.mjurek.notepage.model.Note;
import pl.mjurek.notepage.model.StatusNote;
import pl.mjurek.notepage.model.User;
import pl.mjurek.notepage.service.NoteService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/default_note_list")
public class DefaultNoteController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User loggedUser = (User) request.getSession().getAttribute("loggedUser");

        String allAttribute = request.getParameter("all");
        NoteService service = new NoteService();
        List<Note> notes;

        if (allAttribute == null) {
            notes = service.getAll(loggedUser.getId(), StatusNote.TODO);
        } else {
            notes = service.getAll(loggedUser.getId());
        }
//        notes.forEach(note -> {
//            String description = note.getDescription();
//            String converted = description.replace("\n","\r\n");
//            note.setDescription(converted);
//        });
        request.setAttribute("notesList", notes);
        request.setAttribute("fragment", "notes");
        request.getRequestDispatcher("WEB-INF/index.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
