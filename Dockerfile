# Prebuild Docker containers available by running:
#   docker run -p 8080:8080 edjeffreys/rideout:[branch]
# If a custom build is required (e.g. testing new feature branch) then run:
#   docker build --tag="rideout:[branch]" ./

FROM maven:3.6 AS MAVEN_TOOL_CHAIN
COPY pom.xml /tmp/
COPY api/ /tmp/api/
COPY frontend/pom.xml /tmp/frontend/
COPY frontend/src /tmp/frontend/src/
COPY frontend/*.json /tmp/frontend/
WORKDIR /tmp/
RUN mvn package

FROM tomcat:9-jre8-alpine
RUN rm -r $CATALINA_HOME/webapps/ROOT
COPY --from=MAVEN_TOOL_CHAIN /tmp/api/target/rideout-api.war $CATALINA_HOME/webapps/ROOT.war
EXPOSE 8080

HEALTHCHECK --interval=1m --timeout=3s CMD wget --quiet --tries=1 --spider http://localhost:8080/ || exit 1
