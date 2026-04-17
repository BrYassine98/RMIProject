package rmi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class StoreManager extends UnicastRemoteObject implements Store {
    private final String storeName;
    private final Map<String, Double> prices;

    public StoreManager(String storeName, String dataFile) throws RemoteException {
        this.storeName = storeName;
        this.prices = new HashMap<>();
        loadPrices(dataFile);
    }

    private void loadPrices(String dataFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }

                String[] parts = trimmed.split("\\s+");
                if (parts.length >= 2) {
                    String ingredient = parts[0].toLowerCase();
                    double price = Double.parseDouble(parts[1]);
                    prices.put(ingredient, price);
                }
            }
            System.out.println("[" + storeName + "] Données chargées depuis " + dataFile);
        } catch (IOException | NumberFormatException e) {
            System.err.println("[" + storeName + "] Erreur de chargement: " + e.getMessage());
        }
    }

    @Override
    public double getPrice(String ingredient) throws RemoteException {
        String key = ingredient.toLowerCase();
        Double price = prices.get(key);
        if (price == null) {
            throw new RemoteException("Ingrédient introuvable: " + ingredient);
        }
        System.out.println("[" + storeName + "] getPrice(" + ingredient + ") = " + price);
        return price;
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java rmi.StoreManager <Mag1|Mag2|Mag3> <data-file> [registry-host] [registry-port]");
            System.exit(1);
        }

        String storeName = args[0];
        String dataFile = args[1];
        String registryHost = args.length >= 3 ? args[2] : "localhost";
        int registryPort = args.length >= 4 ? Integer.parseInt(args[3]) : 1099;

        try {
            System.setProperty("java.rmi.server.hostname", registryHost);
            StoreManager manager = new StoreManager(storeName, dataFile);
            try {
                LocateRegistry.getRegistry(registryHost, registryPort).list();
            } catch (RemoteException e) {
                LocateRegistry.createRegistry(registryPort);
            }
            Naming.rebind("rmi://" + registryHost + ":" + registryPort + "/" + storeName, manager);
            System.out.println("[" + storeName + "] enregistré sur rmi://" + registryHost + ":" + registryPort + "/" + storeName);
        } catch (RemoteException e) {
            System.err.println("[" + storeName + "] Erreur RMI: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("[" + storeName + "] Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
