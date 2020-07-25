package pl.mjurek.notepage.controller.note;

import pl.mjurek.notepage.exception.DataAccessException;
import pl.mjurek.notepage.exception.UpdateObjectException;
import pl.mjurek.notepage.model.Note;
import pl.mjurek.notepage.model.User;
import pl.mjurek.notepage.service.DateNoteService;
import pl.mjurek.notepage.service.fun.NewLineConverter;
import pl.mjurek.notepage.service.NoteService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;

@WebServlet("/wrench")
public class WrenchController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String destination = "/note_list";
        request.setCharacterEncoding("UTF-8");

        String description = request.getParameter("description").trim();
        description = NewLineConverter.convertNewLineCharToBR_Tag(description);
        String importantStatus = request.getParameter("importantState");
        String deadlineDate = request.getParameter("date");

        HttpSession session = request.getSession();

        long noteId = (long) session.getAttribute("note_id");
        long dateId = (long) session.getAttribute("date_id");

        NoteService noteService = new NoteService();
        DateNoteService dateNoteService = new DateNoteService();
        try {
            noteService.update(noteId, description, importantStatus);
            dateNoteService.update(dateId, deadlineDate);
        } catch (UpdateObjectException ex) {
            request.setAttribute("message", "Can't update note");

            request.setAttribute("description", description);
            request.setAttribute("fragment", "add");
            destination = "/WEB-INF/index.jsp";
        } catch (ParseException ex) {
            request.setAttribute("message", "Invalid date");

            request.setAttribute("description", description);
            request.setAttribute("date", deadlineDate);
            request.setAttribute("fragment", "add");
            destination = "/WEB-INF/index.jsp";
        }
        request.getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String note_id = request.getParameter("note_id");

        Long noteId = null;
        try {
            noteId = Long.parseLong(note_id);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loggedUser");

        NoteService service = new NoteService();
        Note note;

//      Jeśli wyżej wystąpi błąd to bez sensu czytać z serwisu note o id 'null' - proponuję lepiej zabezpieczyć, wyjść z metody/zgłosić ładny błąd itp
        try {
            note = service.read(user,noteId);
        } catch (DataAccessException e) {
            request.setAttribute("errorMessage","Note not found");
            request.getRequestDispatcher("WEB-INF/error.jsp").forward(request,response);
            return;
        }

        String description = NewLineConverter.convertReadableNewLine(note.getDescription());
        note.setDescription(description);

//      request.getSession(false) - leci do zmiennej lokalnej w takim przypadku (masz więcej niż jedno wywołanie) - sugestia
//      Chociaż nie ma takiej potrzeby, druga linia i tak zastąpi wywołanie pierwszej (zobacz sobie javadoc metody)
        request.getSession(false).setAttribute("note_id", noteId);
        request.getSession(false).setAttribute("date_id", note.getDate().getId());

        request.setAttribute("note", note);
        request.setAttribute("modify", "modify");
        request.setAttribute("fragment", "add");
        request.getRequestDispatcher("WEB-INF/index.jsp").forward(request, response);
    }
}
