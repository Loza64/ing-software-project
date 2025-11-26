# 🕐 REHOSAR - Registro de Horas Sociales y Remuneradas

Sistema web desarrollado para el Departamento de Informática (DEI) con el objetivo de digitalizar y automatizar el registro, validación y generación de hojas de horas sociales y remuneradas para estudiantes e instructores.

---

## 📌 Descripción del Proyecto

Actualmente, los registros de horas sociales y remuneradas en el DEI se realizan de forma manual mediante hojas físicas, lo que dificulta la trazabilidad, validación y resguardo de la información. REHOSAR resuelve este problema permitiendo:

- Registro digital de horas.
- Asociación de actividades predefinidas.
- Validación de formularios por encargados.
- Firma digital ligera como constancia electrónica.
- Generación de hojas PDF para impresión y firma física.

El sistema está dividido en frontend y backend, siguiendo una arquitectura en N Capas y desplegado en la nube.

---

## ⚙️ Funcionalidades Principales

- 📥 Registro de horas con datos como fecha, hora de inicio y fin, actividad y aula.
- 📚 Actividades clasificadas como sociales o remuneradas.
- 📅 Agrupación de registros por formularios semanales.
- ✍️ Firma virtual automática al momento de guardar un registro.
- ✅ Validación y firma digital ligera por parte del encargado.
- 🖨️ Exportación de formulario en formato PDF para impresión.
- 🔐 Control de acceso por roles (ENCARGADO, INSTRUTORES REMUNERADOS Y SOCIALES).
- 📊 Listado de formularios por usuario, estado o período.

---

## 🧱 Arquitectura del Proyecto

Proyecto desarrollado bajo arquitectura **N-Capas**, con separación de responsabilidades:

- **Frontend:** React + TypeScript
- **Backend:** Spring Boot + Java 17
- **Base de Datos:** PostgreSQL
- **ORM:** JPA/Hibernate

---

## 🗃️ Entidades Principales

- **Usuario:** Instructor o encargado.
- **Rol:** InstructorNormal, InstructorRemunerado, Encargado.
- **Materia:** Materias asignadas al instructor.
- **Usuario_Materia:** Tabla intermedia para relación muchos a muchos.
- **Actividad:** Tipo de actividad realizada (social o remunerada).
- **Registro_Hora:** Registro individual de horas realizadas.
- **Formulario:** Grupo de registros por semana.
- **Validación:** Firma digital ligera del encargado.

---

## 👤 Roles del Sistema

- **Instructor / Estudiante:**
  - Registro de horas.
  - Generación de formularios.
  - Descarga de PDF.

- **Encargado:**
  - Revisión de formularios.
  - Validación o rechazo.
  - Firma digital ligera.

---

## 🚀 Tecnologías Utilizadas

| Tecnología     | Descripción                          |
|----------------|--------------------------------------|
| React          | Interfaz de usuario (frontend)       |
| TypeScript     | Tipado estático para mayor seguridad |
| Spring Boot    | Backend con API REST                 |
| PostgreSQL     | Base de datos relacional             |
| JPA/Hibernate  | ORM para acceso a datos              |
| Vercel         | Despliegue del frontend              |
| Docker/Koyeb | Despliegue del backend y base de datos |

---

## 🔐 Seguridad

- Manejo de sesiones por roles.
- Validaciones en frontend y backend.
- Hash de contraseñas almacenadas en la base de datos.
- Filtros y control de acceso a endpoints.

---

## 📦 Estructura de Repositorios

- `backend/` → Código fuente del backend (Spring Boot).
- `frontend/` → Código fuente del frontend (React).
- Ambos repositorios separados como exige el proyecto.

---

## 📄 Documentación

- Documentación completa del API REST mediante Confluence.
- Diagrama Entidad-Relación de la base de datos.
- Descripción funcional y técnica del sistema.

---

## 🧪 Pruebas

- Validación manual de flujos principales (registro, validación, descarga).
- Pruebas básicas con Postman para endpoints REST.

---

## 👨‍💻 Desarrollado por

> Grupo - 08 para la materia **Programación N Capas**  
> Universidad Centroaméricana José Simeón Cañas / Departamento de Electrónica e Informática – 2025

---

## 🧪 Links de deployments, api y diagrama er

