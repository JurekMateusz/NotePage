package pl.mjurek.notepage.controller.note;

import pl.mjurek.notepage.exception.DeleteObjectException;
import pl.mjurek.notepage.exception.UpdateObjectException;
import pl.mjurek.notepage.model.states.NotesControllerOptions;
import pl.mjurek.notepage.service.NoteService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/button_control")
public class ButtonsController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String note_id = request.getParameter("note_id");
        String action = request.getParameter("action");

        Long noteId;
        NotesControllerOptions actionStatus;
        try {
            noteId = Long.parseLong(note_id);
            actionStatus = NotesControllerOptions.valueOf(action);
        } catch (IllegalArgumentException ex) {
            response.sendRedirect(request.getContextPath() + "/note_list");
            return;
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
        } catch (UpdateObjectException ex) {
            request.setAttribute("errorMessage", "Can't update note");
        } catch (DeleteObjectException e) {
            request.setAttribute("errorMessage", "Can't delete note");
        }

        request.getRequestDispatcher("/note_list").forward(request, response);
    }
}
