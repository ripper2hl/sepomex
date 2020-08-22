FROM maven:3.6.3-adoptopenjdk-11-openj9 AS build
ADD src /usr/src/app/src
ADD pom.xml /usr/src/app/
RUN mvn -f /usr/src/app/pom.xml clean package -Dmaven.test.skip=true
RUN mv /usr/src/app/target/*.jar /usr/src/app/target/app.jar

FROM adoptopenjdk/openjdk14-openj9:jre-14.0.2_12_openj9-0.21.0-alpine
RUN mkdir /sepomex-indices/
COPY --from=build /usr/src/app/target/app.jar /
ENTRYPOINT java -XX:MaxMetaspaceSize=64m -XX:CompressedClassSpaceSize=8m -Xss256k -Xmn8m -XX:InitialCodeCacheSize=4m -XX:ReservedCodeCacheSize=8m -XX:MaxDirectMemorySize=16m -XX:NativeMemoryTracking=summary -XX:+UnlockDiagnosticVMOptions -XX:+PrintNMTStatistics -Xshareclasses:cacheDir=/tmp,noPersistentDiskSpaceCheck -Xquickstart -XX:+UseContainerSupport -jar /app.jar
EXPOSE 8080