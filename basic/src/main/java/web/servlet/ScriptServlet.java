package web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 当后台返回js脚本，验证前台是否会执行脚本
 *
 * @author: mahao
 * @date: 2019/9/15
 */
public class ScriptServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("request success");
        String callback = req.getParameter("callback");
        resp.getWriter().write(callback + "('abc');");

    }
}
