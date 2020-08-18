FROM maven:3.6.3-adoptopenjdk-11-openj9 AS build
ADD src /usr/src/app/src
ADD pom.xml /usr/src/app/
RUN mvn -f /usr/src/app/pom.xml clean package -Dmaven.test.skip=true
RUN mv /usr/src/app/target/*.jar /usr/src/app/target/app.jar

FROM adoptopenjdk/openjdk14-openj9
RUN mkdir /sepomex-indices/
COPY --from=build /usr/src/app/target/app.jar /
ENTRYPOINT java $JAVA_OPTS -XX:NativeMemoryTracking=summary -XX:+UnlockDiagnosticVMOptions -XX:+PrintNMTStatistics -Xshareclasses -Xquickstart -jar /app.jar
EXPOSE 8080