FROM maven:3.6.3-openjdk-16-slim AS build
ADD src /usr/src/app/src
ADD pom.xml /usr/src/app/
RUN mvn -f /usr/src/app/pom.xml clean package -Dmaven.test.skip=true
RUN mv /usr/src/app/target/*.jar /usr/src/app/target/app.jar

FROM adoptopenjdk/openjdk16-openj9:x86_64-alpine-jre-16_36_openj9-0.25.0
RUN mkdir /sepomex-indices/
COPY --from=build /usr/src/app/target/app.jar /
ENTRYPOINT java -XX:MaxMetaspaceSize=64m -XX:CompressedClassSpaceSize=8m -Xss256k -Xmn8m -XX:InitialCodeCacheSize=4m -XX:ReservedCodeCacheSize=8m -XX:MaxDirectMemorySize=16m -XX:NativeMemoryTracking=summary -XX:+UnlockDiagnosticVMOptions -XX:+PrintNMTStatistics -Xshareclasses:cacheDir=/tmp,noPersistentDiskSpaceCheck -Xquickstart -XX:+UseContainerSupport -jar /app.jar
EXPOSE 8080