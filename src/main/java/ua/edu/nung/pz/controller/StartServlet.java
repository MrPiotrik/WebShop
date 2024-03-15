package ua.edu.nung.pz.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.edu.nung.pz.model.Firebase;
import ua.edu.nung.pz.model.User;
import ua.edu.nung.pz.view.IndexView;

import java.io.*;
import java.util.Properties;


@WebServlet(name = "StartServlet", urlPatterns = {"/*"}, loadOnStartup = 1)
public class StartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String body = "";
        String context = "";

        switch (request.getPathInfo()) {
            case "/contacts":
                context = "<h2>Our Contacts!</h2>\n";
                break;
            case "/login":
                context = "<h2>Login!</h2>\n";
                context += IndexView.getInstance().getLoginForm();
                break;
            case "/forgotpassword":
                context = "<h2>Restore Password!</h2>\n";
                break;
            default:
                context = "<h2>Hello World from Servlet!</h2>\n";
        }


        body = IndexView.getInstance().getBody(
                IndexView.getInstance().getHeader(""),
                IndexView.getInstance().getFooter(""),
                context);

        out.println(IndexView.getInstance().getPage("Green Shop", body));

        //TODO: remove after successfully testing
        User user = new User();
        user.setEmail("email1@email.com");
        user.setPassword("112211221122");
        user.setDisplayName("Test User");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contextPath = request.getContextPath();
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        // login / register
        Firebase firebase = Firebase.getInstance();
        if (firebase.getUserByEmail(user.getEmail()).equals(Firebase.USER_EXISTS)) {
            String firebaseResponse = firebase.signInWithEmailAndPassword(user.getEmail(), user.getPassword());
            if(firebaseResponse.equals(Firebase.PASSWORD_OK)) {
                System.out.println(Firebase.PASSWORD_OK);
            }  else {
                System.out.println("Wrong Password");
            }
        } else {
            System.out.println("User NOT Exist");
            String userMsg = Firebase.getInstance().createUser(user);
        }

        System.out.println(user);

        response.sendRedirect("/");
    }

    @Override
    public void init() throws ServletException {
        super.init();
        String path = getServletContext().getRealPath("html/");
        IndexView indexView = IndexView.getInstance();
        indexView.setPath(path);

        initFirebase();
    }

    private void initFirebase() {
        String[] firebaseProp = new String[4];
        Properties props = new Properties();
        InputStream is = getClass().getClassLoader().getResourceAsStream("app.properties");
        try {
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Firebase.getInstance().setFirebaseConfigPath(props.getProperty("file.path"));
        Firebase.getInstance().setFirebaseName(props.getProperty("firebase.name"));
        Firebase.getInstance().setApiKey(props.getProperty("web.api.key"));
        Firebase.getInstance().setSignInUrl(props.getProperty("signInUrl"));
        Firebase.getInstance().init();
    }
}
