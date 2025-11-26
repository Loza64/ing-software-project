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

- Documentación completa del API REST mediante Postmant.
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

> [POSTMAN](https://grey-flare-657894.postman.co/workspace/My-Workspace~cc82f0d1-a7ca-4091-8116-bbd72a16d802/collection/19635488-aecd6434-a6dc-40f0-abae-bfe949e4b5e6?action=share&creator=19635488&active-environment=19635488-8f9c3486-bd72-4950-b729-dd084c3903a7)

>[DIAGRAMA_ER](https://drive.google.com/file/d/1buf6y2j1gHlcx6ueR1WxWp3zqs65m6Ke/view?usp=sharing)
---

