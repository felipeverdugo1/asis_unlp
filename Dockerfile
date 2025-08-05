FROM node:22 AS frontend-build

WORKDIR /app
COPY frontend/asis_unlp ./frontend/
WORKDIR /app/frontend

RUN npm install
RUN npm run build --base-href asis_unlp

FROM maven AS grupo5build

WORKDIR /home/app
COPY backend/pom.xml pom.xml
RUN mvn verify --fail-never

COPY backend/src ./src

# Copio el frontend dentro del backend
COPY --from=frontend-build /app/frontend/dist/asis_unlp ./src/main/webapp/asis_unlp/

RUN mvn package

FROM tomcat
COPY --from=grupo5build /home/app/target/app-web-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
