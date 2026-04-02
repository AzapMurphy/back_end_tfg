# 🛒 Proyecto Web - Buscador de Productos

Aplicación web desarrollada con:

* ⚙️ Backend: Spring Boot
* 🌐 Frontend: Angular
* 🔐 Autenticación: Firebase

Permite autenticación de usuarios y consulta/scraping de productos desde el backend.

---

## 🚀 Cómo ejecutar el proyecto

### 1️⃣ Clonar repositorio

```bash
git clone <URL_DEL_REPOSITORIO>
cd <NOMBRE_PROYECTO>
```

---

## 🔥 Configuración IMPORTANTE (Firebase)

⚠️ **Este paso es obligatorio para que el backend funcione correctamente**

Por motivos de seguridad, el archivo de credenciales de Firebase **NO está incluido en el repositorio**.

### 📥 Pasos:

1. Estamos en los momentos iniciales del proyecto, solo se puede ver de forma local
2. Con la Documentación del proyecto, enviamos un archivo de configuración que abajo indicamos donde debe ponerse
---

### 📂 Ubicación del archivo

Renombrar el archivo descargado como:


```

Y colocarlo en:

```bash
src/main/resources/firebase-service-account.json
```

---

## ▶️ Ejecutar backend (Spring Boot)

```bash
src/main/java/com/webscrap/WebscrapApplication.java: run
```

o desde tu IDE (IntelliJ, Eclipse, etc.)

---

## ▶️ Ejecutar frontend (Angular)

```bash
cd frontend
npm install
npm run start
```

Abrir en el navegador:

```bash
http://localhost:4200
```

---

## 🔐 Autenticación

El sistema utiliza Firebase Authentication.

Es necesario:

* Tener usuarios creados en Firebase
* Iniciar sesión desde la aplicación

---

## ⚠️ Notas importantes

* El archivo `firebase-service-account.json` no se incluye por seguridad
* Sin este archivo, el backend devolverá errores **403 / 401**
* Asegúrate de que el `project_id` coincide con el del frontend

---

## 🧩 Tecnologías usadas

* Angular
* Spring Boot
* Firebase Authentication
* REST API

---

## 👨‍💻 Autor

Proyecto desarrollado con fines educativos.
