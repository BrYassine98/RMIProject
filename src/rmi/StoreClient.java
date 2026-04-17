package rmi;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class StoreClient {
    private static final String[] DEFAULT_INGREDIENTS = {"cornichons", "safran", "sel", "poivre"};

    private final List<String> storeNames;
    private final String registryHost;
    private final int registryPort;

    public StoreClient(String registryHost, int registryPort) {
        this.registryHost = registryHost;
        this.registryPort = registryPort;
        this.storeNames = List.of("Mag1", "Mag2", "Mag3");
    }

    private Store lookupStore(String storeName) throws Exception {
        return (Store) Naming.lookup("rmi://" + registryHost + ":" + registryPort + "/" + storeName);
    }

    public void searchLowestPrice(String ingredient) {
        ExecutorService executor = Executors.newFixedThreadPool(storeNames.size());
        Map<String, Future<Double>> futures = new LinkedHashMap<>();

        try {
            for (String storeName : storeNames) {
                Callable<Double> task = () -> {
                    try {
                        Store store = lookupStore(storeName);
                        return store.getPrice(ingredient);
                    } catch (RemoteException e) {
                        return Double.NaN;
                    }
                };
                futures.put(storeName, executor.submit(task));
            }

            double lowestPrice = Double.MAX_VALUE;
            String cheapestStore = null;

            System.out.println();
            System.out.println("Recherche du prix pour: " + ingredient);
            for (Map.Entry<String, Future<Double>> entry : futures.entrySet()) {
                String storeName = entry.getKey();
                try {
                    double price = entry.getValue().get();
                    if (Double.isNaN(price)) {
                        System.out.println("- " + storeName + " : indisponible");
                        continue;
                    }
                    System.out.println("- " + storeName + " : " + price);
                    if (price < lowestPrice) {
                        lowestPrice = price;
                        cheapestStore = storeName;
                    }
                } catch (Exception e) {
                    System.out.println("- " + storeName + " : erreur");
                }
            }

            if (cheapestStore != null) {
                System.out.println("Prix le plus bas: " + lowestPrice + " chez " + cheapestStore);
            } else {
                System.out.println("Aucun magasin n'a fourni de prix pour cet ingrédient.");
            }
        } finally {
            executor.shutdownNow();
        }
    }

    private void runInteractive() {
        Scanner scanner = new Scanner(System.in);
        try {
            while (true) {
                System.out.println();
                System.out.println("--- Store Client ---");
                for (int i = 0; i < DEFAULT_INGREDIENTS.length; i++) {
                    System.out.println((i + 1) + ". " + DEFAULT_INGREDIENTS[i]);
                }
                System.out.println("0. Quitter");
                System.out.print("Choix: ");

                String input = scanner.nextLine().trim();
                if (input.equals("0")) {
                    break;
                }

                try {
                    int choice = Integer.parseInt(input);
                    if (choice >= 1 && choice <= DEFAULT_INGREDIENTS.length) {
                        searchLowestPrice(DEFAULT_INGREDIENTS[choice - 1]);
                    } else {
                        System.out.println("Choix invalide.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Entrée invalide.");
                }
            }
        } finally {
            scanner.close();
        }
    }

    public static void main(String[] args) {
        String host = args.length >= 1 ? args[0] : "localhost";
        int port = args.length >= 2 ? Integer.parseInt(args[1]) : 1099;
        new StoreClient(host, port).runInteractive();
    }
}
