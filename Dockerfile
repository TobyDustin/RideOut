FROM maven:3.6 AS MAVEN_TOOL_CHAIN
COPY pom.xml /tmp/
COPY api/ /tmp/api/
COPY frontend/pom.xml /tmp/frontend/
COPY frontend/src /tmp/frontend/src/
COPY frontend/*.json /tmp/frontend/
WORKDIR /tmp/
RUN mvn package -DskipTests

FROM tomcat:9-jre8-alpine
COPY --from=MAVEN_TOOL_CHAIN /tmp/api/target/rideout-api.war $CATALINA_HOME/webapps/rideout.war

HEALTHCHECK --interval=1m --timeout=3s CMD wget --quiet --tries=1 --spider http://localhost:8080/rideout/ || exit 1
