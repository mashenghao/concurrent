package dpart8;

/**
 * 客户端发送的一次请求
 *
 * @author: mahao
 * @date: 2019/9/1
 */
public class Request {

    private String Msg;

    public Request(String msg) {
        Msg = msg;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    @Override
    public String toString() {
        return "Request{" +
                "Msg='" + Msg + '\'' +
                '}';
    }
}
