# Prompts

## Descripción

* Estoy creando una aplicación para [la gestión de un conjunto residencial], según las entidades, cómo crees que funciona la aplicación? → Insertar la imagen de las entidades
* Crea en un solo párrafo la descripción completa que pueda ser entendida por una persona normal
* Arma un diagrama de relaciones (ERD) con esas entidades

## Estructura

Requiero realizar una aplicación, es un sistema para administrar un conjunto residencial que permite registrar y organizar toda la información importante: cada apartamento y parqueadero queda asociado a sus propietarios o residentes, quienes a su vez tienen diferentes roles según su perfil (por ejemplo, residente, visitante, administrador o personal de seguridad). Los usuarios pueden hacer solicitudes como reservar áreas comunes, reportar novedades o gestionar permisos, mientras que la portería puede registrar la correspondencia recibida para cada apartamento y notificar a sus destinatarios. De esta forma, la plataforma centraliza en un solo lugar la gestión de apartamentos, parqueaderos, áreas comunes, solicitudes y correspondencia, facilitando la comunicación y el control entre residentes, seguridad y administración. Lo que necesito es que me des un prompt completo para pasar a copilot gpt4.1, con el fin de que primero me genere un .md con la estructura completa del proyecto, el detalle de que contiene cada modulo, y toda la informacion que requiera para llevar a cabo el proyecto con una total claridad, recuerda que necesito que se detalle todo, que contiene cada carpeta la estructura de carpetas, algo muy completo Ademas de esto, para que lo tengas en cuenta. tengo la siguiente estructura de carpetas en las cuales contengo codigo java - springboot, quiero que este sea el orden en que se genere el flujo de trabajo:

- 📁 Entity (1) Representa una tabla en la base de datos. Es un objeto que se guarda directamente. 📊 Es como un molde que dice cómo luce cada fila de la tabla.
- 📁 Dto (Data Transfer Object) (2) Objetos que se usan para enviar o recibir datos, especialmente hacia/desde el frontend. 📦 Es como una caja con solo los datos necesarios para enviar o recibir, sin detalles extra.
- 📁 Repository (3) Interactúa con la base de datos (mediante JPA/Hibernate, por ejemplo). Consultas personalizadas. 💾 Es quien sabe guardar, buscar, borrar y actualizar cosas en la base de datos.
- 📁 Service (4) Contiene la lógica del negocio. Procesa los datos, toma decisiones, valida reglas. CRUD. Métodos. 🧠 Es el cerebro que sabe qué hacer con los datos que le llegan del Controller.
- 📁 Impl (5) Contiene las implementaciones concretas de interfaces (normalmente de los services). ⚙️ Es la parte donde realmente se escribe cómo hacer las cosas, detrás de una interfaz.
- 📁 Controller (6) Recibe las solicitudes del usuario (API REST) y devuelve respuestas. 📲 Es como la recepcionista: recibe lo que pides y se lo pasa a quien lo pueda hacer (el Service).
- 📁 Config Contiene clases de configuración del proyecto (por ejemplo, para seguridad, conexión a base de datos, CORS, etc.). 🔧 Piensa en esto como el lugar donde le dices al sistema cómo debe comportarse.
- 📁 Exception Define errores personalizados que puedes lanzar o manejar. 🚨 Sirve para gritar: “¡Algo salió mal!” pero de forma controlada.
