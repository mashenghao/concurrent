package atomic.cas;

/**
 * @author: mahao
 * @date: 2019/9/21
 */
public class CASException extends Exception {

    public CASException() {
        super();
    }

    public CASException(String message) {
        super(message);
    }
}
