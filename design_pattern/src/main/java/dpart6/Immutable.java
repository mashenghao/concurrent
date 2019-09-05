package dpart6;



/**
 * 多线程不可变对象设计模式：
 * <p>
 * 规则：
 * <p>
 * 1. 所有的成员都是`private final`
 * <p>
 * 2. 不提供成员的改变方法，比如setXXX( )
 * <p>
 * 3. 确保所有的方法不会被重载。有两种方法： 类定义为final 和方法定义为final
 * <p>
 * 4. 如果某一个类成员不是原始变量或者不可变类，必须通过在成员初始化(in)或者get方法(out)时通过深度clone方法，来确保类的不可变。
 * <p>
 *
 * public final class MyImmutableDemo {
 *      private final int[] myArray;
 *      public MyImmutableDemo(int[] array) {
 *           this.myArray = array.clone(); // right
 *      }
 * }
 *
 * <p>
 * 5. 对于引用类型数据，通过拷贝或者clone方式确保不可变性；
 * <p>
 * **String的实现方式：**
 * <p>
 *
 * public final class String
 *          implements java.io.Serializable, Comparable<String>, CharSequence
 * {
 *
 *      private final char value[];//0.private final
 *      ....
 *      public String(char value[]) {//1.构造器深拷贝
 *          this.value = Arrays.copyOf(value, value.length);
 *      }
 *      ...
 *      public char[] toCharArray() {
 *           // Cannot use Arrays.copyOf because of class initialization order issues
 *          char result[] = new char[value.length];
 *          System.arraycopy(value, 0, result, 0, value.length);//2.返回对象拷贝
 *          return result;
 *      }
 * ...
 * }
 *
 *
 * @author: mahao
 * @date: 2019/8/28
 */
public final class Immutable {

    /*
        表示是不可变对象，线程的操作只能是访问，属性被定义被定义为final
     */

    private final String name;
    private final int age;

    public Immutable(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {


        return name;
    }

    public int getAge() {
        return age;
    }
}
