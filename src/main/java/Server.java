import java.io.*;
import java.lang.reflect.Constructor;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 风车下跑
 * @create 2022-12-15
 */
public class Server {

    //为了保证所有的controller对象都是单例模式的
    //可以在服务器内写一个管理对象的容器，map<String,HttpServlet>
    private Map<String,HttpServlet> servletMap = new HashMap<>();
    private Socket socket;
    public static void main(String[] args) {
        new Server().start();
    }

    public void start() {
        try {
            System.out.println("服务端启动成功  等待连接");
            ServerSocket serverSocket = new ServerSocket(9999);
            while (true){
                socket = serverSocket.accept();
                //获取传送过来的数据
                this.getRequest(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getRequest(Socket socket) {
        try {
            InputStream inputStream = socket.getInputStream();
            //字符流用来读取中文
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String content = bufferedReader.readLine();
            //将参数包装成map的形式进行存储
            if (null != content && !"".equals(content)){
                this.receiveContent(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveContent(String content) {
        String requestName = null;
        Map<String,String> requestMap = null;
        int index1 = content.indexOf("/");
        int index2 = content.indexOf("?");
        int i = content.lastIndexOf(" ");
        if (index2 != -1) {//携带了参数
            requestMap = new HashMap<>();
            requestName = content.substring(index1+1,index2);
            String allkeyAndValues = content.substring(index2+1,i);
            String[] split = allkeyAndValues.split("&");
            for (String s : split) {
                String[] split1 = s.split("=");
                requestMap.put(split1[0],split1[1]);
            }
        }else{
            requestName = content.substring(index1,i);
        }
        server.HttpServletRequest request = new server.HttpServletRequest(requestName,requestMap);
        server.HttpServletResponse response = new server.HttpServletResponse();
        //通过请求路径名称，找寻资源
        this.findServlet(request,response);
    }
    //通过请求路径名称，找寻资源
    private void findServlet(server.HttpServletRequest request, server.HttpServletResponse response) {
        //找寻资源，实际的过程就是先找到controller类，再找到controller类中对应的方法
        //如果此时有过个不同包下，名称相同的controller类，就需要指明请求路径对应的是哪个controller类
        //需要进行配置（）
        try {
            //获取对应类的全路径
            String value = MyServerReader.getValue(request.getRequestName());
            if (null == value) {
                throw new server.NotFoundException("路径错误,找不到对应的资源");
            }
            //在servletMap中查找是否有对应的servlet
            HttpServlet httpServlet = servletMap.get(value);
            if (null == httpServlet){
                Class<?> aClass = Class.forName(value);
                Constructor<?> constructor = aClass.getConstructor();
                httpServlet = (HttpServlet) constructor.newInstance();
                servletMap.put(value,httpServlet);
            }
            //执行该方法
            httpServlet.service(request,response);
            //执行完成之后，将响应结果返回给浏览器
            recoverBrowser(response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void recoverBrowser(server.HttpServletResponse response) {
        try {
            OutputStream outputStream = this.socket.getOutputStream();
            String context = response.getContext();
            PrintWriter printWriter = new PrintWriter(outputStream);
            printWriter.println(context);
            printWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
