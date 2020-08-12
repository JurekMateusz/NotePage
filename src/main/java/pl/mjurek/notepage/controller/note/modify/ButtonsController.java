package pl.mjurek.notepage.controller.note.modify;

import pl.mjurek.notepage.exception.DataAccessException;
import pl.mjurek.notepage.exception.DeleteObjectException;
import pl.mjurek.notepage.exception.UpdateObjectException;
import pl.mjurek.notepage.model.user.User;
import pl.mjurek.notepage.service.note.NoteChangeParameter;
import pl.mjurek.notepage.service.note.NoteService;

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
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String note_id = req.getParameter("note_id");
    String action = req.getParameter("action");

    long noteId;
    NoteChangeParameter changeParameter;
    try {
      noteId = Long.parseLong(note_id);
      changeParameter = NoteChangeParameter.valueOf(action);
    } catch (IllegalArgumentException ex) {
      resp.sendRedirect(req.getContextPath() + "/note_list");
      return;
    }
    HttpSession session = req.getSession();
    User user = (User) session.getAttribute("loggedUser");

    switch (changeParameter) {
      case TODO:
      case DONE:
        updateStatusNote(req, resp, user, noteId, changeParameter);
        break;
      case WRENCH:
        redirectToWrenchNoteController(req, resp);
        break;
      case DELETE:
        deleteNote(req, resp, user, noteId);
        break;
    }
  }

  private void updateStatusNote(
      HttpServletRequest req,
      HttpServletResponse resp,
      User user,
      long noteId,
      NoteChangeParameter changeParameter)
      throws ServletException, IOException {
    NoteService service = new NoteService();
    try {
      service.update(user, noteId, changeParameter);
    } catch (UpdateObjectException | DataAccessException e) {
      req.setAttribute("errorMessage", "Can't update note");
      req.getRequestDispatcher("WEB-INF/error.jsp").forward(req, resp);
      return;
    }
    req.getRequestDispatcher("/note_list").forward(req, resp);
  }

  private void redirectToWrenchNoteController(HttpServletRequest req, HttpServletResponse resp)
      throws IOException, ServletException {
    req.getRequestDispatcher("/wrench").forward(req, resp);
  }

  private void deleteNote(HttpServletRequest req, HttpServletResponse resp, User user, long noteId)
      throws ServletException, IOException {
    NoteService service = new NoteService();
    try {
      service.deleteNote(user, noteId);
    } catch (DeleteObjectException | DataAccessException e) {
      req.setAttribute("errorMessage", "Can't delete note");
      req.getRequestDispatcher("WEB-INF/error.jsp").forward(req, resp);
      return;
    }
    req.getRequestDispatcher("/note_list").forward(req, resp);
  }
}
