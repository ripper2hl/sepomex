FROM maven:3.6.3-openjdk-11 AS build
ADD src /usr/src/app/src
ADD pom.xml /usr/src/app/
RUN mvn -f /usr/src/app/pom.xml clean package -Dmaven.test.skip=true
RUN mv /usr/target/*.jar app.jar

FROM etiennek/spring-boot
USER root
RUN mkdir /sepomex-indices/ && chown -R bootapp /sepomex-indices/ && chmod 744 /sepomex-indices/
USER bootapp
COPY --from=build /usr/src/app/target/app.jar /
EXPOSE 8007