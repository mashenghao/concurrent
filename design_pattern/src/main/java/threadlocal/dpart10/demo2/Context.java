package threadlocal.dpart10.demo2;

import java.util.HashMap;
import java.util.Map;

/**
 * 上下文类，每一个服务器程序对应一个上下文类
 *
 * @author: mahao
 * @date: 2019/10/3
 */
public class Context {

    private Map<String, Object> session = new HashMap<>();
    
    public Map<String, Object> getSession() {
        return session;
    }
}
