# ASIS UNLP

## Reentrega 2
- En la carpeta reentrega_entrega_2/

## Entrega 3
- Se genero un servlet en src/main/java/tests_servlet/
### Ejecucion del Test
1. Levantar el motor MySQL en su local con puerto 3306.
2. Levantar el proyecto con IntelliJ Tomcat 11.0.7 (Mas abajo esta el paso a paso de la configuracion).
3. Una vez que se levanto la aplicacion se puede hacer un GET a:
   - `https://localhost:8080/tests/DAO`
   - Esto imprime en consola lo que se logro, y luego se puede revisar el motor para ver las tablas y los registros creados.
4. Antes de volver a ejecutar el test hay que limpiar la base mediante otro endpoint:
   - `https://localhost:8080/tests/DAO/cleanup`
   - Esto borra todos los registros creados para el test para evitar conflictos.

### Aspectos a tener en cuenta
- Hay que crear una base de datos llamada `asis_unlp` en el motor MySQL, y las tablas se crearan solas al iniciar el Tomcat.
- Para usar la API:  `http://localhost:8080/rest/(entidad)/(params)`
  - Ejemplo: `http://localhost:8080/rest/usuario/1`

### Credenciales MySQL
- usuario: root
- password: admin

# 🚀 Cómo ejecutar el proyecto

## Mediante Intellij IDEA

1. Cloná este repositorio
2. Abrí el proyecto en IntelliJ (se detecta como Maven automáticamente)
3. Configurá un servidor Tomcat 11:
   - File > Settings > Application Servers > Add > Tomcat 11 jdk 23
4. Creá una configuración de ejecución:
   - Run > Edit Configurations > + > Tomcat Server > Local
   - Usar artefacto: `app_web:war exploded`
   - Context path: `/`
5. Configurá y dejá corriendo un motor MySQL en tu local al puerto 3306.
6. Ejecutá y accedé a `http://localhost:8080/`


## Mediante uso de consola sin ayudines (Linux)
### Requerimientos
- [docker](https://docs.docker.com/engine/install/) y [docker-compose](https://docs.docker.com/compose/install/)
- [Java 23](https://www.oracle.com/java/technologies/javase/jdk23-archive-downloads.html)
- [Maven](https://maven.apache.org/install.html)
- [Tomcat](https://tomcat.apache.org/)

### Orden de accion
Parados en la carpeta raiz del repositorio (sera para nosotros `./`, los path pueden cambiar segun su ubicacion)
1. Levantar el docker-compose para tener el motor MySQL.
   - `docker-compose -f docker-compose/docker-compose.yml up --build --force-recreate`
   - Esto levanta el motor de MySQL y corre el init.sql que se encuentra a su lado.
   - Puede ser que por consola no pueda conectarse a MySQL, para esto hace falta agregarle `--protocol=TCP`
     - `mysql --protocol=TCP -u root -p`
     - Usar las credenciales que estan mas arriba.
2. Compilar el proyecto utilizando Maven (asegurarse que maven use jdk 23).
   - `mvn clean install`
   - Esto genera el archivo target/app-web-<version>.war y corre los tests configurados
   - Para asegurarse el jdk que utilice se puede hacer: `JAVA_HOME=/opt/jdk-23.0.2/ mvn clean install`
3. Copiar el archivo .war generado a /path/instalacion/tomcat/webapps/
   - Ademas se va a renombrar para que el path sea mas comodo de acceder.
   - Copiar el archivo .war al /path/al/tomcat/apache-tomcat-11.0.7/webapps/
     - `cp ./target/app-web-1.0-SNAPSHOT.war /path/al/tomcat/apache-tomcat-11.0.7/webapps/app-web.war`
   - Levantar el tomcat, ejecutando los .sh
     - `./path/al/tomcat/apache-tomcat-11.0.7/bin/startup.sh`
     - Para detenerlo sera:
       - `./path/al/tomcat/apache-tomcat-11.0.7/bin/shutdown.sh`
4. Acceder a la url: https://localhost:8080/app-web/








