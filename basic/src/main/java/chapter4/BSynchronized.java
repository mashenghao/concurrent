package chapter4;

/**
 * synchroinized的原理:
 *
 * @author: mahao
 * @date: 2019/8/12
 */
public class BSynchronized {

    public static void main(String[] args) {
        MyThread2 task = new MyThread2();
        for (int i = 0; i < 3; i++) {
            new Thread(task).start();
        }
    }

}


class MyThread2 implements Runnable {

    private int count = 100;
    private final byte[] lock = new byte[1];


    @Override
    public void run() {
        /*
    public void run();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=3, locals=3, args_size=1
         0: aload_0

         1: getfield      #3                  // Field lock:[B
         4: dup
         5: astore_1

         6: monitorenter
         7: getstatic     #4                  // Field java/lang/System.out:Ljava/io/PrintStream;
        10: new           #5                  // class java/lang/StringBuilder
        13: dup
        14: invokespecial #6                  // Method java/lang/StringBuilder."<init>":()V
        17: invokestatic  #7                  // Method java/lang/Thread.currentThread:()Ljava/lang/Thread;
        20: invokevirtual #8                  // Method java/lang/Thread.getName:()Ljava/lang/String;
        23: invokevirtual #9                  // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        26: ldc           #10                 // String ----->
        28: invokevirtual #9                  // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        31: aload_0
        32: getfield      #2                  // Field count:I
        35: invokevirtual #11                 // Method java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        38: invokevirtual #12                 // Method java/lang/StringBuilder.toString:()Ljava/lang/String;
        41: invokevirtual #13                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
        44: aload_0
        45: dup
        46: getfield      #2                  // Field count:I
        49: iconst_1
        50: isub
        51: putfield      #2                  // Field count:I
        54: aload_1
        55: monitorexit
        56: goto          64
        59: astore_2
        60: aload_1
        61: monitorexit


        62: aload_2
        63: athrow
        64: return

         */
        synchronized (lock) {//方法同步，同步失败，因为count判断任然会进来
            System.out.println(Thread.currentThread().getName() + "----->" + count);
            count--;
        }
    }


    /*

    public synchronized void increase();
    descriptor: ()V
    flags: ACC_PUBLIC, ACC_SYNCHRONIZED  //标记
    Code:
      stack=3, locals=1, args_size=1
         0: getstatic     #4                  // Field java/lang/System.out:Ljava/io/PrintStream;
         3: new           #5                  // class java/lang/StringBuilder
         6: dup
         7: invokespecial #6                  // Method java/lang/StringBuilder."<init>":()V
        10: invokestatic  #7                  // Method java/lang/Thread.currentThread:()Ljava/lang/Thread;
        13: invokevirtual #8                  // Method java/lang/Thread.getName:()Ljava/lang/String;
        16: invokevirtual #9                  // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        19: ldc           #10                 // String ----->
        21: invokevirtual #9                  // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        24: aload_0
        25: getfield      #2                  // Field count:I
        28: invokevirtual #11                 // Method java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        31: invokevirtual #12                 // Method java/lang/StringBuilder.toString:()Ljava/lang/String;
        34: invokevirtual #13                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
        37: aload_0
        38: dup
        39: getfield      #2                  // Field count:I
        42: iconst_1
        43: isub
        44: putfield      #2                  // Field count:I
        47: return


     */
    //同步方法
    public synchronized void increase(){
        System.out.println(Thread.currentThread().getName() + "----->" + count);
        count--;
    }
}