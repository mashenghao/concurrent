package dpart10;

import java.util.HashMap;
import java.util.Map;

/**
 * 线程中存储的应用向下文，需要在全局内使用里面的数据
 *
 * @author: mahao
 * @date: 2019/9/2
 */
public class Context {

    //比如全局都要使用这个name属性
    private String name;

    //session为web中需要全局共享的数值。
    private Map session = new HashMap();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map getSession() {
        return session;
    }

    public void setSession(Map session) {
        this.session = session;
    }
}
