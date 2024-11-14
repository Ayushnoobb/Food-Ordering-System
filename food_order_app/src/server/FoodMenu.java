package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface FoodMenu extends Remote {
    List<Food> getMenu() throws RemoteException;
    Food getFoodItem(String name) throws RemoteException;
    String placeOrder(Map<String, Integer> order) throws RemoteException;
    
    Cart getCart() throws RemoteException;
    void addToCart(String foodName, int quantity) throws RemoteException;
    void removeFromCart(String foodName) throws RemoteException;
    Order checkout() throws RemoteException;
}

