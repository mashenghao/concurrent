package web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 防止表单提交
 *
 * @author: mahao
 * @date: 2019/9/15
 */
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String pwd = req.getParameter("pwd");
        System.out.println("request data, name :" + name + "pwd :" + pwd + "time: " + System.currentTimeMillis());
        String token = (String) req.getSession().getAttribute("token");
        if (token == null) {
            resp.setHeader("Content-type", "text/html;charset=utf-8");
            resp.getWriter().write("表单重复提交....");
            return;
        }
        Cookie[] cookies = req.getCookies();
        for (Cookie cookie : cookies) {
            if (Objects.equals("TOKEN", cookie.getName()) && Objects.equals(token, cookie.getValue())) {
                req.getSession().setAttribute("token", null);
                System.out.println("login success ！！！ " + token);
                return;
            }
        }
        resp.setHeader("Content-type", "text/html;charset=utf-8");
        resp.getWriter().write("表单重复提交了....");
    }
}
