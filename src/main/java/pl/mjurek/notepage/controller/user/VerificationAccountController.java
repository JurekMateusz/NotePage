package pl.mjurek.notepage.controller.user;

import pl.mjurek.notepage.exception.DeleteObjectException;
import pl.mjurek.notepage.exception.UpdateObjectException;
import pl.mjurek.notepage.model.KeyAction;
import pl.mjurek.notepage.service.KeyActionService;
import pl.mjurek.notepage.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/verification")
public class VerificationAccountController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String key = request.getParameter("key");
        KeyActionService service = new KeyActionService();

        try {
            // Ogólnie w try powinno być jak najmniej kodu - najlepiej sama ta linia która może powodować błąd który wurzycasz
            // bo teraz to ja nie jestem w stanie stwierdzić co tu rzuca DeleteObjectException | UpdateObjectException
            // ewentualnie wyciągnij do osobnej metody - nie bój się tworzyć małych metod, dobrze nazwane opiszą samem za siebie co robią
            var keyActionOpt = service.read(key);
            if (keyActionOpt.isEmpty()) {
                request.setAttribute("errorMessage", "Wrong link");
                request.getRequestDispatcher("WEB-INF/index.jsp").forward(request, response);
                return;
            }
            KeyAction keyAct = keyActionOpt.get();
            long userId = keyAct.getUserId();
            UserService userService = new UserService();
            userService.unblock(userId);
            service.delete(key);
        } catch (DeleteObjectException | UpdateObjectException e) {
            request.setAttribute("errorMessage", "Something went wrong with DB");
            request.getRequestDispatcher("WEB-INF/error.jsp").forward(request, response);
            return;
        }

        request.setAttribute("successMessage", "Verification successful");
        request.getRequestDispatcher("WEB-INF/index.jsp").forward(request, response);
    }
}
