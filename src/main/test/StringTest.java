/**
 * @author: mahao
 * @date: 2019/8/10
 */
public class StringTest {
    public static void main(String[] args) {
        String str = "xiyou" + "3g" + "backend";
        String str2 = "xiyou3gbackend";
        String str3 = str + "test";
        String str4 = "a" + "b";
        System.out.println(str2 == str);
        System.out.println(str3 == "xiyou3gbackendtest");
        System.out.println(str4 == "ab");
    }
}
