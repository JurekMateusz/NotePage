package pl.mjurek.notepage.controller;

import pl.mjurek.notepage.exception.CantAddObjectException;
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

        String description = request.getParameter("inputDescription");
        String importantState = request.getParameter("importantState");
        String deadlineDate = request.getParameter("inputDate");

        User authenticatedUser = (User) request.getSession().getAttribute("loggedUser");

        NoteService noteService = new NoteService();
        Note note = null;
        try {
            note = noteService.addNote(authenticatedUser, description, importantState, deadlineDate);
        } catch (CantAddObjectException ex) {
            request.setAttribute("message", "Can't add note");

            request.setAttribute("note", note);
            request.setAttribute("fragment", "add");
            request.getRequestDispatcher(request.getContextPath() + "/WEB-INF/index.jsp").forward(request, response);
        } catch (ParseException ex) {
            request.setAttribute("message", "Invalid date");

            request.setAttribute("note", note);
            request.setAttribute("fragment", "add");
            request.getRequestDispatcher(request.getContextPath() + "/WEB-INF/index.jsp").forward(request, response);
        }
     //   request.setAttribute("fragment","notes");
        request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("fragment", "add");
        request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
    }
}
