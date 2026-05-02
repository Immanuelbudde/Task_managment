# Stage 1: Build the application
FROM maven:3.8.5-openjdk-11 AS build
COPY . /app
WORKDIR /app
RUN mvn clean package -DskipTests

# Stage 2: Run the application in Tomcat
FROM tomcat:9.0-jdk11-openjdk
# Remove default Tomcat apps
RUN rm -rf /usr/local/tomcat/webapps/*
# Copy the built WAR file and rename to ROOT.war for root context
COPY --from=build /app/target/TaskManagement.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
