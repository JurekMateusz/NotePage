package pl.mjurek.notepage.controller;

import pl.mjurek.notepage.exception.DeleteObjectException;
import pl.mjurek.notepage.model.NotesControllerOptions;
import pl.mjurek.notepage.service.NoteService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/notes_control")
public class NoteController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String note_id = request.getParameter("note_id");
        String action = request.getParameter("action");


//        HttpSession session = request.getSession(true);
//        session.setMaxInactiveInterval(10 * 60);
//        session.setAttribute("loggedUser", loggedUser);
//    getLastSearchAtribute;
//
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
                    //TODO send to ..
                    break;
                case DELETE:
                    service.deleteNote(noteId);
            }
        }catch (DeleteObjectException ex){
            //TODO
        }
    }
}
