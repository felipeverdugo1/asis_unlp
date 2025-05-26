## 🚀 Cómo ejecutar el proyecto

1. Cloná este repositorio
2. Abrí el proyecto en IntelliJ (se detecta como Maven automáticamente)
3. Configurá un servidor Tomcat 11:
   - File > Settings > Application Servers > Add > Tomcat 11 jdk 23
4. Creá una configuración de ejecución:
   - Run > Edit Configurations > + > Tomcat Server > Local
   - Usar artefacto: `app_web:war exploded`
   - Context path: `/` 
5. Ejecutá y accedé a `http://localhost:8080/app_web/`
