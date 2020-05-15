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
                case WRENCH:
//                    request.setAttribute("fragment", "add");
//                    request.setAttribute("modify", "modify");
//                    Note note = service.read();
//                    request.setAttribute("description", note.getDescription());
//                    request.setAttribute("date", jspFormat(note.getDate().getDateDeadlineNote()));
//                    request.getRequestDispatcher("WEB-INF/index.js").forward(request, response);
//                    return;
                    //TODO WRENCH Controller
                case DELETE:
                    service.deleteNote(noteId);
            }
        } catch (Exception ex) {
            request.getRequestDispatcher("WEB-INF/error.jsp").forward(request, response);
            return;
        }
        String send = "/param_note_list";
        Cookie[] cookies = request.getCookies();
        if (cookies.length < 3) {//TODO temporary
            send = "/default_note_list";
        }
//        request.getRequestDispatcher(send).forward(request, response);
        response.sendRedirect(request.getContextPath()+send);
    }

    private String jspFormat(Timestamp time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String result = dateFormat.format(time);
        return result;
    }
}
