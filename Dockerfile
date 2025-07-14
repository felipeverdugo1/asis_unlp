FROM maven as grupo5build

WORKDIR /home/ejemplo
COPY pom.xml pom.xml
RUN mvn verify --fail-never
COPY src src
RUN mvn package

FROM tomcat
COPY --from=grupo5build /home/ejemplo/target/app-web-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
