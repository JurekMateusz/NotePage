package pl.mjurek.notepage.controller;

import pl.mjurek.notepage.exception.AddObjectException;
import pl.mjurek.notepage.exception.UpdateObjectException;
import pl.mjurek.notepage.model.ImportantState;
import pl.mjurek.notepage.model.Note;
import pl.mjurek.notepage.model.NotesControllerOptions;
import pl.mjurek.notepage.model.User;
import pl.mjurek.notepage.service.NoteService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

@WebServlet("/wrench")
public class WrenchController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String description = request.getParameter("inputDescription").trim();
        String importantState = request.getParameter("importantState");
        String deadlineDate = request.getParameter("inputDate");

        User authenticatedUser = (User) request.getSession().getAttribute("loggedUser");
        String id = request.getParameter("note_id");

        Long dateId = Long.parseLong(id);

        Note note = Note.builder()
                .id(dateId)
                .description(description)
                .importantState(ImportantState.valueOf(importantState))
                .build();
        NoteService noteService = new NoteService();
        try {
            noteService.update(note,deadlineDate);
        } catch (UpdateObjectException ex) {
            request.setAttribute("message", "Can't update note");

            request.setAttribute("description", description);
            request.setAttribute("fragment", "add");
            request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
            return;
        } catch (ParseException ex) {
            request.setAttribute("message", "Invalid date");

            request.setAttribute("description", description);
            request.setAttribute("dateString", deadlineDate);
            request.setAttribute("fragment", "add");
            request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
            return;
        }
        request.getRequestDispatcher("/default_note_list").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String note_id = request.getParameter("note_id");

        Long noteId = null;
        try {
            noteId = Long.parseLong(note_id);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }

        NoteService service = new NoteService();
        Note note = service.read(noteId);

        request.setAttribute("description", note.getDescription());
        request.setAttribute("dateString", note.getDate().toString());
        request.setAttribute("modify", "modify");
        request.setAttribute("fragment", "add");
        request.setAttribute("note_id", note_id);
        request.getRequestDispatcher("WEB-INF/index.jsp").forward(request, response);
    }
}
