# Projet : API Spring Boot - Signature & Chiffrement JWT

## 📌 Introduction
Ce projet est une **API REST Spring Boot** qui démontre **deux aspects clés des JWT** :
1. **Signature des JWT avec RSA (`RS256`)** → Garantit l'authenticité et l'intégrité du JWT.
2. **Chiffrement des JWT avec RSA-OAEP + AES (`AES-GCM`)** → Protège le contenu du JWT contre la lecture.

L'objectif est de montrer comment utiliser **les JWT de manière sécurisée**, en évitant les bibliothèques externes.

---

## 🚀 Fonctionnalités
✅ Génération d’un **JWT signé** avec `RS256`

✅ Vérification d’un JWT signé

✅ **Chiffrement d’un JWT** avec `RSA-OAEP + AES-GCM`

✅ **Déchiffrement d’un JWT**

✅ API REST accessible via **Swagger** (`http://localhost:8080/swagger-ui.html`)

---

## 🛠️ Installation

### **1️⃣ Prérequis**
- Java 17+
- Maven 3+

### **2️⃣ Cloner le projet**
```sh
git clone <URL_DU_REPO_GITLAB>
cd jwt-demo
```

### **3️⃣ Construire et exécuter l'application**
```sh
mvn clean install
mvn spring-boot:run
```

L'API sera disponible sur : `http://localhost:8080`

---

## 📌 Utilisation de l'API

