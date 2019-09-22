package chapter6;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Unsafe的使用
 *
 * @author: mahao
 * @date: 2019/9/22
 */
public class UnsafeTest {

    public static void main(String[] args) {
        Unsafe unsafe = getUnsafe();
        System.out.println(unsafe);
    }

    private static Unsafe getUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (Exception e) {
            throw new RuntimeException("error");
        }
    }
}
