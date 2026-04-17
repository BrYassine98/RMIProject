# RMI Project – Comparateur de prix entre magasins

## 1) Project Overview / Vue d'ensemble

Ce projet est une application **Java RMI (Remote Method Invocation)** qui permet à un client de consulter plusieurs magasins distants et d'afficher **le prix le plus bas** pour un ingrédient donné.

> **Purpose (EN):** Demonstrate distributed computing concepts (remote interface, remote objects, registry lookup, parallel requests, and result aggregation).

Le client interroge `Mag1`, `Mag2` et `Mag3` en parallèle, récupère les prix disponibles, puis calcule automatiquement la meilleure offre.

---

## 2) Fonctionnalités principales (Features)

- Architecture distribuée basée sur **Java RMI**
- 3 serveurs de magasin (`Mag1`, `Mag2`, `Mag3`)
- Recherche du prix en **parallèle** côté client
- Agrégation des réponses et sélection du prix minimum
- Chargement des prix depuis des fichiers texte (`data/*.txt`)
- Gestion des erreurs réseau / indisponibilité d’un magasin
- Menu interactif en console

---

## 3) Architecture du projet

### 3.1 Architecture système

- **Interface distante** : `Store` expose la méthode RMI `getPrice(String ingredient)`
- **Serveurs** : `StoreManager` implémente `Store`, lit les données et publie le service RMI
- **Client** : `StoreClient` contacte les 3 magasins et compare les prix
- **RMI Registry** : point d’enregistrement/résolution des objets distants (port 1099 par défaut)

### 3.2 Description des composants

- `Store.java` : contrat distant partagé client/serveur
- `StoreManager.java` : implémentation serveur + publication dans le registry
- `StoreClient.java` : interface utilisateur console + logique de comparaison

### 3.3 Communication flow diagram

```text
┌───────────────────────────┐
│        StoreClient        │
│ (menu + comparaison prix) │
└─────────────┬─────────────┘
              │ lookup rmi://host:1099/MagX
              ▼
      ┌───────────────────┐
      │   RMI Registry    │
      └───────┬───────────┘
              │ résout les services
      ┌───────┼───────────────┐
      ▼       ▼               ▼
┌────────┐ ┌────────┐     ┌────────┐
│  Mag1  │ │  Mag2  │     │  Mag3  │
│StoreMgr│ │StoreMgr│     │StoreMgr│
└───┬────┘ └───┬────┘     └───┬────┘
    │ getPrice   │ getPrice      │ getPrice
    └───────┬────┴───────┬──────┘
            ▼            ▼
      résultats partiels (prix)
            ▼
      minimum global affiché
```

---

## 4) Structure complète du projet (Project Structure)

```text
RMIProject/
├── .gitignore                  # Ignore bin/, *.class, logs/
├── README.md                   # Documentation du projet
├── data/
│   ├── Mag1.txt                # Prix magasin 1
│   ├── Mag2.txt                # Prix magasin 2
│   └── Mag3.txt                # Prix magasin 3
├── scripts/
│   ├── start-client.bat        # Compile + lance le client
│   ├── start-mag1.bat          # Compile + lance serveur Mag1
│   ├── start-mag2.bat          # Compile + lance serveur Mag2
│   ├── start-mag3.bat          # Compile + lance serveur Mag3
│   └── start-registry.bat      # Optionnel: lance rmiregistry séparé
└── src/
    └── rmi/
        ├── Store.java          # Interface distante RMI
        ├── StoreClient.java    # Client console
        └── StoreManager.java   # Serveur RMI
```

---

## 5) Prérequis (Prerequisites)

- **Java JDK 11+** (JDK 17 recommandé)
- Variables d’environnement configurées : `java`, `javac`, `rmiregistry` accessibles
- Système supporté : **Windows / Linux / macOS**
- Port réseau libre : **1099** (ou port personnalisé)

Vérification rapide :

```bash
java -version
javac -version
```

---

## 6) Compilation & Setup (Windows / Linux / macOS)

### 6.1 Compilation manuelle (toutes plateformes)

Depuis la racine du projet :

```bash
javac -d bin src/rmi/*.java
```

### 6.2 Setup et lancement – Windows (CMD/PowerShell)

Option recommandée (scripts fournis) :

1. Ouvrir 3 terminaux et lancer :

```bat
scripts\start-mag1.bat
scripts\start-mag2.bat
scripts\start-mag3.bat
```

2. Ouvrir un 4ᵉ terminal :

```bat
scripts\start-client.bat
```

> Le premier serveur actif crée le registry si nécessaire. Vous pouvez aussi utiliser `scripts\start-registry.bat`.

### 6.3 Setup et lancement – Linux / macOS

Aucun script shell n’est versionné, mais ces commandes fonctionnent :

