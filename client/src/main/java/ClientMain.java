import com.code.rpc.proxy.RpcProxy;
import com.code.rpc.registry.Registry;
import com.code.rpc.registry.ZookeeperRegistry;
import com.code.rpc.transport.Client;
import com.code.service.HelloService;

/**
 * @description: TODO
 * @author: huhongtao
 * @date: 2024-09-27 15:19
 */
public class ClientMain {


    public static void main(String[] args) {
        Client client = new Client();
        Registry registry = new ZookeeperRegistry();
        RpcProxy rpcProxy = new RpcProxy(registry, "helloService", client);
        HelloService helloService = rpcProxy.getProxy(HelloService.class);
        String data = helloService.hello("aaa");
        System.out.println(data);
    }
}
