package dpart4;

/**
 * 临界资源，只允许一个线程访问
 *
 * @author: mahao
 * @date: 2019/8/26
 */
public class Gate {

    private int count;
    private String name;
    private String code;


    //访问资源
    public synchronized void pass(String name, String code) {
        this.name = name;
        this.code = code;
        verify();//验证允许访问
        count++;//记录访问资源数
    }

    private void verify() {
        if (name == code || name != null && name.equals(code)) {

        } else {
            System.out.println("error" + count + toString());
        }
    }

    @Override
    public String toString() {
        return "Gate{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
