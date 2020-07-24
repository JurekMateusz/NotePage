package pl.mjurek.notepage.controller.note;

import pl.mjurek.notepage.exception.DataAccessException;
import pl.mjurek.notepage.exception.DeleteObjectException;
import pl.mjurek.notepage.exception.UpdateObjectException;
import pl.mjurek.notepage.model.User;
import pl.mjurek.notepage.model.states.NotesControllerOptions;
import pl.mjurek.notepage.service.NoteService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/button_control")
public class ButtonsController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String note_id = request.getParameter("note_id");
        String action = request.getParameter("action");

        long noteId;
        NotesControllerOptions actionStatus;
        try {
            noteId = Long.parseLong(note_id);
            actionStatus = NotesControllerOptions.valueOf(action);
        } catch (IllegalArgumentException ex) {
            response.sendRedirect(request.getContextPath() + "/note_list");
            return;
        }
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loggedUser");
        NoteService service = new NoteService();
        try {
            switch (actionStatus) {
                case TODO:
                case DONE:
                    service.update(user,noteId, actionStatus);
                    break;
                case DELETE:
                    service.deleteNote(user,noteId);
            }
        } catch (UpdateObjectException | DeleteObjectException ex) {
            request.setAttribute("errorMessage", "Can't update note");
        } catch (DataAccessException e) {
            request.setAttribute("errorMessage", "Note not found");
            request.getRequestDispatcher("WEB-INF/error.jsp").forward(request, response);
            return;
        }

        request.getRequestDispatcher("/note_list").forward(request, response);
    }
}
