package pl.mjurek.notepage.controller;

import pl.mjurek.notepage.exception.AddObjectException;
import pl.mjurek.notepage.exception.UpdateObjectException;
import pl.mjurek.notepage.model.ImportantState;
import pl.mjurek.notepage.model.Note;
import pl.mjurek.notepage.model.NotesControllerOptions;
import pl.mjurek.notepage.model.User;
import pl.mjurek.notepage.service.DateNoteService;
import pl.mjurek.notepage.service.NoteService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@WebServlet("/wrench")
public class WrenchController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String description = request.getParameter("inputDescription").trim();
        String importantStatus = request.getParameter("importantState");
        String deadlineDate = request.getParameter("inputDate");

        Long noteId = (Long) request.getSession().getAttribute("note_id");
        Long dateId = (Long) request.getSession().getAttribute("date_id");

        NoteService noteService = new NoteService();
        DateNoteService dateNoteService = new DateNoteService();
        try {
            noteService.update(noteId,description,importantStatus);
            dateNoteService.update(dateId,deadlineDate);
        } catch (UpdateObjectException ex) {
            request.setAttribute("message", "Can't update note");

            request.setAttribute("description", description);
            request.setAttribute("fragment", "add");
            request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
            return;
        } catch (ParseException ex) {
            request.setAttribute("message", "Invalid date");

            request.setAttribute("description", description);
            request.setAttribute("date", deadlineDate);
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

        String date = new SimpleDateFormat("MM/dd/yyyy").format(note.getDate().getDateDeadlineNote());


        request.getSession(false).setAttribute("note_id", noteId);
        request.getSession(false).setAttribute("date_id", note.getDate().getId());

        request.setAttribute("description", note.getDescription());
        request.setAttribute("date", date);
        request.setAttribute("modify", "modify");
        request.setAttribute("fragment", "add");
        request.getRequestDispatcher("WEB-INF/index.jsp").forward(request, response);
    }
}
