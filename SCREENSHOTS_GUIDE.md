# Guide des captures d'écran - Tests SoapUI

Ce guide indique quelles captures d'écran prendre et où les placer dans la documentation.

---

## 📸 Captures d'écran recommandées

### 1️⃣ Importation du WSDL dans SoapUI

**Où :** Section "Tests avec SoapUI" du README

**Étapes :**
1. Lancer SoapUI
2. Fichier → Importer WSDL
3. Sélectionner le fichier `wsdl.xml`
4. Cliquer sur OK
5. **CAPTURER** : L'écran montrant le projet créé avec l'arborescence

**Ce qui doit être visible :**
```
BanqueWS
├── BanqueWSSoapBinding
│   └── CompteSoapServicePort
│       ├── createCompte
│       ├── deleteCompte
│       ├── getCompteById
│       └── getComptes
```

**Nom du fichier :** `screenshot-01-import-wsdl.png`

---

### 2️⃣ Créer un compte (createCompte) - Requête

**Où :** Section "Tester chaque opération" du README

**Étapes :**
1. Double-cliquer sur `createCompte`
2. L'éditeur s'ouvre avec la requête SOAP
3. Modifier les valeurs :
   - solde: 2500.0
   - type: COURANT
4. **CAPTURER** : L'onglet Request avec les paramètres

**Ce qui doit être visible :**
- Onglet "Request" sélectionné
- Endpoint : `http://localhost:8080/services/ws`
- XML complet de la requête SOAP
- Bouton Run (▶)

**Nom du fichier :** `screenshot-02-create-compte-request.png`

---

### 3️⃣ Créer un compte (createCompte) - Réponse

**Où :** Section "Tester chaque opération" du README

**Étapes :**
1. Après avoir cliqué sur Run (étape précédente)
2. Observer l'onglet Response
3. **CAPTURER** : L'onglet Response avec la réponse

**Ce qui doit être visible :**
- Onglet "Response" sélectionné
- Statut HTTP : 200 OK ✅
- XML de la réponse avec :
  - id : 1 (auto-généré)
  - solde : 2500.0
  - dateCreation : (timestamp)
  - type : COURANT
- Barre de status verte (Success)

**Nom du fichier :** `screenshot-03-create-compte-response.png`

---

### 4️⃣ Créer un deuxième compte (createCompte - variante)

**Où :** Section "Tester chaque opération" du README (sous-section "Créer un deuxième compte")

**Étapes :**
1. Créer un nouveau compte avec :
   - solde: 10000.0
   - type: EPARGNE
2. Run
3. **CAPTURER** : La requête ET la réponse (peut être 2 captures ou 1 capture zoomée montrant les deux)

**Ce qui doit être visible :**
- Requête avec solde=10000.0, type=EPARGNE
- Réponse avec id=2

**Nom du fichier :** `screenshot-04-create-compte-epargne-response.png`

---

### 5️⃣ Lister tous les comptes (getComptes)

**Où :** Section "Tester chaque opération" du README (sous-section "Lister tous les comptes")

**Étapes :**
1. Double-cliquer sur `getComptes`
2. L'éditeur s'ouvre (pas de paramètres)
3. Run
4. **CAPTURER** : L'onglet Response