> [Frontend](https://ing-software-project.vercel.app/)

> [Backend](https://spring-rehosar.fly.dev/)

> [POSTMAN](https://grey-flare-657894.postman.co/workspace/My-Workspace~cc82f0d1-a7ca-4091-8116-bbd72a16d802/collection/19635488-aecd6434-a6dc-40f0-abae-bfe949e4b5e6?action=share&creator=19635488&active-environment=19635488-8f9c3486-bd72-4950-b729-dd084c3903a7&live=xoyv7wpd4n)

>[DIAGRAMA_ER](https://drive.google.com/file/d/1buf6y2j1gHlcx6ueR1WxWp3zqs65m6Ke/view?usp=sharing)
---
📘 Descripción Funcional y Técnica del Sistema REHOSAR
🧩 1. Descripción General del Sistema

REHOSAR (Registro de Horas Sociales y Remuneradas) es un sistema web desarrollado para el Departamento de Electrónica e Informática (DEI) con el propósito de digitalizar, automatizar y centralizar el proceso de registro, validación y generación de formularios de horas sociales y remuneradas de estudiantes e instructores.

El sistema elimina el uso de hojas físicas y permite:

Registrar horas desde la web.

Asociar actividades clasificadas como sociales o remuneradas.

Validar formularios semanalmente.

Firmar digitalmente mediante firma ligera tanto del instructor como del encargado.

Generar automáticamente un PDF oficial del formulario.

El sistema está construido bajo una arquitectura N-Capas y desplegado completamente en la nube.

🧭 2. Descripción Funcional
✔️ 2.1 Objetivos Funcionales

Registro digital de horas por parte de instructores y estudiantes.

Agrupación semanal de registros dentro de formularios.

Validación y firma digital ligera por parte del encargado.

Generación de formularios en PDF para impresión o archivo.

Control de acceso por roles.

Historial y seguimiento de formularios enviados, rechazados y validados.

✔️ 2.2 Funcionalidades del Sistema
🔹 A. Registro de Horas

El usuario puede:

Registrar fecha, hora de inicio, hora de finalización.

Seleccionar actividad (social o remunerada).

Seleccionar aula o lugar.

Guardar múltiples registros por día.

El sistema calcula automáticamente:

Diferencias de tiempo.

Horas totales por registro.

Firma ligera del usuario al enviarlo.

🔹 B. Generación de Formularios

Cada usuario genera un formulario semanal que incluye:

Datos del usuario.

Registros de la semana agrupados.

Total de horas sociales o remuneradas.

Estado del formulario (PENDIENTE, VALIDADO, RECHAZADO).

🔹 C. Validación por Encargado

El encargado puede:

Revisar formularios pendientes.

Aceptar o rechazar.

Firmar digitalmente mediante firma ligera.

Añadir un comentario al rechazar.

🔹 D. Exportación en PDF

El formulario validado puede descargarse como PDF con:

Datos del instructor.

Registros en tabla.

Firma digital ligera.

Espacios para firmas físicas si son requeridas.

🔹 E. Seguridad y Roles

Roles del sistema:

Instructor Normal

Instructor Remunerado

Encargado

Cada rol tiene permisos específicos sobre:

Vista de formularios

Acceso a endpoints

Operaciones permitidas en frontend

🏗️ 3. Arquitectura Técnica
✔️ 3.1 Arquitectura General

REHOSAR implementa arquitectura N-Capas, dividida en:

1. Capa de Presentación – Frontend

React 18 + TypeScript

Comunicación con backend mediante API REST

Módulos:

Registro de horas

Gestión de formularios

Panel de encargado

Descarga de PDF

2. Capa de Negocio – Backend (Spring Boot)

El backend implementa:

Servicios (Service Layer)

Controladores REST

Validaciones

Seguridad (Filters + JWT-like session)

Generación de PDF

Lenguaje: Java 17

Frameworks:

Spring Boot

Spring Web

Spring Data JPA

Hibernate

3. Capa de Persistencia – Base de Datos

PostgreSQL

ORM: Hibernate/JPA

Relaciones:

Usuarios ↔ Roles (Many to One)

Usuario ↔ Materia (Many to Many)

Actividad (Catalog)

Formulario ↔ Registro_Hora (One to Many)

Validación (One to One formulario encargado)

Incluye integridad referencial y claves foráneas en todas las relaciones.

✔️ 3.2 Flujo General del Sistema
1️⃣ Instructor registra horas

⬇️

2️⃣ Sistema asigna firma ligera del instructor

⬇️

3️⃣ Se genera formulario semanal

⬇️

4️⃣ Encargado revisa

✔️ Valida → Firma ligera + Aprueba

❌ Rechaza → Añade comentario
⬇️

5️⃣ Usuario descarga PDF
🗃️ 4. Modelo de Datos (Entidades)
Usuario

id, nombre, email, password (hasheado)

rol_id

relación con materias

Rol

ENCARGADO

INSTRUCTOR_REMUNERADO

INSTRUCTOR_SOCIAL

Materia

Materias asignadas al instructor

Actividad

Catálogo de actividades

Tipo (social/remunerada)

Registro_Hora

fecha

hora_inicio

hora_fin

total_horas calculado

actividad_id

formulario_id (opcional)

Formulario

semana

usuario_id

estado

horas_totales

Validación

formulario_id

encargado_id

firma_ligera

fecha_validacion

🔐 5. Seguridad

Hash de contraseñas usando algoritmo seguro.

Validación por roles en backend.

Middleware/filtros para restringir acceso.

Validación de datos en backend y frontend.

Manejo adecuado de sesiones/headers.

Protección contra:

Acceso no autorizado

Manipulación de datos

Inyección SQL (mitigado con JPA)
