pipeline {
    agent any

    environment {
        JAVA_HOME = "/usr/lib/jvm/java-17-openjdk-amd64"
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
        NEXUS_URL = "http://172.27.132.151:8081/repository/maven-releases/"
        TOMCAT_HOME = "/opt/tomcat/latest"
        TOMCAT_WEBAPPS = "${TOMCAT_HOME}/webapps/"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/hattay112amine/spring-boot-country-service-microservice.git'
            }
        }

        stage('Build & Test') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('MySonarQubeServer') {
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                // Remplacer 'nexus-credentials-id' par l'ID réel des credentials Jenkins
                withCredentials([usernamePassword(credentialsId: '247ecaa9-0438-4568-9472-f99377ca65ec', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {
                    sh "mvn deploy -Dnexus.username=${NEXUS_USER} -Dnexus.password=${NEXUS_PASS}"
                }
            }
        }

        stage('Deploy to Tomcat') {
            steps {
                echo "Copie du WAR dans Tomcat"
                sh "cp target/*.war ${TOMCAT_WEBAPPS}"
                
                echo "Redémarrage de Tomcat"
                sh "${TOMCAT_HOME}/bin/shutdown.sh || true"
                sh "${TOMCAT_HOME}/bin/startup.sh"
            }
        }
    }

    post {
        success { echo "Pipeline terminé avec succès !" }
        failure { echo "Pipeline échoué !" }
    }
}
