import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

/**
 * @author 风车下跑
 * @create 2022-12-20
 * 启动的时候加载io相关的数据
 */
public class MyServerReader {
    public MyServerReader() {
        System.out.println("web.properties加载成功");
    }

    private static HashMap<String,String> map = new HashMap<>();
    static {
        Properties properties = new Properties();
        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("web.properties");
        try {
            properties.load(resourceAsStream);
            Enumeration<Object> keys = properties.keys();
            if (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                String value = (String) properties.get(key);
                map.put(key,value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (resourceAsStream != null){
                    resourceAsStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static String getValue(String key){
        return map.get(key);
    }
}
