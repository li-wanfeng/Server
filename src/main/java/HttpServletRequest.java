import java.util.Map;

/**
 * @author 风车下跑
 * @create 2022-12-17
 */
public class HttpServletRequest {

    private String requestName;
    private Map<String,String> requestMap;

    public HttpServletRequest(String requestName, Map<String, String> requestMap) {
        this.requestName = requestName;
        this.requestMap = requestMap;
    }

    public String getRequestName() {
        return requestName;
    }

    public Map<String, String> getRequestMap() {
        return requestMap;
    }
    public String getRequest(String key){
        return requestMap.get(key);
    }
}
