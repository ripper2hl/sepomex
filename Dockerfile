FROM jboss/wildfly
MAINTAINER perales/sepomex
USER root
COPY sepomex.txt /tmp/sepomex.txt
RUN chown jboss:jboss /tmp/sepomex.txt
USER jboss
ADD src/main/resources/docker/customization /opt/jboss/wildfly/customization/
ADD src/main/resources/docker/modules /opt/jboss/wildfly/modules/
RUN /opt/jboss/wildfly/customization/execute.sh
ADD target/sepomex.war /opt/jboss/wildfly/standalone/deployments/
