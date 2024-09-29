import com.code.rpc.factory.BeanManager;
import com.code.rpc.registry.ServiceInfo;
import com.code.rpc.registry.ZookeeperRegistry;
import com.code.rpc.transport.Server;
import com.code.service.HelloService;
import org.apache.curator.x.discovery.ServiceInstance;

/**
 * @description: TODO
 * @author: huhongtao
 * @date: 2024-09-27 15:55
 */
public class ServerMain {

    public static void main(String[] args) throws Exception {
        // 创建ZookeeperRegistry，并将Provider的地址信息封装成ServerInfo
        // 对象注册到Zookeeper
        HelloService helloService = new HelloServiceImpl();
        ZookeeperRegistry discovery = new ZookeeperRegistry();
        ServiceInfo serviceInfo = new ServiceInfo("10.1.3.13", 20000);
        discovery.register(ServiceInstance.builder().name("helloService").payload(serviceInfo).build());

        BeanManager.registerBean(HelloService.class.getName(), helloService);
        Server server = new Server();
        server.start();
    }
}
