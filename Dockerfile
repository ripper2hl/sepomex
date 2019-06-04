FROM maven:alpine AS build
ADD src /usr/src/app/src
ADD pom.xml /usr/src/app/
RUN mvn -f /usr/src/app/pom.xml clean package -Dmaven.test.skip=true

FROM etiennek/spring-boot
COPY --from=build /usr/src/app/target/app.jar /
EXPOSE 8080