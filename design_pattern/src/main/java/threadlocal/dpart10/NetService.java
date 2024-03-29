package threadlocal.dpart10;

import chapter3_methods.ThreadUtil;

import java.util.Map;

/**
 * 网络服务，需要依托上下文中的session，从sesson中获取
 * 到参数后，才可以请求网络
 *
 * @author: mahao
 * @date: 2019/9/2
 */
public class NetService {

    public String execute(Context context) {
        Map session = context.getSession();
        String val = (String) session.get("key");
        String netResult = getnetResult(val);
        return netResult;
    }

    private String getnetResult(String val) {
        ThreadUtil.sleep(3000);
        return val + "--- 789net" + "_____" + ThreadUtil.getName();
    }
}
