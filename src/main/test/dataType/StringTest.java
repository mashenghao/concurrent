package dataType;

/**
 * String源码解析：
 * <p>
 * String类型存储数据的数据结构是数组，声明为final类型的char[] value;
 */
public class StringTest {

    public static void main(String[] args) {

        String s2 = new String("abc".hashCode() + "");
        String s1 = "abc";
        System.out.println(s1);
        System.out.println(s2);
        System.out.println(s1 == s2);
        System.out.println("abc" == s1);
    }
}