```bash
# 1) Compiler
javac -d bin src/rmi/*.java

# 2) Lancer les 3 serveurs dans 3 terminaux distincts
java -cp bin rmi.StoreManager Mag1 data/Mag1.txt localhost 1099
java -cp bin rmi.StoreManager Mag2 data/Mag2.txt localhost 1099
java -cp bin rmi.StoreManager Mag3 data/Mag3.txt localhost 1099

# 3) Lancer le client dans un 4e terminal
java -cp bin rmi.StoreClient localhost 1099
```

---

## 7) Guide d’utilisation (Usage Guide)

### 7.1 Démarrer tous les composants

1. Compiler (`javac ...`)
2. Démarrer `Mag1`, `Mag2`, `Mag3`
3. Démarrer `StoreClient`

### 7.2 Navigation menu côté client

Le client affiche un menu d’ingrédients :

- `1` → cornichons
- `2` → safran
- `3` → sel
- `4` → poivre
- `0` → quitter

### 7.3 Exemple d’utilisation

1. Choisir `2` (safran)
2. Le client interroge les 3 magasins en parallèle
3. Il affiche les prix disponibles
4. Il annonce le magasin le moins cher

---

## 8) Format des données (Ingredient / Price format)

Chaque fichier `data/MagX.txt` contient **une ligne par ingrédient** :

```text
ingredient prix
```

Exemple (fichier `data/Mag1.txt`) :

```text
cornichons 10.0
safran 1000.0
sel 1.0
poivre 100.0
```

Autre exemple (`data/Mag3.txt`) :

```text
cornichons 50.0
safran 6.0
sel 30.0
poivre 50.0
```

Règles :

- séparateur : espace(s)
- ingrédient : texte (insensible à la casse côté serveur)
- prix : nombre décimal (`double`)
- lignes vides et commentaires (`#`) ignorés

---

## 9) Détail des composants

### 9.1 `Store.java` (Remote Interface)

- Hérite de `java.rmi.Remote`
- Déclare la méthode distante :

```java
double getPrice(String ingredient) throws RemoteException;
```

### 9.2 `StoreManager.java` (Server Implementation)

Responsabilités :

- Charger les prix depuis `data/MagX.txt`
- Implémenter `getPrice(...)`
- Publier l’objet distant dans le RMI Registry (`Naming.rebind`)
- Créer le registry si absent (`LocateRegistry.createRegistry(...)`)

Arguments principaux :

```text
java rmi.StoreManager <Mag1|Mag2|Mag3> <data-file> [registry-host] [registry-port]
```

### 9.3 `StoreClient.java` (Client Implementation)

Responsabilités :

- Afficher le menu utilisateur
- Résoudre les objets distants (`Naming.lookup`)
- Lancer des requêtes parallèles (`ExecutorService`)
- Comparer les réponses et afficher le minimum

Arguments principaux :

```text
java rmi.StoreClient [registry-host] [registry-port]
```

---

## 10) Troubleshooting (Problèmes fréquents)

### Problème: `Connection refused` / impossible de joindre un magasin

- Vérifier que le serveur concerné est lancé
- Vérifier l’hôte et le port (`localhost 1099`)
- Vérifier qu’aucun firewall ne bloque le port

### Problème: `ClassNotFoundException` / erreurs de classpath

- Recompiler : `javac -d bin src/rmi/*.java`
- Lancer avec `-cp bin`

### Problème: `Ingrédient introuvable`

- Vérifier que l’ingrédient existe dans `data/MagX.txt`
- Respecter le format `ingredient prix`

### Problème: port 1099 déjà utilisé

- Fermer le process qui utilise 1099
- Ou relancer avec un autre port pour tous les composants

---

## 11) Exemple de sortie console (Example Output)

```text
--- Store Client ---
1. cornichons
2. safran
3. sel
4. poivre
0. Quitter
Choix: 2

Recherche du prix pour: safran
- Mag1 : 1000.0
- Mag2 : 500.0
- Mag3 : 6.0
Prix le plus bas: 6.0 chez Mag3
```

---

## 12) License & Author

- **Author:** Yassine (GitHub: [@BrYassine98](https://github.com/BrYassine98))
- **License:** Aucune licence n'est fournie dans le dépôt (ajoutez un fichier `LICENSE` pour formaliser)

---

## Quick Start (EN)

```bash
javac -d bin src/rmi/*.java
java -cp bin rmi.StoreManager Mag1 data/Mag1.txt localhost 1099
java -cp bin rmi.StoreManager Mag2 data/Mag2.txt localhost 1099
java -cp bin rmi.StoreManager Mag3 data/Mag3.txt localhost 1099
java -cp bin rmi.StoreClient localhost 1099
```
