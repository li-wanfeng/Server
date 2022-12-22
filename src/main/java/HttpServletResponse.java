/**
 * @author 风车下跑
 * @create 2022-12-20
 * 定义这个类的目的就是，为了当作一个容器，装载Controller执行后的结果
 * 类中可以描述一些方法，帮助处理String的拼接，用户使用的时候方便很多
 */
public class HttpServletResponse {
    private StringBuilder responceContext = new StringBuilder();
    //创建一个追加方法，给字符串进行追加
    public void write(String str){
        responceContext.append(str);
    }
    //创建一个方法，获取字符串的内容
    public String getContext(){
        return responceContext.toString();
    }
}
