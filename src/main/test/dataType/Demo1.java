package dataType;

import java.util.Arrays;

/**
 * java中的值传递问题。
 */
public class Demo1 {

    public static void main(String[] args) {
        int a = 1;
        Integer b = 1;
        String s = "??";
        int[] arr = {1, 2};
        change(a, b, s, arr);
        System.out.println("change method be call after");
        /*
        1.a是基本数据类型，在一个方法中，基本数据类型的值，存放在栈帧的局部变量表中，
        当基本类型作为参数传递到其他方法中时，在其他方法的局部变量表中，会创建一个局部变量
        这个局部变量的数值，就是基本数据类型的值。所以通过描述可以明白，两个方法中的值，不是同一个，
        所以方法中的数据更改，不会影响到调用方法的数值。

         */
        System.out.println(a);//a=1

        /*
        2.b是Integer的对象，所以b传递的是b在堆内存中的对象地址，也就是change方法中b指向的对象，
        和main方法中b指向的对象是同一个。对象存在于堆内存中，堆内存中的数据是共享的，当对象改变时，
        其他引用变量都能感知出来。但是在这里，输出结果仍然是1，原因是change方法改变的值不是main方法中
        b指向的对象，而是新创建了一个对象。Integer，String等基本数据的包装类型，都被定义成final，意味
        着他们对象的值，都是不允许改变的，所以main方法中的b，指向的任然是1的包装类型，而change方法中的
        b，指向的对象是新创建的对象2，两个引用指向的不是同一个对象。 检测可以通过打印b的地址值，看出不是
        同一个对象。
         */
        System.out.println(b);//b=1

        /*
        3.s是String类型，s创建的数据也是不可变的，s是String类型，String在设计时，考录到安全和效率，将
        String设置为不可变,当s被创建后，对象"??"就作为常量，被存放在常量池中，作为常量。当其他字符串创建时，
        会先去常量池中查询是否存在字符串常量，有了直接返回该常量地址，否则，重新创建一个字符串，然后放入常量池
        中。当发生String类型的更改时，比如change中s的更改，main中s和change中s是指向同一个常量字符串，当change中
        发生更改时，会重新创建一个字符串对象“？？--”，然后将该对象的引用地址传给s引用变量，"??"仍然存在，被main方法中的
        s所指向着，没有地方更改main中s指向的对象，所以数据还是"??"; 因为string代表的数据类型不可变特性，所以特别适合
        做map的key，key对象不会发生更改，String也重写了equals方法，也适合判断。
         */
        System.out.println(s);//??

        /*
        4.arr是数组类型，数据是可变的，arr是对象，change改变了对象的值，所以会发生数据变化。
         */
        System.out.println(Arrays.toString(arr));//{0,1}

    }


    private static void change(int a, Integer b, String s, int[] arr) {

        a++;
        b = b++;
        s = s + "--";
        arr[0] = 0;

        System.out.println(a);
        System.out.println(b);
        System.out.println(s);
        System.out.println(Arrays.toString(arr));
    }
}
