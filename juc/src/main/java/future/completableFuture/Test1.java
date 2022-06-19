package future.completableFuture;

import atomic.ThreadUtil;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.CompletableFuture;

/**
 * CompletableFuture原理：
 * 提供了根据前一个或者前多个future的结果，在进行处理的逻辑。
 * 实现就是，
 * 1. 调用一个处理异步结果的方法时，就会新创建一个Future返回，
 * 方法的处理逻辑是，新建一个Completion对象，这个对象中保存了用户定义的函数，
 * 上一个Future,还有新返回的Future，处理函数结束后，就会将结果写到Future，外部就能
 * 取到异步处理后的结果， 还有异步线程池。
 * <p>
 * 2.新的处理函数被触发，需要依赖前一个Future，所有CompleteFuture中有个stack属性，类型就是Completion，
 * 用来当前Future完成后，需要触发的下一个stage的处理。 如果同一个Future，添加了多个后续的处理stage，
 * 比如 c1,同时有c21 和 c22 两个作为后续，这时候，就用到Completion中的next属性，这也是个Completion 类型的
 * 属性，是用来表示当前Completion完成后，要出发的下一个Completion。当 c1的stack被设为c21后，c22又来添加，则会将stack
 * 设为c22，c21设为c22的next的值。 所以起名为stack.
 * <p>
 * 3.CompleteFuture 的每个后续处理都对应一个内部类，这些内部类都是Completion的子类，不同的是处理函数，有的是Function，
 * 或者Consumer,所以有多个，相同的是有Future和下一个Future的输出引用，以及线程池。
 * 1.对于触发下一个stage的操作，如果是起始Future，则是调用的java.util.concurrent.CompletableFuture#postComplete()，
 * 2.对于每个Completion内部，函数执行完毕后，也会触发下一个stack的执行，也是调用postComplete()方法。
 * <p>
 * 4. 函数签名，
 * 对于只处理正确结果：
 * 1元输入，是thenApply、thenAccept、thenRun
 * 2元输入与，是thenCombine、thenAcceptBoth、runAfterBoth
 * 2元输入或，是applyToEither、acceptEither、runAfterEither
 * 对于异常和正确情况：
 * 全部情况分别是:  handle(带输出) 、 whenComplete(不带输出)
 * 异常情况： exceptionally
 *
 * @author mahao
 * @date 2022/06/16
 */
public class Test1 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        /*
        当CF1 抛出异常时，之后的都会抛出 同一个异常，递归抛出异常。 除非用了whenComple()这种处理异常的函数
         */
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {
            if (1 == 1) {
                throw new RuntimeException("f1 抛出异常");
            }
            return "";
        });

        CompletableFuture<Void> f2 = f1.thenRun(() -> System.out.println("我是f2"));
        CompletableFuture<Void> f3 = f1.thenRun(() -> System.out.println("我是f3"));

        //异常处理，如果f1是正常的，当前CF返回的就是他的值，不正常就会执行函数，返回一个函数设置的默认值。
        f1.exceptionally((r) -> {
            return "异常处理";
        });

        try {
            f2.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        f3.get();


    }

}
