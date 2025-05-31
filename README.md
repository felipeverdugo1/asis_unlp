## 🚀 Cómo ejecutar el proyecto

1. Cloná este repositorio
2. Abrí el proyecto en IntelliJ (se detecta como Maven automáticamente)
3. Configurá un servidor Tomcat 11:
   - File > Settings > Application Servers > Add > Tomcat 11 jdk 23
4. Creá una configuración de ejecución:
   - Run > Edit Configurations > + > Tomcat Server > Local
   - Usar artefacto: `app_web:war exploded`
   - Context path: `/` 
5. Ejecutá y accedé a `http://localhost:8080/`


Crear la base de datos asis_unlp y respetar el documento /resources/META-INF/persistence.xml 
Cuando se levanta la app y accedé a `http://localhost:8080/` se crea el schema de la base de datos asis_unlp ( las tablas sin datos ) 
Puede ejecutar el script.sql para llenar las tablas con datos genericos 
Para usar la api ->  `http://localhost:8080/rest/(entidad)/(params) ` Ej : `http://localhost:8080/rest/usuario/1`