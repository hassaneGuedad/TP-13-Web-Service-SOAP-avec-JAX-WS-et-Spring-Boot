# Exemples de requêtes SOAP - Banque WS

Ce fichier contient des exemples de requêtes SOAP prêtes à être testées dans SoapUI.

## 1. Créer un compte courant (createCompte)

**Scénario :** Créer un nouveau compte avec un solde initial et un type.

**Endpoint :** `http://localhost:8080/services/ws`

**Requête SOAP :**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ws="http://ws.demo.example.com/">
  <soapenv:Header/>
  <soapenv:Body>
    <ws:createCompte>
      <solde>2500.50</solde>
      <type>COURANT</type>
    </ws:createCompte>
  </soapenv:Body>
</soapenv:Envelope>
```

**Réponse réussie (HTTP 200) :**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <ns2:createCompteResponse xmlns:ns2="http://ws.demo.example.com/">
      <return>
        <id>1</id>
        <solde>2500.5</solde>
        <dateCreation>2025-11-15T15:45:32.123Z</dateCreation>
        <type>COURANT</type>
      </return>
    </ns2:createCompteResponse>
  </soap:Body>
</soap:Envelope>
```

---

## 2. Créer un compte d'épargne (createCompte - Variante)

**Scénario :** Créer un compte d'épargne avec solde plus élevé.

**Requête SOAP :**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ws="http://ws.demo.example.com/">
  <soapenv:Header/>
  <soapenv:Body>
    <ws:createCompte>
      <solde>10000.00</solde>
      <type>EPARGNE</type>
    </ws:createCompte>
  </soapenv:Body>
</soapenv:Envelope>
```

**Réponse réussie (HTTP 200) :**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <ns2:createCompteResponse xmlns:ns2="http://ws.demo.example.com/">
      <return>
        <id>2</id>
        <solde>10000.0</solde>
        <dateCreation>2025-11-15T15:46:00.456Z</dateCreation>
        <type>EPARGNE</type>
      </return>
    </ns2:createCompteResponse>
  </soap:Body>
</soap:Envelope>
```

---

## 3. Créer un compte livret (createCompte - Variante)

