package gym.gymwiki;

import java.io.*;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        createPage(request, response);
    }

    private void createPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        TIUser user = (TIUser) session.getAttribute("user");
        if (user == null) {
            user = new TIUser();
            session.setAttribute("user", user);
        }

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        ServletContext context = getServletContext();

        String page = request.getParameter("page");
        String layout = Tools.getLayout("index.html", context);
        layout = Tools.fill(layout, "HEADER", "pages/header.html", context);

        if("logowanie".equals(page)) {
            layout = Tools.fill(layout, "BODY", "pages/login.html", context);
        }else if ("wylogowanie".equals(page)) {
            layout = Tools.fill(layout, "BODY", "pages/logout.html", context);
        }else if("rejestracja".equals(page)) {
            layout = Tools.fill(layout, "BODY", "pages/rejestracja.html", context);
        } else {
            layout = Tools.fill(layout, "BODY", "pages/body.html", context);
        }

        if (user.getPrivilege() == -1) {
            layout = layout.replace("[[LOGOWANIE]]",
                    "<li><a href=\"site?page=logowanie\">Logowanie</a></li>" +
                            "<li><a href=\"site?page=rejestracja\">Rejestracja</a></li>");
        } else {
            layout = layout.replace("[[LOGOWANIE]]", "<li><a href=\"site?page=wylogowanie\">Wylogowanie</a></li>");
        }

        layout = Tools.fill(layout, "FOOTER", "pages/footer.html", context);
        out.println(layout);
        out.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String komunikat = "";
        HttpSession session = request.getSession();
        TIUser user = (TIUser) session.getAttribute("user");
        PrintWriter out = response.getWriter();
        out.println("<script type='text/javascript'>");
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        if (user == null) {
            user = new TIUser();
            session.setAttribute("user", user);
        }

        if (request.getParameter("potwierdzLogin") != null && user.getPrivilege() == -1) {
            komunikat = "Zalogowano jako ";
            if (login.equals("user") && password.equals("user")) {
                user.setLogin(login);
                user.setPrivilege(1);
                komunikat += "user.";
            } else if (login.equals("admin") && password.equals("admin")) {
                user.setLogin(login);
                user.setPrivilege(2);
                komunikat += "administrator.";
            } else {
                komunikat = "Błędny login lub hasło.";
            }
        } else if (request.getParameter("potwierdzWylogowanie") != null && user.getPrivilege() != -1) {
            user.setLogin("");
            user.setPrivilege(-1);
            out.println("localStorage.clear();");
            komunikat = "Zostałeś wylogowany.";
        }

        out.println("alert('" + komunikat + "');");
        out.println("</script>");

        createPage(request, response);
    }

    public void destroy() {
    }
}