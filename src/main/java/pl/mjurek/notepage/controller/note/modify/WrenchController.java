package pl.mjurek.notepage.controller.note.modify;

import pl.mjurek.notepage.exception.DataAccessException;
import pl.mjurek.notepage.exception.UpdateObjectException;
import pl.mjurek.notepage.model.note.Note;
import pl.mjurek.notepage.model.user.User;
import pl.mjurek.notepage.service.date.DateNoteService;
import pl.mjurek.notepage.service.function.html.newline.NewLineConverter;
import pl.mjurek.notepage.service.note.NoteService;

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
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
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
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String note_id = request.getParameter("note_id");

    long noteId;
    try {
      noteId = Long.parseLong(note_id);
    } catch (IllegalArgumentException ex) {
      request.setAttribute("errorMessage", "Can't find note");
      request.getRequestDispatcher("/note_list").forward(request, response);
      return;
    }
    HttpSession session = request.getSession();
    User user = (User) session.getAttribute("loggedUser");

    NoteService service = new NoteService();
    Note note;

    try {
      note = service.read(user, noteId);
    } catch (DataAccessException e) {
      request.setAttribute("errorMessage", "Note not found");
      request.getRequestDispatcher("WEB-INF/error.jsp").forward(request, response);
      return;
    }

    String description = NewLineConverter.convertReadableNewLine(note.getDescription());
    note.setDescription(description);

    session.setAttribute("note_id", noteId);
    session.setAttribute("date_id", note.getDate().getId());

    request.setAttribute("note", note);
    request.setAttribute("modify", "modify");
    request.setAttribute("fragment", "add");
    request.getRequestDispatcher("WEB-INF/index.jsp").forward(request, response);
  }
}
