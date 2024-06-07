FROM amazoncorretto:22.0.1-alpine
MAINTAINER nocars.tk
#WORKDIR /app
COPY target/ts3-RSS-Feed-1.0-SNAPSHOT-jar-with-dependencies.jar cato.jar
#ENTRYPOINT ["java", "-jar", "/app/cato.jar"]
ENTRYPOINT ["java", "-jar", "cato.jar"]