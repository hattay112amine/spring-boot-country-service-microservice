pipeline {
    agent any

    environment {
        JAVA_HOME = "/usr/lib/jvm/java-17-openjdk-amd64"  // Chemin vers Java 17 sur ton serveur
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
        NEXUS_USER = "admin"
        NEXUS_PASSWORD = "a5b857a6-87af-4c11-8f22-f404af28a04c"
        NEXUS_URL = "http://172.27.132.151:8081/repository/maven-releases/"
        TOMCAT_WEBAPPS = "/opt/tomcat/latest/webapps/"
    }

    stages {

        stage('Checkout') {
            steps {
                echo "Checkout du code depuis GitHub"
                git branch: 'main', url: 'https://github.com/hattay112amine/spring-boot-country-service-microservice.git'
            }
        }

        stage('Build & Test') {
            steps {
                echo "Compilation et tests Maven"
                sh 'mvn clean install'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo "Analyse SonarQube"
                withSonarQubeEnv('MySonarQubeServer') { // Nom de ton serveur SonarQube défini dans Jenkins
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                echo "Déploiement sur Nexus"
                sh "mvn deploy -Dnexus.username=${NEXUS_USER} -Dnexus.password=${NEXUS_PASSWORD}"
            }
        }

        stage('Deploy to Tomcat') {
            steps {
                echo "Déploiement du fichier WAR sur Tomcat"
                sh "cp target/*.war ${TOMCAT_WEBAPPS}"
            }
        }
    }

    post {
        success {
            echo "Pipeline terminé avec succès !"
        }
        failure {
            echo "Pipeline échoué !"
        }
    }
}
