package pl.mjurek.notepage.controller;

import pl.mjurek.notepage.exception.DeleteObjectException;
import pl.mjurek.notepage.exception.UpdateObjectException;
import pl.mjurek.notepage.model.Note;
import pl.mjurek.notepage.model.NotesControllerOptions;
import pl.mjurek.notepage.model.User;
import pl.mjurek.notepage.service.NoteService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@WebServlet("/notes_control")
public class NoteController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String note_id = request.getParameter("note_id");
        String action = request.getParameter("action");

        Long noteId = null;
        NotesControllerOptions actionStatus = null;
        try {
            noteId = Long.parseLong(note_id);
            actionStatus = NotesControllerOptions.valueOf(action);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        NoteService service = new NoteService();
        try {
            switch (actionStatus) {
                case TODO:
                case DONE:
                    service.update(noteId, actionStatus);
                    break;
                case DELETE:
                    service.deleteNote(noteId);
            }
        } catch (DeleteObjectException | UpdateObjectException ex) {
            request.getRequestDispatcher("WEB-INF/error.jsp").forward(request, response);
            return;
        }

        String destination;
        Cookie[] cookies = request.getCookies();
        if (cookies.length < 3) {//TODO temporary
            destination = "/default_note_list";
        } else {
            destination = "/param_note_list";
        }
        request.getRequestDispatcher(destination).forward(request, response);
    }

    private String jspFormat(Timestamp time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String result = dateFormat.format(time);
        return result;
    }
}
