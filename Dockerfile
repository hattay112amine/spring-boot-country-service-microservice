FROM openjdk:21-oracle
VOLUME /tmp

# Vérifier si le JAR existe, sinon afficher un message d'erreur
COPY target/*.jar app.jar

# Alternative: copier un JAR spécifique si vous connaissez son nom
# COPY target/country-service-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
