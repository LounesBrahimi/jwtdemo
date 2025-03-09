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

## 📖 Théorie : Comprendre JWT, Signature et Chiffrement

### **1️⃣ Qu’est-ce qu’un JWT ?**
Un JWT (**JSON Web Token**) est un jeton sécurisé utilisé pour **authentifier** un utilisateur ou échanger des informations de manière sécurisée. Il est composé de 3 parties :

```
header.payload.signature
```
- **Header** : Contient l’algorithme utilisé (`RS256` pour la signature, par exemple).
- **Payload** : Contient les données (`iss`, `sub`, `exp`, etc.).
- **Signature** : Garantit l’authenticité et l’intégrité du JWT.

### **2️⃣ Pourquoi signer un JWT (`RS256`) ?**
🔹 Pour s'assurer que le JWT **provient bien de l'émetteur** et qu'il **n'a pas été modifié**.
🔹 Utilisation d'une clé privée RSA pour signer, et d'une clé publique pour vérifier.

### **3️⃣ Pourquoi chiffrer un JWT (`RSA-OAEP + AES-GCM`) ?**
🔹 Un JWT signé **peut être lu par n'importe qui** (il n'est pas chiffré).
🔹 Le chiffrement permet de **protéger les données sensibles**.
🔹 On utilise **AES-GCM** pour chiffrer les données et **RSA-OAEP** pour chiffrer la clé AES.

### **4️⃣ Différence entre signature et chiffrement**
| **Méthode** | **Objectif** | **Lecture possible ?** |
|------------|-------------|------------------|
| **Signature (`RS256`)** | Vérifier l'origine et l'intégrité | ✅ Oui |
| **Chiffrement (`RSA-OAEP + AES-GCM`)** | Protéger le contenu | ❌ Non |

---

## 🔥 FAQ

### **Pourquoi éviter `java-jwt` ou `jjwt` ?**
📌 Ce projet montre **comment implémenter JWT sans dépendances externes**.
📌 Il utilise **les API Java standard (`java.security`, `javax.crypto`)** pour tout gérer.

### **Comment tester l’API sans `curl` ?**
📌 Ouvre Swagger UI : `http://localhost:8080/swagger-ui.html`.
📌 Fais des requêtes directement via l'interface web.

### **Est-ce sécurisé en production ?**
✅ Oui, mais **les clés RSA doivent être stockées de manière sécurisée**.
✅ **Utiliser un fournisseur de JWT sécurisé** en production (Keycloak, Auth0, etc.).

---

## 🚀 Conclusion
Cette API montre **deux méthodes d’authentification avec JWT** : la signature (`RS256`) et le chiffrement (`RSA-OAEP + AES-GCM`).

💡 **Veux-tu des améliorations ? N'hésite pas à contribuer !** 💡

---

## 📜 Licence
📌 Projet open-source sous licence MIT.
