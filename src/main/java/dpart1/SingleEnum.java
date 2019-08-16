package dpart1;

/**
 * 通过枚举类型实现单例（2019/08）
 */
public class SingleEnum {

    private SingleEnum() {
    }

    /**
     * 枚举类中含有单例类的实例，枚举中之枚举出一个对象，
     * 就相当于改类只生成一个对象实例，也就是单例设计模式，其
     * 原理就和静态内部类是一样的，知识枚举类是静态内部类的简约实现
     */
    private static enum Single {

        INSTANCE;

        private SingleEnum singleEnum;

        private Single() {
            singleEnum = new SingleEnum();
        }

        public SingleEnum getSingleEnum() {
            return singleEnum;
        }
    }

    public static SingleEnum getInstance() {
        return Single.INSTANCE.getSingleEnum();
    }

    public static void main(String[] args) {
        SingleEnum instance = SingleEnum.getInstance();
        System.out.println(instance);
    }
}
