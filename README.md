# ✈️ Treep - Gestionnaire de Voyages

Application de gestion de voyages avec interface JavaFX et backend Spring Boot, permettant de planifier, organiser et partager vos aventures.

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-green?logo=springboot)
![JavaFX](https://img.shields.io/badge/JavaFX-21-blue?logo=java)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?logo=docker)

## 📋 Table des matières

- [Fonctionnalités](#-fonctionnalités)
- [Architecture](#-architecture)
- [Prérequis](#-prérequis)
- [Installation](#-installation)
- [Utilisation](#-utilisation)
- [Structure du projet](#-structure-du-projet)
- [Technologies](#-technologies)
- [API](#-api)
- [Contribuer](#-contribuer)

## ✨ Fonctionnalités

### 🔐 Authentification
- Inscription et connexion sécurisées
- Hachage des mots de passe avec BCrypt
- Gestion des sessions utilisateur

### 🗺️ Gestion des voyages
- **Création de voyages** avec destination, dates et budget
- **Voyages privés/publics** - Contrôlez la visibilité de vos voyages
- **Activités** - Planifiez votre programme jour par jour
- **Suppression** - Uniquement pour les propriétaires

### ⭐ Système de favoris
- Marquez vos voyages préférés d'une étoile
- Accès rapide à vos favoris dans "Mes voyages"
- Synchronisation en temps réel

### 🎨 Interface utilisateur
- Design moderne avec BootstrapFX
- Animations fluides au survol
- Badges visuels (Privé 🔒, Auteur)
- Recherche instantanée de voyages

## 🏗️ Architecture

```
Treep/
├── backend/          # API REST Spring Boot
│   ├── controller/   # Endpoints REST
│   ├── model/        # Entités JPA
│   ├── repository/   # Accès données
│   ├── service/      # Logique métier
│   └── config/       # Configuration (Security, etc.)
│
├── frontend/         # Application JavaFX
│   ├── controller/   # Contrôleurs FXML
│   ├── model/        # Modèles de données
│   ├── service/      # Services API
│   ├── resources/
│   │   ├── fxml/     # Interfaces utilisateur
│   │   └── css/      # Styles personnalisés
│   └── App.java      # Point d'entrée
│
└── docker-compose.yaml
```

### Stack technique

**Backend**
- **Framework** : Spring Boot 3.5.7
- **Base de données** : PostgreSQL 15
- **ORM** : Spring Data JPA / Hibernate
- **Sécurité** : BCrypt pour les mots de passe
- **Conteneurisation** : Docker

**Frontend**
- **UI** : JavaFX 21
- **Styling** : BootstrapFX 0.4.0
- **HTTP** : HttpURLConnection natif
- **Sérialisation** : Jackson 2.15.2

## 📦 Prérequis

- **Java 21** (JDK)
- **Maven 3.6+**
- **Docker** et **Docker Compose**
- **Git**

## 🚀 Installation

### 1. Cloner le projet

```bash
git clone https://github.com/NathanaelCanet/Treep.git
cd Treep
```

### 2. Lancer le backend (Docker)

```bash
docker-compose up --build
```

Cela démarre :
- PostgreSQL sur le port `5432`
- API Spring Boot sur le port `8080`

### 3. Lancer le frontend (JavaFX)

```bash
cd frontend
./mvnw javafx:run
```

**Windows** :
```powershell
cd frontend
.\mvnw.cmd javafx:run
```

## 💻 Utilisation

### Connexion

Comptes de test disponibles :

| Login | Mot de passe | Rôle |
|-------|--------------|------|
| `admin` | `admin` | ADMIN |
| `user` | `user` | USER |

### Créer un voyage

1. Cliquez sur **"Créer un voyage"**
2. Renseignez :
   - Destination
   - Dates (début et fin)
   - Budget total
   - Visibilité (Privé/Public)
3. Ajoutez des activités
4. Sauvegardez

### Gérer les favoris

- Cliquez sur l'étoile ☆ pour ajouter aux favoris
- L'étoile devient jaune ★ quand le voyage est favori
- Retrouvez vos favoris dans **"Mes voyages"**

## 📁 Structure du projet

### Backend

```
backend/src/main/java/com/treep/backend/
├── controller/
│   ├── UserController.java      # Authentification, favoris
│   ├── TripController.java      # CRUD voyages
│   └── ActivityController.java  # CRUD activités
├── model/
│   ├── User.java                # Utilisateur + favoris
│   ├── Trip.java                # Voyage
│   └── Activity.java            # Activité
├── repository/
│   ├── UserRepository.java
│   ├── TripRepository.java
│   └── ActivityRepository.java
├── service/
│   └── PasswordService.java     # Hachage BCrypt
└── config/
    └── SecurityConfig.java      # Configuration sécurité
```

### Frontend

```
frontend/src/main/
├── java/com/treep/frontend/
│   ├── controller/
│   │   ├── AuthController.java       # Connexion/Inscription
│   │   ├── HubController.java        # Vue principale
│   │   ├── TripCardController.java   # Carte de voyage
│   │   └── DashboardController.java  # Création voyage
│   ├── model/
│   │   ├── User.java
│   │   ├── Trip.java
│   │   └── Activity.java
│   ├── service/
│   │   ├── ApiClientServices.java    # Client HTTP
│   │   └── AuthService.java          # Session utilisateur
│   └── App.java
└── resources/
    ├── fxml/
    │   ├── auth-view.fxml
    │   ├── hub-view.fxml
    │   ├── trip-card.fxml
    │   └── dashboard-view.fxml
    └── css/
        └── styles.css
```

## 🛠️ Technologies

### Backend
- **Spring Boot** 3.5.7 - Framework Java
- **Spring Data JPA** - ORM
- **Spring Security** - Sécurité (BCrypt)
- **PostgreSQL** 15 - Base de données
- **Lombok** - Réduction boilerplate
- **Docker** - Conteneurisation

### Frontend
- **JavaFX** 21 - Interface graphique
- **BootstrapFX** 0.4.0 - Composants UI
- **Jackson** 2.15.2 - JSON
- **Maven** - Build

## 🌐 API

### Authentification

```http
POST /api/users/login
Content-Type: application/json

{
  "login": "user",
  "password": "user"
}
```

### Voyages

```http
# Récupérer tous les voyages
GET /api/trips

# Créer un voyage
POST /api/trips
Content-Type: application/json

{
  "destination": "Paris",
  "dateDebut": "2025-06-01",
  "dateFin": "2025-06-07",
  "budgetTotal": 1500.0,
  "isPrivate": false,
  "user": { "id": 1 }
}

# Supprimer un voyage
DELETE /api/trips/{id}
```

### Favoris

```http
# Ajouter aux favoris
POST /api/users/{userId}/favorites/{tripId}

# Retirer des favoris
DELETE /api/users/{userId}/favorites/{tripId}

# Lister les favoris
GET /api/users/{userId}/favorites
```

## 🤝 Contribuer

1. Fork le projet
2. Créez une branche (`git checkout -b feature/AmazingFeature`)
3. Commit vos changements (`git commit -m 'feat: Add AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrez une Pull Request

### Conventions de commit

- `feat:` Nouvelle fonctionnalité
- `fix:` Correction de bug
- `style:` Changements de style
- `refactor:` Refactoring
- `docs:` Documentation

## 📝 Licence

Ce projet est sous licence MIT.

## 👥 Auteurs

- **Nathanael Canet** - Développeur principal

---

**Bon voyage avec Treep ! ✈️🌍**