FROM jboss/wildfly
MAINTAINER perales/sepomex
COPY --chown=jboss test.txt /tmp/test.txt
ADD src/main/resources/docker/customization /opt/jboss/wildfly/customization/
ADD src/main/resources/docker/modules /opt/jboss/wildfly/modules/
RUN /opt/jboss/wildfly/customization/execute.sh
ADD target/sepomex-1.0-SNAPSHOT.war /opt/jboss/wildfly/standalone/deployments/