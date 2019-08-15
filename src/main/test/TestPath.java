import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @program: concurrent
 * @Date: 2019/8/9 11:24
 * @Author: mahao
 * @Description:
 */
public class TestPath {

    @Test
    public void demo1() throws IOException {
        Properties p = new Properties();

        p.load(TestPath.class.getClassLoader().getResourceAsStream("daemon.properties"));
        String path = p.getProperty("path");
        System.out.println(path);

    }
}