**Ce qui doit être visible :**
- Requête vide (juste l'enveloppe SOAP, pas de paramètres)
- Réponse affichant 2 comptes (id=1 COURANT et id=2 EPARGNE)
- Statut HTTP : 200 OK ✅

**Nom du fichier :** `screenshot-05-get-comptes-response.png`

---

### 6️⃣ Récupérer un compte par ID (getCompteById)

**Où :** Section "Tester chaque opération" du README (sous-section "Récupérer un compte spécifique")

**Étapes :**
1. Double-cliquer sur `getCompteById`
2. Modifier le paramètre : id = 1
3. Run
4. **CAPTURER** : L'onglet Request ET Response

**Ce qui doit être visible :**
- Requête avec <id>1</id>
- Réponse avec le compte id=1 uniquement

**Nom du fichier :** `screenshot-06-get-compte-by-id-response.png`

---

### 7️⃣ Supprimer un compte (deleteCompte)

**Où :** Section "Tester chaque opération" du README (sous-section "Supprimer un compte")

**Étapes :**
1. Double-cliquer sur `deleteCompte`
2. Modifier le paramètre : id = 1
3. Run
4. **CAPTURER** : L'onglet Response

**Ce qui doit être visible :**
- Requête avec <id>1</id>
- Réponse avec <return>true</return>
- Statut HTTP : 200 OK ✅
- Barre verte (succès)

**Nom du fichier :** `screenshot-07-delete-compte-response.png`

---

### 8️⃣ Vérifier la suppression (getComptes après deleteCompte)

**Où :** Section "Tester chaque opération" du README (sous-section "Vérifier la suppression")

**Étapes :**
1. Relancer getComptes
2. Run
3. **CAPTURER** : L'onglet Response

**Ce qui doit être visible :**
- Réponse avec 1 seul compte (id=2 EPARGNE)
- Le compte id=1 n'est plus dans la liste

**Nom du fichier :** `screenshot-08-get-comptes-after-delete.png`

---

### 9️⃣ Console H2 - Données en base (optionnel)

**Où :** Section "Accéder à la console H2" du README

**Étapes :**
1. Ouvrir navigateur : `http://localhost:8080/h2-console`
2. Remplir le formulaire de connexion
3. Cliquer sur "Connect"
4. Exécuter la requête : `SELECT * FROM COMPTE;`
5. **CAPTURER** : L'écran montrant la table COMPTE avec les données

**Ce qui doit être visible :**
- Connexion à `jdbc:h2:mem:testdb`
- Requête SQL : `SELECT * FROM COMPTE;`
- Résultats : tableau avec les colonnes (id, solde, date_creation, type)
- Les comptes restants (id=2 après la suppression)

**Nom du fichier :** `screenshot-09-h2-console-data.png`

---

### 🔟 Arborescence du projet SoapUI (optionnel)

**Où :** Section "Résultats après import" du README

**Étapes :**
1. Faire un zoom sur le panneau de gauche (Project Explorer)
2. Montrer l'arborescence complète
3. **CAPTURER** : Juste la partie gauche avec l'arborescence

**Ce qui doit être visible :**
- BanqueWS (nom du service)
- BanqueWSSoapBinding (binding SOAP)
- CompteSoapServicePort (port)
- Les 4 opérations : createCompte, deleteCompte, getCompteById, getComptes

**Nom du fichier :** `screenshot-10-project-tree.png`

---

## 📋 Checklist de captures

- [ ] Screenshot 1 : Importation du WSDL (arborescence du projet)
- [ ] Screenshot 2 : createCompte - Requête
- [ ] Screenshot 3 : createCompte - Réponse (id=1, COURANT)
- [ ] Screenshot 4 : createCompte deuxième compte (id=2, EPARGNE)
- [ ] Screenshot 5 : getComptes - Réponse (2 comptes)
- [ ] Screenshot 6 : getCompteById - Requête et Réponse (id=1)
- [ ] Screenshot 7 : deleteCompte - Réponse (true)
- [ ] Screenshot 8 : getComptes après deleteCompte (1 compte restant)
- [ ] Screenshot 9 : Console H2 - Données en base (optionnel)
- [ ] Screenshot 10 : Arborescence du projet (optionnel)

---

## 🎨 Conseils pour les captures

### Zoom et clarté
- Utiliser une résolution de 1920x1080 ou plus pour une meilleure lisibilité
- Zoomer sur SoapUI (Ctrl++ ou Cmd++) pour que le texte soit bien lisible
- Laisser assez d'espace blanc autour des zones importantes

### Annotations (optionnel mais recommandé)
- Utiliser des flèches rouges/jaunes pour pointer les éléments importants
- Encadrer le statut HTTP (200 OK)
- Surligner les valeurs importantes (id, solde, type)
- Utiliser un logiciel comme :
  - **Snagit** (payant, très complet)
  - **Greenshot** (gratuit, simple)
  - **Paint** (simple, gratuit, inclus dans Windows)

### Format et compression
- Enregistrer en PNG (lossless)
- Résolution minimale : 1280x720
- Taille fichier cible : 200-500 KB par capture
- Nommer clairement : `screenshot-XX-description.png`

### Placement dans le README
Insérer les captures avec :
```markdown
![Description](./screenshots/screenshot-01-import-wsdl.png)
```

---

## 📁 Structure de dossier recommandée

```
demo/
├── README.md
├── SOAP_EXAMPLES.md
├── SCREENSHOTS_GUIDE.md (ce fichier)
└── screenshots/
    ├── screenshot-01-import-wsdl.png
    ├── screenshot-02-create-compte-request.png
    ├── screenshot-03-create-compte-response.png
    ├── screenshot-04-create-compte-epargne-response.png
    ├── screenshot-05-get-comptes-response.png
    ├── screenshot-06-get-compte-by-id-response.png
    ├── screenshot-07-delete-compte-response.png
    ├── screenshot-08-get-comptes-after-delete.png
    ├── screenshot-09-h2-console-data.png (optionnel)
    └── screenshot-10-project-tree.png (optionnel)
```

---

## 🔍 Vérification avant de finaliser

Pour chaque capture, vérifier :
- ✅ Le texte SOAP/XML est bien lisible
- ✅ Le statut HTTP (200) est visible
- ✅ Les valeurs importantes (id, solde, type) sont clairement visibles
- ✅ La barre de status (verte = succès) est visible
- ✅ L'endpoint HTTP est visible (`http://localhost:8080/services/ws`)
- ✅ Pas de données sensibles (aucune n'existe ici, mais à vérifier)
- ✅ Pas d'erreurs ou de logs perturbateurs à l'écran

---

## 🎬 Scénario de test complet (pour les captures)

Pour un résultat optimal, suivre ce scénario sans relancer le serveur :

1. **Démarrer l'appli** → logs affichent "Tomcat started on port 8080"
2. **Importer le WSDL** → Screenshot 1
3. **Créer compte 1** (COURANT, 2500.0) → Screenshots 2-3
4. **Créer compte 2** (EPARGNE, 10000.0) → Screenshot 4
5. **Lister comptes** (getComptes) → Screenshot 5
6. **Récupérer compte 1** (getCompteById id=1) → Screenshot 6
7. **Supprimer compte 1** (deleteCompte id=1) → Screenshot 7
8. **Lister comptes** (getComptes) → Screenshot 8 (verif: 1 compte restant)
9. **Console H2** (optionnel) → Screenshot 9
10. **Arborescence projet** (optionnel) → Screenshot 10

Total : 8-10 captures pour un test complet documenté.

---

**Fin du guide** ✅

