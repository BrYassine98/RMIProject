package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Store extends Remote {
    double getPrice(String ingredient) throws RemoteException;
}
