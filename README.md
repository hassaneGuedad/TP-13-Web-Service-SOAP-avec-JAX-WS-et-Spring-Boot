# Banque WS - Service SOAP avec Spring Boot et Apache CXF

## 📋 Table des matières
1. [Description du projet](#description-du-projet)
2. [Technologies utilisées](#technologies-utilisées)
3. [Architecture](#architecture)
4. [Installation et démarrage](#installation-et-démarrage)
5. [Configuration](#configuration)
6. [Utilisation du service SOAP](#utilisation-du-service-soap)
7. [Tests avec SoapUI](#tests-avec-soapui)
8. [Endpoints SOAP](#endpoints-soap)
9. [Exemples de requêtes SOAP](#exemples-de-requêtes-soap)
10. [Dépannage](#dépannage)

---

## Description du projet

**Banque WS** est une application Spring Boot qui expose un service web SOAP permettant de gérer des comptes bancaires. Le service offre des opérations CRUD complètes :
- **Créer** un nouveau compte (solde + type)
- **Lister** tous les comptes
- **Récupérer** un compte par ID
- **Supprimer** un compte par ID

Le projet utilise :
- **Spring Boot 3.5.7** : framework principal
- **Apache CXF 4.0.3** : implémentation SOAP/WSDL
- **Spring Data JPA** : accès aux données
- **H2 Database** : base de données en mémoire (embeddable)
- **Lombok 1.18.34** : réduction du boilerplate Java
- **Maven** : gestion des dépendances et build

---

## Technologies utilisées

| Composant | Version | Rôle |
|-----------|---------|------|
| Java | 17 | Langage principal |
| Spring Boot | 3.5.7 | Framework web/configuration |
| Apache CXF | 4.0.3 | Stack SOAP/WSDL |
| Spring Data JPA | 3.x | ORM/persistance |
| Hibernate | 6.6.33 | Implémentation JPA |
| H2 Database | 2.3.232 | Base de données en mémoire |
| Lombok | 1.18.34 | Annotations Java |
| Maven | 3.9+ | Build automation |

---

## Architecture

```
demo/
├── src/main/java/com/example/demo/
│   ├── DemoApplication.java                (classe principale Spring Boot)
│   ├── config/
│   │   └── CxfConfig.java                  (configuration de l'endpoint SOAP)
│   ├── entities/
│   │   ├── Compte.java                     (entité JPA + JAXB)
│   │   └── TypeCompte.java                 (enum : COURANT, EPARGNE, LIVRET)
│   ├── repositories/
│   │   └── CompteRepository.java           (interface Spring Data JPA)
│   └── ws/
│       └── CompteSoapService.java          (service SOAP exposé)
├── src/main/resources/
│   ├── application.properties               (configuration Spring + CXF)
│   ├── static/
│   └── templates/
├── pom.xml                                   (dépendances Maven)
└── README.md                                 (ce fichier)
```

### Flux de requête SOAP

```
Client SOAP (ex: SoapUI)
        ↓
    HTTP POST
        ↓
http://localhost:8080/services/ws
        ↓
CXF Servlet (cxf.path=/services)
        ↓
EndpointImpl (publish(/services/ws))
        ↓
CompteSoapService (implémentation)
        ↓
CompteRepository (Spring Data JPA)
        ↓
H2 Database (jdbc:h2:mem:testdb)
        ↓
Réponse SOAP (XML)
```

---

## Installation et démarrage

### Prérequis
- **Java 17** ou supérieur (télécharger : https://adoptium.net/)
- **Maven 3.9+** (ou utiliser le wrapper `mvnw.cmd` fourni)
- **Git** (optionnel, pour cloner le projet)

### Étapes d'installation

1. **Cloner ou télécharger le projet**
   ```bash
   git clone <url-du-repo>
   cd demo
   ```

2. **Compiler le projet**
   ```bash
   mvnw.cmd -DskipTests package
   ```
   Ou avec Maven installé globalement :
   ```bash
   mvn -DskipTests package
   ```

3. **Lancer l'application**
   ```bash
   java -jar target/demo-0.0.1-SNAPSHOT.jar
   ```
   Ou depuis l'IDE (clic droit sur `DemoApplication.java` → Run).

4. **Vérifier le démarrage**
   ```
   Tomcat started on port 8080 (http) with context path '/'
   Started DemoApplication in X seconds
   ```

### Services disponibles après le démarrage

| Service | URL | Description |
|---------|-----|-------------|
| WSDL | http://localhost:8080/services/ws?wsdl | Définition du service SOAP |
| Endpoint SOAP | http://localhost:8080/services/ws | Point d'entrée pour les requêtes SOAP |
| Console H2 | http://localhost:8080/h2-console | Gestion interactive de la BD H2 |

---

## Configuration

### Fichier `application.properties`

```properties
# Nom de l'application
spring.application.name=demo

# H2 Database
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driverClassName=org.h2.Driver

# Hibernate auto ddl
spring.jpa.hibernate.ddl-auto=update

# Console H2
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Apache CXF servlet path
cxf.path=/services

# Logging pour debug CXF
logging.level.org.apache.cxf=DEBUG
logging.level.org.springframework.web=DEBUG
```

### Fichier `CxfConfig.java`

```java
@Configuration
public class CxfConfig {
    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, compteSoapService);
        endpoint.publish("/services/ws");
        endpoint.setPublishedEndpointUrl("http://localhost:8080/services/ws?wsdl");
        return endpoint;
    }
}
```

**Explication :**
- `cxf.path=/services` : tous les endpoints CXF sont préfixés par `/services`
- `endpoint.publish("/services/ws")` : expose le service à `/services/ws`
- `setPublishedEndpointUrl(...)` : force l'URL complète du WSDL pour éviter les imports cassés

---

## Utilisation du service SOAP

### Récupérer le WSDL

Pour obtenir la définition du service (fichier `.wsdl`), utilise :

```bash
# Avec curl (Windows 10+)
curl -v "http://localhost:8080/services/ws?wsdl" -o banque.wsdl

# Ou avec PowerShell
Invoke-WebRequest -Uri "http://localhost:8080/services/ws?wsdl" -OutFile banque.wsdl
```

Le fichier `banque.wsdl` contient :
- Les types de données (Compte, TypeCompte)
- Les 4 opérations disponibles
- Le binding SOAP et l'adresse de l'endpoint

### Valider le WSDL

```bash
# Vérifier que le fichier commence par <?xml (pas de BOM)
file banque.wsdl

# Ou avec PowerShell (affiche les 10 premières lignes)
Get-Content -Path .\banque.wsdl -TotalCount 10
```

---

## Tests avec SoapUI

### Installation de SoapUI

1. Télécharger SoapUI Community Edition : https://www.soapui.org/downloads/soapui/
2. Installer et lancer l'application.

### Importer le WSDL dans SoapUI

#### Méthode 1 : Import local (recommandé)

1. **Télécharger le WSDL** (voir section précédente)
2. Dans SoapUI :
   - Clic droit dans le panneau de gauche (Project Explorer)
   - Choisir **File → Import WSDL**
   - Sélectionner le fichier `banque.wsdl` téléchargé
   - Cliquer sur **OK**

#### Méthode 2 : Import depuis URL

1. Dans SoapUI :
   - File → New SOAP Project
   - Initial WSDL : `http://localhost:8080/services/ws?wsdl`
   - Cocher "Create Requests"
   - Cliquer sur **OK**

### Résultats après import

Tu devrais voir dans le Project Explorer :

```
BanqueWS (Service)
├── BanqueWSSoapBinding (Binding)
│   ├── CompteSoapServicePort (Port)
│   │   ├── createCompte (Request)
│   │   ├── deleteCompte (Request)
│   │   ├── getCompteById (Request)
│   │   └── getComptes (Request)
```

### Tester chaque opération

Chaque opération dispose d'une requête SOAP pré-générée par SoapUI. Voici comment envoyer une requête :

1. Double-clique sur une opération (ex: `createCompte`)
2. La requête SOAP s'ouvre dans l'éditeur principal
3. Modifie les paramètres si nécessaire
4. Clique sur le bouton **▶ Play** (ou appuie sur `Ctrl+Entrée`)
5. La réponse SOAP s'affiche en bas

---

## Endpoints SOAP

### 1. `createCompte` - Créer un compte

**Type :** POST
**URL :** `http://localhost:8080/services/ws`

**Paramètres :**
- `solde` (double) : solde initial du compte
- `type` (enum) : type de compte (COURANT, EPARGNE, LIVRET)

**Réponse :** Objet Compte créé avec ID affecté

**Cas d'usage :**
```
Entrée  : solde=5000.0, type=COURANT
Sortie  : Compte{id=1, solde=5000.0, dateCreation=2025-11-15T..., type=COURANT}
```

---

### 2. `getComptes` - Lister tous les comptes

**Type :** POST
**URL :** `http://localhost:8080/services/ws`

**Paramètres :** Aucun

**Réponse :** Liste de tous les comptes

**Cas d'usage :**
```
Entrée  : (aucun paramètre)
Sortie  : [Compte{id=1, ...}, Compte{id=2, ...}, ...]
```

---

### 3. `getCompteById` - Récupérer un compte par ID

**Type :** POST
**URL :** `http://localhost:8080/services/ws`

**Paramètres :**
- `id` (long) : ID du compte à récupérer

**Réponse :** Objet Compte ou null si non trouvé

**Cas d'usage :**
```
Entrée  : id=1
Sortie  : Compte{id=1, solde=5000.0, ...}
```

---

### 4. `deleteCompte` - Supprimer un compte

**Type :** POST
**URL :** `http://localhost:8080/services/ws`

**Paramètres :**
- `id` (long) : ID du compte à supprimer

**Réponse :** boolean (true si supprimé, false sinon)

**Cas d'usage :**
```
Entrée  : id=1
Sortie  : true (suppression réussie)
```

---

## Exemples de requêtes SOAP

### Créer un compte (createCompte)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ws="http://ws.demo.example.com/">
  <soapenv:Header/>
  <soapenv:Body>
    <ws:createCompte>
      <solde>1500.0</solde>
      <type>COURANT</type>
    </ws:createCompte>
  </soapenv:Body>
</soapenv:Envelope>
```

**Réponse attendue :**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <ns2:createCompteResponse xmlns:ns2="http://ws.demo.example.com/">
      <return>
        <id>1</id>
        <solde>1500.0</solde>
        <dateCreation>2025-11-15T10:30:00</dateCreation>
        <type>COURANT</type>
      </return>
    </ns2:createCompteResponse>
  </soap:Body>
</soap:Envelope>
```

---

### Lister tous les comptes (getComptes)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ws="http://ws.demo.example.com/">
  <soapenv:Header/>
  <soapenv:Body>
    <ws:getComptes/>
  </soapenv:Body>
</soapenv:Envelope>
```

**Réponse attendue :**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <ns2:getComptesResponse xmlns:ns2="http://ws.demo.example.com/">
      <return>
        <id>1</id>
        <solde>1500.0</solde>
        <dateCreation>2025-11-15T10:30:00</dateCreation>
        <type>COURANT</type>
      </return>
      <return>
        <id>2</id>
        <solde>3000.0</solde>
        <dateCreation>2025-11-15T10:35:00</dateCreation>
        <type>EPARGNE</type>
      </return>
    </ns2:getComptesResponse>
  </soap:Body>
</soap:Envelope>
```

---

### Récupérer un compte par ID (getCompteById)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ws="http://ws.demo.example.com/">
  <soapenv:Header/>
  <soapenv:Body>
    <ws:getCompteById>
      <id>1</id>
    </ws:getCompteById>
  </soapenv:Body>
</soapenv:Envelope>
```

**Réponse attendue :**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <ns2:getCompteByIdResponse xmlns:ns2="http://ws.demo.example.com/">
      <return>
        <id>1</id>
        <solde>1500.0</solde>
        <dateCreation>2025-11-15T10:30:00</dateCreation>
        <type>COURANT</type>
      </return>
    </ns2:getCompteByIdResponse>
  </soap:Body>
</soap:Envelope>
```

---

### Supprimer un compte (deleteCompte)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ws="http://ws.demo.example.com/">
  <soapenv:Header/>
  <soapenv:Body>
    <ws:deleteCompte>
      <id>1</id>
    </ws:deleteCompte>
  </soapenv:Body>
</soapenv:Envelope>
```

**Réponse attendue :**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <ns2:deleteCompteResponse xmlns:ns2="http://ws.demo.example.com/">
      <return>true</return>
    </ns2:deleteCompteResponse>
  </soap:Body>
</soap:Envelope>
```

---

## Tests avec SoapUI - Guide visuel

### Scénario de test complet

#### 1️⃣ Créer un compte (createCompte)

**Action :**
- Ouvrir la requête `createCompte`
- Modifier les paramètres :
  - `solde` : 2500.0
  - `type` : EPARGNE
- Cliquer sur **▶ Run** (ou Ctrl+Entrée)

**Résultat attendu :**
- Code HTTP : 200 OK
- Réponse : Compte créé avec ID (ex: id=1)
- Status bar : ✅ Vert (succès)

**Capture d'écran à prendre :**
```
┌─────────────────────────────────────┐
│ SoapUI - createCompte Request       │
├─────────────────────────────────────┤
│ [Endpoint: http://localhost:8080/..│
│                                     │
│ <ws:createCompte>                   │
│   <solde>2500.0</solde>             │
│   <type>EPARGNE</type>              │
│ </ws:createCompte>                  │
│                                     │
├─────────────────────────────────────┤
│ [Response - HTTP 200]               │
│ <ns2:createCompteResponse>          │
│   <return>                          │
│     <id>1</id>                      │
│     <solde>2500.0</solde>           │
│     <type>EPARGNE</type>            │
│   </return>                         │
│ </ns2:createCompteResponse>         │
│                                     │
│ ✅ Status: Success                  │
└─────────────────────────────────────┘
```

---

#### 2️⃣ Créer un deuxième compte

**Action :**
- Créer un nouveau compte avec :
  - `solde` : 5000.0
  - `type` : COURANT

**Résultat attendu :**
- Deuxième compte créé avec ID=2

**Capture d'écran :**
```
Même format que ci-dessus avec id=2, solde=5000.0, type=COURANT
```

---

#### 3️⃣ Lister tous les comptes (getComptes)

**Action :**
- Ouvrir la requête `getComptes`
- Cliquer sur **▶ Run**

**Résultat attendu :**
- Code HTTP : 200 OK
- Réponse : Liste contenant 2 comptes (id=1 et id=2)

**Capture d'écran :**
```
┌─────────────────────────────────────┐
│ SoapUI - getComptes Request         │
├─────────────────────────────────────┤
│ [Endpoint: http://localhost:8080/..│
│                                     │
│ <ws:getComptes/>                    │
│                                     │
├─────────────────────────────────────┤
│ [Response - HTTP 200]               │
│ <ns2:getComptesResponse>            │
│   <return>                          │
│     <id>1</id>                      │
│     <solde>2500.0</solde>           │
│     <type>EPARGNE</type>            │
│   </return>                         │
│   <return>                          │
│     <id>2</id>                      │
│     <solde>5000.0</solde>           │
│     <type>COURANT</type>            │
│   </return>                         │
│ </ns2:getComptesResponse>           │
│                                     │
│ ✅ Status: Success                  │
└─────────────────────────────────────┘
```

---

#### 4️⃣ Récupérer un compte spécifique (getCompteById)

**Action :**
- Ouvrir la requête `getCompteById`
- Définir `id` : 1
- Cliquer sur **▶ Run**

**Résultat attendu :**
- Code HTTP : 200 OK
- Réponse : Compte avec id=1

**Capture d'écran :**
```
Requête avec <id>1</id>
Réponse avec le compte id=1 (solde=2500.0, type=EPARGNE)
```

---

#### 5️⃣ Supprimer un compte (deleteCompte)

**Action :**
- Ouvrir la requête `deleteCompte`
- Définir `id` : 1
- Cliquer sur **▶ Run**

**Résultat attendu :**
- Code HTTP : 200 OK
- Réponse : `<return>true</return>` (suppression réussie)

**Capture d'écran :**
```
┌─────────────────────────────────────┐
│ SoapUI - deleteCompte Request       │
├─────────────────────────────────────┤
│ [Endpoint: http://localhost:8080/..│
│                                     │
│ <ws:deleteCompte>                   │
│   <id>1</id>                        │
│ </ws:deleteCompte>                  │
│                                     │
├─────────────────────────────────────┤
│ [Response - HTTP 200]               │
│ <ns2:deleteCompteResponse>          │
│   <return>true</return>             │
│ </ns2:deleteCompteResponse>         │
│                                     │
│ ✅ Status: Success                  │
└─────────────────────────────────────┘
```

---

#### 6️⃣ Vérifier la suppression (getComptes)

**Action :**
- Relancer `getComptes` pour vérifier que le compte id=1 a bien été supprimé

**Résultat attendu :**
- Code HTTP : 200 OK
- Réponse : Liste contenant 1 seul compte (id=2)

**Capture d'écran :**
```
Réponse getComptes avec uniquement le compte id=2 restant
```

---

## Dépannage

### ❌ Erreur : "No service was found"

**Cause :** L'URL sans `?wsdl` renvoie une page HTML.  
**Solution :**
- Utiliser l'URL complète : `http://localhost:8080/services/ws?wsdl`
- Ou importer le fichier WSDL local dans SoapUI

---

### ❌ Erreur : "Content is not allowed in prolog"

**Cause :** SoapUI a reçu du HTML au lieu du WSDL.  
**Solution :**
1. Télécharger le WSDL : `curl -v "http://localhost:8080/services/ws?wsdl" -o wsdl.xml`
2. Importer le fichier local dans SoapUI : **File → Import WSDL**
3. Vérifier que `soap:address` contient l'URL correcte (avec `?wsdl`)

---

### ❌ Erreur : "Cannot find database driver"

**Cause :** Dépendance H2 manquante.  
**Solution :** Assurer que `pom.xml` contient :
```xml
<dependency>
  <groupId>com.h2database</groupId>
  <artifactId>h2</artifactId>
</dependency>
```
Puis relancer le build.

---

### ❌ Erreur : "ExceptionInInitializerError: com.sun.tools.javac.code.TypeTag"

**Cause :** Flags de compilation incompatibles avec Java 17.  
**Solution :** Simplifier le `maven-compiler-plugin` dans `pom.xml` en supprimant les options `-J--add-exports`.

---

### ❌ Erreur : Réponse vide ou timeout

**Cause :** L'application n'est pas démarrée ou écoute sur un port différent.  
**Solution :**
1. Vérifier que Spring Boot démarre : `java -jar target/demo-0.0.1-SNAPSHOT.jar`
2. Vérifier le port (par défaut 8080) : chercher dans les logs `"Tomcat started on port"`
3. Tester l'accessibilité : `curl http://localhost:8080/`

---

### ✅ Accéder à la console H2

Pour consulter la base de données en temps réel :

1. Ouvrir le navigateur : `http://localhost:8080/h2-console`
2. Remplir le formulaire :
   - **Driver Class** : `org.h2.Driver`
   - **JDBC URL** : `jdbc:h2:mem:testdb`
   - **User Name** : `sa`
   - **Password** : (laisser vide)
3. Cliquer sur **Connect**
4. Exécuter des requêtes SQL : `SELECT * FROM COMPTE;`

---

## Structure de la base de données

### Table `COMPTE`

| Colonne | Type | Nullable | Description |
|---------|------|----------|-------------|
| id | BIGINT | NO | Clé primaire (auto-increment) |
| solde | DOUBLE | NO | Solde du compte |
| date_creation | TIMESTAMP | YES | Date/heure de création |
| type | VARCHAR(20) | YES | Type de compte (COURANT, EPARGNE, LIVRET) |

**Schéma SQL généré :**
```sql
CREATE TABLE COMPTE (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  solde DOUBLE NOT NULL,
  date_creation TIMESTAMP,
  type VARCHAR(20)
);
```

---

## Checklist de test complet

- [ ] Application démarre sur le port 8080
- [ ] URL `http://localhost:8080/services/ws?wsdl` renvoie un WSDL valide
- [ ] WSDL importé correctement dans SoapUI
- [ ] createCompte : créer 2-3 comptes de différents types
- [ ] getComptes : lister et vérifier les comptes créés
- [ ] getCompteById : récupérer chaque compte individuellement
- [ ] deleteCompte : supprimer un compte et vérifier la suppression
- [ ] Console H2 : vérifier les données dans la table COMPTE
- [ ] Logs applicatifs : aucune exception ni warning critique

---

## Support et contributions

### Problèmes/Bugs
- Consulter la section [Dépannage](#dépannage)
- Vérifier les logs applicatifs : `java -jar target/demo-0.0.1-SNAPSHOT.jar 2>&1 | tee app.log`

### Améliorations futures
- [ ] Ajouter authentification/autorisation (JWT, OAuth2)
- [ ] Ajouter validation des données (annotations `@Validated`)
- [ ] Ajouter logging structuré (SLF4J, Logback)
- [ ] Ajouter tests unitaires (JUnit 5, Mockito)
- [ ] Ajouter CI/CD (GitHub Actions, Jenkins)
- [ ] Migrer vers une BD persistante (PostgreSQL, MySQL)

---

## Fichiers clés

| Fichier | Rôle |
|---------|------|
| `CxfConfig.java` | Configuration de l'endpoint SOAP |
| `CompteSoapService.java` | Implémentation des 4 opérations |
| `Compte.java` | Entité JPA + JAXB |
| `CompteRepository.java` | Accès données (Spring Data JPA) |
| `application.properties` | Configuration Spring Boot |
| `pom.xml` | Dépendances Maven |

---

## Liens utiles

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Apache CXF Documentation](https://cxf.apache.org/)
- [SoapUI Documentation](https://www.soapui.org/docs)
- [H2 Database Console](http://www.h2database.com/html/main.html)
- [Jakarta XML Web Services (JAX-WS)](https://jakarta.ee/specifications/xml-web-services/)

---

## Licence

Ce projet est fourni à titre d'exemple éducatif.

---

## Auteur

Projet démonstratif - Service SOAP avec Spring Boot et Apache CXF  
Date : Novembre 2025

---

## Captures d'écran - Résumé

<img width="950" height="505" alt="caap1_off" src="https://github.com/user-attachments/assets/40837aee-856d-4738-ada3-91a657038609" />

<img width="950" height="505" alt="caap2" src="https://github.com/user-attachments/assets/649c4870-64d7-43cc-9b34-67424e2d5c59" />

<img width="950" height="505" alt="caaap3" src="https://github.com/user-attachments/assets/449d60b3-66d6-444f-ae9a-e8420c106456" />