### **1️⃣ Générer un JWT signé**
- **Endpoint :** `POST /jwt/sign`
- **Paramètre :** `user` (nom d'utilisateur)
- **Exemple de requête :**
```sh
curl -X POST "http://localhost:8080/jwt/sign?user=Alice"
```
- **Réponse (JWT signé) :**
```json
"eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJBbGljZSIsImlzcyI6Ik15QXBwIiw...
```

### **2️⃣ Vérifier un JWT signé**
- **Endpoint :** `POST /jwt/verify`
- **Paramètre :** `token` (JWT à vérifier)
- **Exemple :**
```sh
curl -X POST "http://localhost:8080/jwt/verify?token=<VOTRE_JWT>"
```
- **Réponse :** `true` (valide) ou `false` (invalide)

### **3️⃣ Chiffrer un JWT**
- **Endpoint :** `POST /jwt/encrypt`
- **Paramètre :** `data` (données à chiffrer)
- **Exemple :**
```sh
curl -X POST "http://localhost:8080/jwt/encrypt?data=HelloWorld"
```
- **Réponse (JWT chiffré) :**
```json
"ZklDQyQhQS..."
```

### **4️⃣ Déchiffrer un JWT**
- **Endpoint :** `POST /jwt/decrypt`
- **Paramètre :** `token` (JWT chiffré)
- **Exemple :**
```sh
curl -X POST "http://localhost:8080/jwt/decrypt?token=<VOTRE_JWT_CHIFFRE>"
```
- **Réponse :**
```json
"HelloWorld"
```

---

# 📖 Comprendre JWT, Signature et Chiffrement

## **1️⃣ Qu’est-ce qu’un JWT ?**
Un **JSON Web Token (JWT)** est un standard ouvert permettant l’échange sécurisé d’informations entre deux parties sous forme de **chaîne encodée en JSON**.

Un JWT est constitué de **trois parties distinctes** :
```
header.payload.signature
```
Chaque partie est encodée en **Base64 URL-safe**, ce qui signifie que le JWT peut être transmis via HTTP sans problème.

📌 **Exemple d’un JWT brut :**
```
eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.
eyJzdWIiOiJBbGljZSIsImlzcyI6Ik15QXBwIiwgImV4cCI6MTcwMDAwMDB9.
QXplRXh0cmF...c2lnbmF0dXJl
```

📌 **Exemple après décomposition :**
```json
// En-tête (Header)
{
  "alg": "RS256",
  "typ": "JWT"
}

// Charge utile (Payload)
{
  "iss": "MyApp",
  "sub": "Alice",
  "exp": 1741195234
}

// Signature (Signature encodée en Base64)
"QXplRXh0cmF...c2lnbmF0dXJl"
```

Chaque partie a un rôle spécifique :
- **Header** : Contient les métadonnées, comme l’algorithme utilisé (`RS256`).
- **Payload** : Contient les **claims** (informations comme `iss` (issuer), `sub` (subject), et `exp` (expiration)).
- **Signature** : Permet de garantir que le JWT **n’a pas été modifié**.

🔹 **Un JWT signé est vérifiable mais reste lisible. Il n'est pas chiffré !**

---

## **2️⃣ Signature numérique d’un JWT (`RS256`)**
La signature numérique permet de **garantir l’authenticité** et **l’intégrité** du JWT.

### **📌 Pourquoi signer un JWT ?**
✅ Vérifier que le JWT **provient bien d’une source fiable**.

✅ Empêcher toute **modification frauduleuse** du contenu.

### **📌 Comment fonctionne la signature (`RS256`) ?**
1️⃣ Le serveur crée un JWT et le **signe** avec une **clé privée RSA**.

2️⃣ Le client reçoit ce JWT et l’envoie à un serveur distant.

3️⃣ Ce serveur distant **vérifie la signature** avec la **clé publique**.

📌 **Illustration du processus de signature JWT (`RS256`)** :
```
[Serveur] → Génère JWT + Signature (Clé privée RSA) → [Client]
[API distante] → Vérifie Signature (Clé publique RSA) → Valide JWT
```

🔹 **Si un attaquant modifie le payload, la signature ne correspondra plus et le JWT sera rejeté.**

---

## **3️⃣ Chiffrement d’un JWT (`RSA-OAEP + AES-GCM`)**
Contrairement à la signature, le chiffrement **rend le contenu du JWT illisible**.

### **📌 Pourquoi chiffrer un JWT ?**
🔒 Empêcher un attaquant **de lire** les informations contenues dans le JWT.

🔒 Protéger **des données sensibles** (ex: email, ID de session, rôles).

### **📌 Comment fonctionne le chiffrement ?**
- **AES-GCM** est utilisé pour chiffrer le **contenu du JWT**.
- **RSA-OAEP** est utilisé pour chiffrer **la clé AES** (transmission sécurisée).
- Seul le serveur destinataire peut **déchiffrer la clé AES avec RSA** et lire le JWT.

📌 **Illustration du chiffrement JWT (`RSA-OAEP + AES-GCM`)** :
```
[Serveur] → Chiffrement (AES-GCM) + Protection (RSA-OAEP) → [Client]
[API distante] → Déchiffrement (RSA-OAEP + AES) → Utilisation du JWT
```

🔹 **Même si un attaquant intercepte le JWT, il ne pourra pas le lire sans la clé privée.**

---

## **4️⃣ Différence entre Signature et Chiffrement**
| **Méthode** | **Objectif** | **Lecture possible ?** |
|------------|-------------|------------------|
| **Signature (`RS256`)** | Vérifier l'origine et l'intégrité | ✅ Oui |
| **Chiffrement (`RSA-OAEP + AES-GCM`)** | Protéger le contenu | ❌ Non |

📌 **Résumé :**
- Un JWT **signé** prouve son authenticité mais peut être **lu** par n'importe qui.
- Un JWT **chiffré** est totalement **incompréhensible** sans la clé de déchiffrement.

---

## **5️⃣ Pourquoi éviter `HS256` et préférer `RS256` ?**
`HS256` (HMAC-SHA256) est un algorithme **symétrique** (une seule clé secrète pour signer et vérifier). Cela signifie que **le même secret doit être partagé entre l’émetteur et le vérificateur**, ce qui **augmente les risques de compromission**.

✅ **`RS256` (RSA-SHA256) est préférable** car il utilise **une paire de clés publique/privée**.

📌 **Différence entre `HS256` et `RS256` :**
| **Algorithme** | **Type de clé** | **Sécurité** |
|--------------|---------------|------------|
| `HS256` | Clé secrète unique | ❌ Risque de fuite de clé |
| `RS256` | Clé privée (signature) + Clé publique (vérification) | ✅ Sécurisé |

🔹 **Si un attaquant récupère la clé secrète d’un JWT signé en `HS256`, il peut générer des JWT frauduleux. Ce problème n'existe pas avec `RS256` car la clé privée n’est jamais partagée.**

---

## **6️⃣ Exemple concret : JWT en action**

📌 **Étape 1 : Le serveur génère un JWT signé**
```json
{
  "alg": "RS256",
  "typ": "JWT"
}.
{
  "iss": "MyApp",
  "sub": "Alice",
  "exp": 1741195234
}.
"signature_rsa"
```

📌 **Étape 2 : L’API vérifie la signature avec la clé publique**
```java
Signature verifier = Signature.getInstance("SHA256withRSA");
verifier.initVerify(publicKey);
verifier.update((header + "." + payload).getBytes(StandardCharsets.UTF_8));
boolean isValid = verifier.verify(signature);
```

📌 **Étape 3 : Si le JWT est valide, l’API accorde l’accès**

🚀 **Avec ces techniques, les JWT deviennent une solution fiable et sécurisée pour l’authentification !**

---

## 📜 Licence
📌 Projet open-source sous licence MIT.
