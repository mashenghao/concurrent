package dpart1;

/**
 * 枚举类的实现方式：
 * <p>
 * EnumClass表示接口状态请求返回状态，有两个属性，状态码和信息
 */
public class EnumClass {

    /*
    1.将里面的实例定义为public static final ，
    2.类型都是类对象对属性实例，可以在静态代码块中，或者直接创建初始化都行，
        在类初始化时，静态属性直接赋值和静态代码块中赋值是一样的。
     */
    public static final EnumClass SUCCESS;
    public static final EnumClass SERVER_ERROR;
    public static final EnumClass PATH_EXCEPTION;


    static {
        SUCCESS = new EnumClass(200, "OK");
        SERVER_ERROR = new EnumClass(500, "server error");
        PATH_EXCEPTION = new EnumClass(400, "path cant find");
    }

    public EnumClass(int retCode, String retMsg) {
        this.retCode = retCode;
        this.retMsg = retMsg;
    }

    //类的属性
    private int retCode;
    private String retMsg;

    public int getRetCode() {
        return retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }
}

/**
 * 同过枚举实现上面的定义
 */
enum RequestStatus {
    SUCCESS(200, "OK"),
    SERVER_ERROR(500, "server error"),
    PATH_EXCEPTION(400, "path cant find");

    private RequestStatus(int retCode, String retMsg) {
        this.retCode = retCode;
        this.retMsg = retMsg;
    }

    //枚举的的属性
    private int retCode;
    private String retMsg;

    public int getRetCode() {
        return retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }
}

class TestEnumClass {
    public static void main(String[] args) {
        EnumClass statu = EnumClass.SUCCESS;
        System.out.println(statu);
        System.out.println(statu.getRetCode() + "_" + statu.getRetMsg());

        System.out.println("==========enum==================");
        RequestStatus statu2 = RequestStatus.SERVER_ERROR;
        System.out.println(statu2);//默认继承的枚举类，将toString方法重写了。
        System.out.println(statu2.getRetCode() + "_" + statu2.getRetMsg());
    }
}