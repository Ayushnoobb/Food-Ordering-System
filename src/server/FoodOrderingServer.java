
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class FoodOrderingServer {
    public static void main(String[] args) throws Exception {
        // Create and export the RMI registry on port 1099
        Registry registry = LocateRegistry.createRegistry(1098);

        // Create an instance of the server implementation
        // UserServiceImp service = new UserServiceImp();
        FoodOrderingServiceImpl service = new FoodOrderingServiceImpl();

        // Bind the service object to the registry with the name "FoodOrderingService"
        registry.rebind("FoodOrderingService", service);

        System.out.println("Server is running...");
    }
}
