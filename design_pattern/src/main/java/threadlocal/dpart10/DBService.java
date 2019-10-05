package threadlocal.dpart10;

import chapter3.ThreadUtil;

/**
 * 数据库服务，获取到数据，然后存入到上下文中
 *
 * @author: mahao
 * @date: 2019/9/2
 */
public class DBService {

    private final String NAME = "mahao";

    public void execute(Context context) {
        ThreadUtil.sleep(1000);
        context.setName(NAME + ThreadUtil.getName());
    }
}
