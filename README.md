# 🏪 RMI Store Project - Informatique Répartie

> **Projet de système distribué en Java utilisant RMI pour interroger plusieurs magasins en parallèle et trouver le prix le plus bas d'un ingrédient.**

**Enseignant:** Karim Ben romdhane | **Classe:** MPGL1/S2

---

## 📋 Table des matières

- [Vue d'ensemble](#vue-densemble)
- [Caractéristiques](#caractéristiques)
- [Architecture du système](#architecture-du-système)
- [Structure du projet](#structure-du-projet)
- [Prérequis](#prérequis)
- [Installation et compilation](#installation-et-compilation)
- [Guide d'utilisation](#guide-dutilisation)
- [Format des données](#format-des-données)
- [Documentation des composants](#documentation-des-composants)
- [Dépannage](#dépannage)
- [Exemple de sortie](#exemple-de-sortie)

---

## 🎯 Vue d'ensemble

Ce projet implémente un système distribué utilisant **Java RMI (Remote Method Invocation)** qui simule un scénario réel : un cuisinier cherche les ingrédients au meilleur prix chez différents magasins.

### Scenario
Le client (cuisinier) cherche les ingrédients nécessaires pour préparer un repas au meilleur coût. Il interroge **3 magasins en parallèle** et affiche le magasin proposant le prix le plus bas pour l'ingrédient recherché.

---

## ✨ Caractéristiques

- ✅ **Interrogation parallèle** des 3 magasins simultanément
- ✅ **Architecture distribuée** utilisant Java RMI
- ✅ **Interface client interactive** avec menu de sélection
- ✅ **Gestion d'erreurs robuste** (magasins indisponibles, ingrédients non trouvés)
- ✅ **Enregistrement automatique** du registry RMI
- ✅ **Support cross-platform** (Windows/Linux/Mac)
- ✅ **Données dynamiques** chargées depuis des fichiers texte

---

## 🏗️ Architecture du système

### Diagramme d'architecture

```
┌──────────────────────────────────────────────────────────────┐
│                     Réseau RMI                               │
└──────────────────────────────────────────────────────────────┘
         ▲                    ▲                    ▲
         │                    │                    │
    appel RMI           appel RMI            appel RMI
    getPrice()          getPrice()           getPrice()
         │                    │                    │
         ▼                    ▼                    ▼
┌──────────────────┐ ┌──────────────────┐ ┌──────────────────┐
│   StoreManager   │ │   StoreManager   │ │   StoreManager   │
│      Mag1        │ │      Mag2        │ │      Mag3        │
└────────┬─────────┘ └────────┬─────────┘ └────────┬─────────┘
         │                    │                    │
    ┌────▼────┐           ┌───▼────┐           ┌──▼────┐
    │ Mag1.txt│           │Mag2.txt│           │Mag3.tx│
    └─────────┘           └────────┘           └───────┘

         ▲                    ▲                    ▲
         │                    │                    │
         └────────────┬───────┴────────┬──────────┘
                      │ comparaison    │
                      │ des prix       │
                      ▼                │
            ┌──────────────────────┐   │
            │   StoreClient        │◄──┘
            │  (Machine Client)    │
            │  Affiche résultat    │
            └──────────────────────┘
```

### Composants principaux

| Composant | Rôle | Machine |
|-----------|------|---------|
| **Store.java** | Interface RMI distante | - |
| **StoreManager** | Serveur gérant un magasin et ses prix | Mag1, Mag2, Mag3 |
| **StoreClient** | Client interrogeant les magasins | Machine Client |
| **RMI Registry** | Registre central pour la découverte de services | Port 1099 |

---

## 📁 Structure du projet

```
RMIProject/
├── src/
│   └── rmi/
│       ├── Store.java              # Interface RMI Remote
│       ├── StoreManager.java        # Implémentation serveur
│       └── StoreClient.java         # Client RMI
├── data/
│   ├── Mag1.txt                     # Données Magasin 1
│   ├── Mag2.txt                     # Données Magasin 2
│   └── Mag3.txt                     # Données Magasin 3
├── scripts/
│   ├── start-registry.bat           # Lance le RMI Registry
│   ├── start-mag1.bat               # Lance le serveur Mag1
│   ├── start-mag2.bat               # Lance le serveur Mag2
│   ├── start-mag3.bat               # Lance le serveur Mag3
│   └── start-client.bat             # Lance le client
├── bin/                             # Classes compilées (généré)
├── README.md                        # Ce fichier
└── .gitignore                       # Fichiers à ignorer

```

---

## 🔧 Prérequis

- **Java JDK 8+** (recommandé JDK 11 ou supérieur)
- **Compilateur javac** disponible dans le PATH
- **Système d'exploitation** : Windows, Linux, ou macOS
- **Accès à localhost:1099** (port RMI par défaut)

### Vérification de l'installation

```bash
java -version
javac -version
```

---

## 💻 Installation et compilation

### 1. Cloner le repository

```bash
git clone https://github.com/BrYassine98/RMIProject.git
cd RMIProject
```

### 2. Créer le répertoire bin

```bash
mkdir bin
```

### 3. Compiler les sources

**Windows (CMD/PowerShell) :**
```batch
javac -d bin src\rmi\*.java
```

**Linux/macOS :**
```bash
javac -d bin src/rmi/*.java
```

---

## 🚀 Guide d'utilisation

### Démarrage complet du système

Le système comprend 3 serveurs et 1 client. Suivez les étapes dans cet ordre :

#### **Étape 1 : Démarrer le premier serveur (Mag1)**

**Windows :**
```batch
scripts\start-mag1.bat
```

**Linux/macOS :**
```bash
cd bin
java -cp . rmi.StoreManager Mag1 ../data/Mag1.txt localhost 1099
```

Ce serveur crée automatiquement le RMI Registry.

#### **Étape 2 : Démarrer les serveurs Mag2 et Mag3**

**Windows :**
```batch
scripts\start-mag2.bat
scripts\start-mag3.bat
```

**Linux/macOS (dans des terminaux séparés) :**
```bash
cd bin
java -cp . rmi.StoreManager Mag2 ../data/Mag2.txt localhost 1099
java -cp . rmi.StoreManager Mag3 ../data/Mag3.txt localhost 1099
```

#### **Étape 3 : Lancer le client**

**Windows :**
```batch
scripts\start-client.bat
```

**Linux/macOS :**
```bash
cd bin
java -cp . rmi.StoreClient localhost 1099
```

### Menu du client

Une fois le client lancé, un menu interactif s'affiche :

```
--- Store Client ---
1. cornichons
2. safran
3. sel
4. poivre
0. Quitter
Choix:
```

**Entrez le numéro** de l'ingrédient que vous recherchez, puis appuyez sur **Entrée**.

Le système affichera le prix chez chaque magasin et indiquera le meilleur prix.

---

## 📊 Format des données

### Structure des fichiers de prix

Chaque fichier de magasin (Mag1.txt, Mag2.txt, Mag3.txt) contient une ligne par ingrédient :

```
ingredient prix
```

### Format détaillé

- **Ingrédient** : Nom en minuscules, sans espaces
- **Prix** : Nombre décimal (point comme séparateur)
- **Séparateur** : Espace ou tabulation
- **Commentaires** : Lignes commençant par `#` sont ignorées
- **Lignes vides** : Ignorées

### Exemple (Mag1.txt)

```
cornichons 10.0
safran 1000.0
sel 1.0
poivre 100.0
```

### Exemple (Mag3.txt)

```
cornichons 50
safran 6
sel 30
poivre 50
```

---

## 📚 Documentation des composants

### 1. Store.java - Interface RMI

Interface distante qui définit les services accessibles par RMI.

```java
package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Store extends Remote {
    double getPrice(String ingredient) throws RemoteException;
}
```

**Méthode :**
- `getPrice(String ingredient)` : Retourne le prix d'un ingrédient
  - **Paramètre** : nom de l'ingrédient
  - **Retour** : prix (double)
  - **Exception** : RemoteException si l'ingrédient n'existe pas

---

### 2. StoreManager.java - Serveur RMI

Implémente le service Store et gère les données des magasins.

**Fonctionnalités :**
- Charge les prix depuis un fichier au démarrage
- Implémente l'interface Store
- S'enregistre automatiquement dans le RMI Registry
- Crée le Registry si nécessaire

**Constructeur :**
```java
StoreManager(String storeName, String dataFile)
```

**Méthode principale :**
```java
public static void main(String[] args)
// args[0] : nom du magasin (Mag1, Mag2, Mag3)
// args[1] : chemin du fichier de données
// args[2] : hôte du registry (optionnel, défaut: localhost)
// args[3] : port du registry (optionnel, défaut: 1099)
```

**Exemple de sortie console :**
```
[Mag1] Données chargées depuis data/Mag1.txt
[Mag1] enregistré sur rmi://localhost:1099/Mag1
```

---

### 3. StoreClient.java - Client RMI

Client interactif qui interroge les magasins en parallèle.

**Fonctionnalités :**
- Menu interactif pour sélectionner un ingrédient
- Interrogation parallèle des 3 magasins
- Comparaison des prix
- Affichage du meilleur prix

**Méthode principale :**
```java
public static void main(String[] args)
// args[0] : hôte du registry (optionnel, défaut: localhost)
// args[1] : port du registry (optionnel, défaut: 1099)
```

**Processus de recherche :**
1. Lookup des serveurs Store dans le Registry
2. Soumission de tâches parallèles (une par magasin)
3. Récupération des résultats avec timeout
4. Comparaison et affichage

---

## 🔍 Dépannage

### ❌ "Connection refused" ou "Registre introuvable"

**Problème** : Le RMI Registry n'est pas accessible

**Solutions** :
1. Assurez-vous que le premier serveur (Mag1) a bien démarré en premier
2. Vérifiez que le port 1099 n'est pas occupé :
   ```bash
   netstat -an | findstr 1099  # Windows
   lsof -i :1099               # Linux/macOS
   ```
3. Redémarrez tous les serveurs

### ❌ "Ingrédient introuvable"

**Problème** : L'ingrédient n'existe pas dans le fichier de données

**Solutions** :
1. Vérifiez le fichier de données (Mag1.txt, Mag2.txt, Mag3.txt)
2. Assurez-vous que l'ingrédient est présent et bien formaté
3. Les noms d'ingrédients ne sont pas sensibles à la casse

### ❌ "ClassNotFoundException: rmi.Store"

**Problème** : Classes non compilées ou mauvaise structure de répertoires

**Solutions** :
```bash
# Recompiler
javac -d bin src/rmi/*.java

# Vérifier la structure
dir bin              # Vérifier que rmi/*.class existe
```

### ❌ Serveurs se ferment immédiatement

**Problème** : Erreur lors du chargement des données

**Solutions** :
1. Vérifiez le chemin des fichiers de données
2. Assurez-vous que les fichiers existent
3. Vérifiez le format des données (ingrédient + espace + prix)

### ❌ Client se termine sans afficher le menu

**Problème** : Impossible de se connecter aux serveurs

**Solutions** :
1. Vérifiez que les 3 serveurs sont en cours d'exécution
2. Utilisez les mêmes `host:port` partout
3. Vérifiez les pare-feu (firewall)

---

## 📝 Exemple de sortie

### Console du serveur Mag1

```
[Mag1] Données chargées depuis data/Mag1.txt
[Mag1] enregistré sur rmi://localhost:1099/Mag1
[Mag1] getPrice(safran) = 1000.0
[Mag1] getPrice(safran) = 1000.0
```

### Console du client

```
--- Store Client ---
1. cornichons
2. safran
3. sel
4. poivre
0. Quitter
Choix: 2

Recherche du prix pour: safran
- Mag1 : 1000.0
- Mag2 : 500
- Mag3 : 6
Prix le plus bas: 6 chez Mag3

--- Store Client ---
1. cornichons
2. safran
3. sel
4. poivre
0. Quitter
Choix: 0
```

---

## 📋 Fichiers de configuration

### start-mag1.bat
```batch
@echo off
setlocal
cd /d %~dp0.. 
javac -d bin src\rmi\*.java
start "Mag1" cmd /k "cd /d %~dp0.. && java -cp bin rmi.StoreManager Mag1 data\Mag1.txt localhost 1099"
```

### start-mag2.bat
```batch
@echo off
setlocal
cd /d %~dp0.. 
javac -d bin src\rmi\*.java
start "Mag2" cmd /k "cd /d %~dp0.. && java -cp bin rmi.StoreManager Mag2 data\Mag2.txt localhost 1099"
```

### start-mag3.bat
```batch
@echo off
setlocal
cd /d %~dp0.. 
javac -d bin src\rmi\*.java
start "Mag3" cmd /k "cd /d %~dp0.. && java -cp bin rmi.StoreManager Mag3 data\Mag3.txt localhost 1099"
```

### start-client.bat
```batch
@echo off
setlocal
cd /d %~dp0.. 
javac -d bin src\rmi\*.java
java -cp bin rmi.StoreClient localhost 1099
```

---

## 🔐 Notes de sécurité

Ce projet est à titre éducatif. Pour une utilisation en production :

- Utilisez des connexions sécurisées (SSL/TLS)
- Implémentez l'authentification et l'autorisation
- Utilisez un policy file pour les permissions RMI
- Validez toutes les entrées utilisateur
- Utilisez un registry RMI séparé et sécurisé

---

## 👨‍💻 Auteur et environnement

- **Classe** : MPGL1/S2
- **Enseignant** : Karim Ben romdhane
- **Année** : 2026
- **Langage** : Java
- **Technologie** : RMI (Remote Method Invocation)

---

## 📄 Licence

Ce projet est fourni à titre éducatif pour la matière **Informatique Répartie (RMI)**.

---

## ✅ Checklist avant de lancer

- [ ] Java JDK 8+ installé
- [ ] Répertoire `bin/` créé
- [ ] Fichiers source compilés (`javac -d bin src/rmi/*.java`)
- [ ] Fichiers de données présents dans `data/`
- [ ] Port 1099 disponible
- [ ] Tous les scripts bat/sh en place

---
