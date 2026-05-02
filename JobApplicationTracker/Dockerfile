# Stage 1: Build
FROM maven:3.8.5-openjdk-11 AS build
COPY . /app
WORKDIR /app
RUN mvn clean package -DskipTests

# Stage 2: Run (Using Temurin JDK 17 - the most stable for Railway)
FROM tomcat:9.0-jdk17-temurin-jammy
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /app/target/TaskManagement.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
