package pl.mjurek.notepage.controller;

import pl.mjurek.notepage.model.Note;
import pl.mjurek.notepage.model.StatusNote;
import pl.mjurek.notepage.model.User;
import pl.mjurek.notepage.service.NoteService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/default_note_list")
public class DefaultNoteController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User loggedUser = (User) request.getSession().getAttribute("loggedUser");

        String allAttribute = request.getParameter("all");
        NoteService service = new NoteService();
        List<Note> notes;

        if (allAttribute != null || lastSearchAll(request.getCookies())) {
            notes = service.getAll(loggedUser.getId());
        } else {
            saveCookieLastSearchAllNotes(response);
            notes = service.getAll(loggedUser.getId(), StatusNote.TODO);
        }

        request.setAttribute("notesList", notes);
        request.setAttribute("fragment", "notes");
        request.getRequestDispatcher("WEB-INF/index.jsp").forward(request, response);
    }

    private boolean lastSearchAll(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("ALL_NOTES")) {
                return true;
            }
        }
        return false;
    }

    private void saveCookieLastSearchAllNotes(HttpServletResponse response) {
        Cookie cookie = new Cookie("ALL_NOTES", "ALL_NOTES");
        cookie.setMaxAge(-1);
        response.addCookie(cookie);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