**Scénario :** Créer un compte livret (compte d'épargne particulier).

**Requête SOAP :**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ws="http://ws.demo.example.com/">
  <soapenv:Header/>
  <soapenv:Body>
    <ws:createCompte>
      <solde>5000.75</solde>
      <type>LIVRET</type>
    </ws:createCompte>
  </soapenv:Body>
</soapenv:Envelope>
```

**Réponse réussie (HTTP 200) :**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <ns2:createCompteResponse xmlns:ns2="http://ws.demo.example.com/">
      <return>
        <id>3</id>
        <solde>5000.75</solde>
        <dateCreation>2025-11-15T15:46:30.789Z</dateCreation>
        <type>LIVRET</type>
      </return>
    </ns2:createCompteResponse>
  </soap:Body>
</soap:Envelope>
```

---

## 4. Lister tous les comptes (getComptes)

**Scénario :** Récupérer la liste complète de tous les comptes créés.

**Endpoint :** `http://localhost:8080/services/ws`

**Requête SOAP :**
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

**Réponse réussie (HTTP 200) - Exemple avec 3 comptes :**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <ns2:getComptesResponse xmlns:ns2="http://ws.demo.example.com/">
      <return>
        <id>1</id>
        <solde>2500.5</solde>
        <dateCreation>2025-11-15T15:45:32.123Z</dateCreation>
        <type>COURANT</type>
      </return>
      <return>
        <id>2</id>
        <solde>10000.0</solde>
        <dateCreation>2025-11-15T15:46:00.456Z</dateCreation>
        <type>EPARGNE</type>
      </return>
      <return>
        <id>3</id>
        <solde>5000.75</solde>
        <dateCreation>2025-11-15T15:46:30.789Z</dateCreation>
        <type>LIVRET</type>
      </return>
    </ns2:getComptesResponse>
  </soap:Body>
</soap:Envelope>
```

**Réponse si aucun compte (liste vide) :**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <ns2:getComptesResponse xmlns:ns2="http://ws.demo.example.com/">
    </ns2:getComptesResponse>
  </soap:Body>
</soap:Envelope>
```

---

## 5. Récupérer un compte par ID (getCompteById)

**Scénario :** Récupérer les détails d'un compte spécifique par son ID.

**Endpoint :** `http://localhost:8080/services/ws`

**Requête SOAP - Récupérer compte ID=1 :**
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

**Réponse réussie (HTTP 200) :**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <ns2:getCompteByIdResponse xmlns:ns2="http://ws.demo.example.com/">
      <return>
        <id>1</id>
        <solde>2500.5</solde>
        <dateCreation>2025-11-15T15:45:32.123Z</dateCreation>
        <type>COURANT</type>
      </return>
    </ns2:getCompteByIdResponse>
  </soap:Body>
</soap:Envelope>
```

**Requête SOAP - Récupérer compte ID=2 :**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ws="http://ws.demo.example.com/">
  <soapenv:Header/>
  <soapenv:Body>
    <ws:getCompteById>
      <id>2</id>
    </ws:getCompteById>
  </soapenv:Body>
</soapenv:Envelope>
```

**Réponse réussie (HTTP 200) :**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <ns2:getCompteByIdResponse xmlns:ns2="http://ws.demo.example.com/">
      <return>
        <id>2</id>
        <solde>10000.0</solde>
        <dateCreation>2025-11-15T15:46:00.456Z</dateCreation>
        <type>EPARGNE</type>
      </return>
    </ns2:getCompteByIdResponse>
  </soap:Body>
</soap:Envelope>
```

**Requête SOAP - Récupérer compte inexistant (ID=999) :**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ws="http://ws.demo.example.com/">
  <soapenv:Header/>
  <soapenv:Body>
    <ws:getCompteById>
      <id>999</id>
    </ws:getCompteById>
  </soapenv:Body>
</soapenv:Envelope>
```

**Réponse (compte non trouvé) - HTTP 200 :**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <ns2:getCompteByIdResponse xmlns:ns2="http://ws.demo.example.com/">
      <return></return>
    </ns2:getCompteByIdResponse>
  </soap:Body>
</soap:Envelope>
```
(Remarque : la réponse contient un élément vide, indiquant `null`)

---

## 6. Supprimer un compte (deleteCompte)

**Scénario :** Supprimer un compte existant.

**Endpoint :** `http://localhost:8080/services/ws`

**Requête SOAP - Supprimer compte ID=1 :**
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

**Réponse réussie (HTTP 200) - Compte supprimé :**
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

**Requête SOAP - Supprimer compte inexistant (ID=999) :**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ws="http://ws.demo.example.com/">
  <soapenv:Header/>
  <soapenv:Body>
    <ws:deleteCompte>
      <id>999</id>
    </ws:deleteCompte>
  </soapenv:Body>
</soapenv:Envelope>
```

**Réponse (compte non trouvé) - HTTP 200 :**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <ns2:deleteCompteResponse xmlns:ns2="http://ws.demo.example.com/">
      <return>false</return>
    </ns2:deleteCompteResponse>
  </soap:Body>
</soap:Envelope>
```

---

## Plan de test complet (Scénario réaliste)

Copie/colle ces requêtes dans SoapUI dans cet ordre pour un test complet :

### Étape 1 : Création de comptes
1. Exécuter createCompte (COURANT, solde=2500.50) → ID=1
2. Exécuter createCompte (EPARGNE, solde=10000.00) → ID=2
3. Exécuter createCompte (LIVRET, solde=5000.75) → ID=3

### Étape 2 : Consultation
4. Exécuter getComptes → devrait lister 3 comptes
5. Exécuter getCompteById (id=1) → devrait retourner le compte COURANT
6. Exécuter getCompteById (id=2) → devrait retourner le compte EPARGNE

### Étape 3 : Suppression
7. Exécuter deleteCompte (id=1) → devrait retourner true
8. Exécuter deleteCompte (id=999) → devrait retourner false

### Étape 4 : Vérification
9. Exécuter getComptes → devrait lister 2 comptes (ID=2 et ID=3)
10. Exécuter getCompteById (id=1) → devrait retourner vide/null (supprimé)

---

## Notes importantes

- **Format de date :** Les dates sont retournées au format ISO 8601 (UTC) : `2025-11-15T15:45:32.123Z`
- **Types de compte valides :** COURANT, EPARGNE, LIVRET (case-sensitive)
- **Solde :** Nombre décimal (double), ex: 2500.50
- **ID :** Généré automatiquement (auto-increment) lors de la création
- **Content-Type :** Les requêtes utilisent `text/xml; charset=utf-8` (SOAP 1.1)

---

## Utilisation dans SoapUI

1. Copier la requête SOAP ci-dessus
2. Dans SoapUI, ouvrir une opération (ex: createCompte)
3. Coller la requête dans l'onglet **Request**
4. Adapter les paramètres si nécessaire
5. Cliquer sur **▶ Run** (ou Ctrl+Entrée)
6. Observer la réponse dans l'onglet **Response**

---

**Fin des exemples** ✅

