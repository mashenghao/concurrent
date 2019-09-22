package chapter6;

import chapter1.ThreadUtil;
import sun.misc.Unsafe;

import java.io.FileInputStream;
import java.lang.reflect.Field;

/**
 * @author: mahao
 * @date: 2019/9/22
 */
public class UnsafeTest2 {

    public static void main(String[] args) throws Exception {
        //如果想使用一个类里面的普通方法，必须先初始化对象
        Class<?> aClass = ClassLoader.getSystemClassLoader().loadClass("chapter6.UnsafeTest2$In");
        In in1 = (In) aClass.newInstance();
        System.out.println(in1.getI());
        //1. 但是对于Unsafe，则可以直接跳过初始化，直接开辟空间,不会调用构造方法
        Unsafe unsafe = ThreadUtil.getUnsafe();
        In in2 = (In) unsafe.allocateInstance(In.class);
        System.out.println("跳过初始化方法，直接生成对象是： " + in2.getI());

        //2.作用2 ： 给私有属性赋值
        Field field = in2.getClass().getDeclaredField("i");
        unsafe.putInt(in2, unsafe.objectFieldOffset(field), 2);
        System.out.println("使用unsafe给变量赋值后结果是   " + in2.getI());
        //3. 作用3； 加载类；
        byte[] buf = findClass("D:\\Java\\ideas\\concurrent\\juc\\target\\classes\\chapter1\\CAS");
        Class clazz = unsafe.defineClass(null, buf, 0, buf.length);
        System.out.println(clazz + "   " + clazz.getClassLoader());
    }

    private static byte[] findClass(String classname) {
        try {
            FileInputStream fileInputStream = new FileInputStream(classname + ".class ");
            byte[] buf = new byte[fileInputStream.available()];
            fileInputStream.read(buf);
            return buf;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static class In {

        private int i;

        static {

            System.out.println("static ... ");
        }

        public In() {
            i = 1;
            System.out.println("construct ... ");
        }

        public int getI() {
            return i;
        }
    }
}
