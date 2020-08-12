package pl.mjurek.notepage.controller.note.view;

import pl.mjurek.notepage.model.note.Note;
import pl.mjurek.notepage.service.note.SortOptions;
import pl.mjurek.notepage.model.note.StatusNote;
import pl.mjurek.notepage.model.user.User;
import pl.mjurek.notepage.service.note.NoteService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@WebServlet("/note_list")
public class NoteController extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession(false);

    User loggedUser = (User) session.getAttribute("loggedUser");
    long id = loggedUser.getId();
    String sessionSearchByParameter = (String) session.getAttribute("search_by");

    String requestSearchAll =
        request.getParameter("search_by"); // return not null only for request for all notes
    List<Note> notes;

    if (hasUserSetViewPreferences(requestSearchAll, sessionSearchByParameter)) {
      notes = getAllNotesWithStatusTODO(id);
    } else if (thisRequestForSearchAll(requestSearchAll)) {
      notes = getAllUserNotes(id);
      session.setAttribute("search_by", "all");
    } else if (searchByParameter(sessionSearchByParameter)) {
      notes = getNotesByParametersInSession(id, session);
    } else {
      notes = getAllUserNotes(id);
    }

    request.setAttribute("notesList", notes);
    request.setAttribute("fragment", "notes");
    request.getRequestDispatcher("WEB-INF/index.jsp").forward(request, response);
  }

  private boolean hasUserSetViewPreferences(String s1, String s2) {
    return s1 == null && s2 == null;
  }

  private List<Note> getAllNotesWithStatusTODO(long id) {
    NoteService service = new NoteService();
    return service.getAll(id, StatusNote.TODO);
  }

  private boolean thisRequestForSearchAll(String parameter) {
    return Objects.equals(parameter, "all");
  }

  private List<Note> getAllUserNotes(long id) {
    NoteService service = new NoteService();
    return service.getAll(id);
  }

  private boolean searchByParameter(String parameter) {
    return Objects.equals(parameter, "parameter");
  }

  private List<Note> getNotesByParametersInSession(long id, HttpSession session) {
    NoteService service = new NoteService();
    String typeNotes = (String) session.getAttribute("type");
    String sortBy = (String) session.getAttribute("sort_by");
    String order = (String) session.getAttribute("order");

    SortOptions sortByEnum = SortOptions.valueOf(sortBy);

    return service.getAll(id, typeNotes, sortByEnum, order);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doPost(request, response);
  }
}
