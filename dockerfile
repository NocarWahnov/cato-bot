FROM amazoncorretto:22.0.1-alpine
MAINTAINER nocars.tk
COPY target/ts3-RSS-Feed-1.0-SNAPSHOT.jar cato.jar
ENTRYPOINT ["java", "-jar", "/cato.jar"]