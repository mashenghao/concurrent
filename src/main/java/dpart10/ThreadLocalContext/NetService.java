package dpart10.ThreadLocalContext;

import chapter3.ThreadUtil;
import dpart10.Context;

import java.util.Map;

/**
 * @author: mahao
 * @date: 2019/9/2
 */
public class NetService {

    public String execute() {
        Context context = ActionContext.getActionContext().getContext();
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
