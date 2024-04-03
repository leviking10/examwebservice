
FROM openjdk:17-jdk

# Argument pour le JAR_FILE (à remplacer par le chemin de votre fichier JAR)
ARG JAR_FILE=target/*.jar

# Copier le fichier JAR dans l'image
COPY ${JAR_FILE} app.jar

# Exécuter l'application Java à partir du JAR
ENTRYPOINT ["java","-jar","/app.jar"]
