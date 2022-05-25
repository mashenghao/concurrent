package web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * 防止表单重复提交，令牌方式
 *
 * @author: mahao
 * @date: 2019/9/15
 */

public class FormServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        Cookie cookie = new Cookie("TOKEN", token);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 3);//秒
        resp.addCookie(cookie);
        req.getSession().setAttribute("token", token);
        resp.sendRedirect("/concurrent/login.jsp");

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
}
