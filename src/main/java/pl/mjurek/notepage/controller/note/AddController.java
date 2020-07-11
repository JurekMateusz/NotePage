package pl.mjurek.notepage.controller.note;

import pl.mjurek.notepage.exception.AddObjectException;
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
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String destination = "/note_list";

        request.setCharacterEncoding("UTF-8");
        User authenticatedUser = (User) request.getSession().getAttribute("loggedUser");

        String description = request.getParameter("inputDescription").trim();
        String importantState = request.getParameter("importantState");
        String deadlineDate = request.getParameter("inputDate");

        String converted = description.replaceAll("(\r\n|\n)", "<br/>");

        NoteService noteService = new NoteService();
        try {
            noteService.addNote(authenticatedUser, converted, importantState, deadlineDate);
        } catch (AddObjectException ex) {
            request.setAttribute("errorMessage", "Can't add note");
            request.setAttribute("noteDescription", description);
            request.setAttribute("fragment", "add");

            destination = "/WEB-INF/index.jsp";
        } catch (ParseException ex) {
            request.setAttribute("errorMessage", "Invalid date");
            request.setAttribute("noteDescription", description);
            request.setAttribute("date", deadlineDate);
            request.setAttribute("fragment", "add");

            destination = "/WEB-INF/index.jsp";
        }

        request.getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("fragment", "add");
        request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
    }

}
