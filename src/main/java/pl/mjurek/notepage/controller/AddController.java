package pl.mjurek.notepage.controller;

import pl.mjurek.notepage.exception.AddObjectException;
import pl.mjurek.notepage.model.Note;
import pl.mjurek.notepage.model.User;
import pl.mjurek.notepage.service.NoteService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

@WebServlet("/add")
public class AddController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String description = request.getParameter("inputDescription").trim();
        String importantState = request.getParameter("importantState");
        String deadlineDate = request.getParameter("inputDate");

        User authenticatedUser = (User) request.getSession().getAttribute("loggedUser");

        Note note = null;
        NoteService noteService = new NoteService();
        try {
            note = noteService.addNote(authenticatedUser, description, importantState, deadlineDate);
        } catch (AddObjectException ex) {
            request.setAttribute("message", "Can't add note");

            request.setAttribute("description", description);
            request.setAttribute("fragment", "add");
            request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
            return;
        } catch (ParseException ex) {
            request.setAttribute("message", "Invalid date");

            request.setAttribute("description", description);
            request.setAttribute("fragment", "add");
            request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
            return;
        }

        request.getRequestDispatcher("/default_note_list").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("fragment", "add");
        request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
    }
}
