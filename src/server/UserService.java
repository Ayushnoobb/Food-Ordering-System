
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UserService extends Remote {

  boolean registerUser(
    String firstName,
    String lastName,
    String password,
    String passportNumber,
    String email ,
    Integer role
  ) throws RemoteException;

  boolean loginUser(
    String email,
    String password
  ) throws RemoteException ;

}