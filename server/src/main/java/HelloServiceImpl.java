import com.code.service.HelloService;

/**
 * @description: TODO
 * @author: huhongtao
 * @date: 2024-09-27 15:55
 */
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String name) {
        System.out.println("name:" + name);
        return "bbb";
    }
}
