package pl.mjurek.notepage.controller.note.add;

import pl.mjurek.notepage.exception.AddObjectException;
import pl.mjurek.notepage.model.note.Note;
import pl.mjurek.notepage.model.user.User;
import pl.mjurek.notepage.service.function.html.newline.NewLineConverter;
import pl.mjurek.notepage.service.note.NoteService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

@WebServlet("/add")
public class AddController extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String destination = "/note_list";

    request.setCharacterEncoding("UTF-8");
    User authenticatedUser = (User) request.getSession().getAttribute("loggedUser");

    String description = request.getParameter("description").trim();
    String importantState = request.getParameter("importantState");
    String deadlineDate = request.getParameter("date");

    Note note = Note.builder().description(description).build();

    description = NewLineConverter.convertNewLineCharToBR_Tag(description);
    NoteService noteService = new NoteService();
    try {
      noteService.addNote(authenticatedUser, description, importantState, deadlineDate);
    } catch (AddObjectException ex) {
      request.setAttribute("errorMessage", "Can't add note");
      request.setAttribute("note", note);
      request.setAttribute("fragment", "add");

      destination = "/WEB-INF/index.jsp";
    } catch (ParseException ex) {
      request.setAttribute("errorMessage", "Invalid date");
      request.setAttribute("note", note);
      request.setAttribute("fragment", "add");

      destination = "/WEB-INF/index.jsp";
    }

    request.getRequestDispatcher(destination).forward(request, response);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.setAttribute("fragment", "add");
    request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
  }
}
