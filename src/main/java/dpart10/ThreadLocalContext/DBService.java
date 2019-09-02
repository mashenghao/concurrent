package dpart10.ThreadLocalContext;

import chapter3.ThreadUtil;
import dpart10.Context;

/**
 * 数
 *
 * @author: mahao
 * @date: 2019/9/2
 */
public class DBService {

    private final String NAME = "mahao";

    public void execute() {
        Context context = ActionContext.getActionContext().getContext();
        ThreadUtil.sleep(1000);
        context.setName(NAME + ThreadUtil.getName());
    }
}
