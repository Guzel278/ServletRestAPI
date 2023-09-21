package x5.servlet;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import x5.servlet.model.User;
import x5.servlet.service.UserService;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@WebServlet(urlPatterns = "/*")
public class UserServlet extends HttpServlet {
    private final ObjectMapper mapper = new ObjectMapper();
    private final UserService userService = new UserService();
    @Override
    protected  void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        InputStream is = req.getInputStream();
        User user = mapper.readValue(is, User.class);
        User newuser = userService.createNewUser(user);
        OutputStream os = resp.getOutputStream();
        mapper.writeValue(os, newuser);
    }
    @Override
    protected  void  doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String uri = req.getRequestURI();
       String[] split = uri.split("/");
       String idString = split[split.length - 1];
       Long id = Long.parseLong(idString);
       User user = userService.getUserById(id);
       if(user == null){
           resp.setStatus(404);
           return;
       }
       OutputStream os = resp.getOutputStream();
       mapper.writeValue(os, user);
    }
    @Override
    protected  void  doDelete (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        String[] split = uri.split("/");
        String idString = split[split.length - 1];
        Long id = Long.parseLong(idString);
        boolean res;
        try {
            res = userService.deleteUser(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        OutputStream os = resp.getOutputStream();
        mapper.writeValue(os, res);
    }
    @Override
    protected  void  doPut (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();        InputStream is = req.getInputStream();
        User newuser = mapper.readValue(is, User.class);
        String[] split = uri.split("/");
        String idString = split[split.length - 1];
        Long id = Long.parseLong(idString);
        User olduser = userService.getUserById(id);
        if(olduser == null){
            resp.setStatus(404);
            return;
        }
        userService.updateUser(id, newuser);
        OutputStream os = resp.getOutputStream();
        mapper.writeValue(os, newuser);
    }

}
