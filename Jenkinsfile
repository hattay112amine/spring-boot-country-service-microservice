pipeline {
    agent any

    environment {
        JAVA_HOME = "/usr/lib/jvm/java-17-openjdk-amd64"
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
        NEXUS_URL = "http://172.27.132.151:8081/repository/maven-releases/"
        TOMCAT_WEBAPPS = "/opt/tomcat/latest/webapps/"
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
                withCredentials([usernamePassword(credentialsId: 'nexus-creds', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASSWORD')]) {
                    sh "mvn deploy -Dnexus.username=${NEXUS_USER} -Dnexus.password=${NEXUS_PASSWORD}"
                }
            }
        }

        stage('Deploy to Tomcat') {
            steps {
                sh "cp target/*.war ${TOMCAT_WEBAPPS}"
            }
        }
    }

    post {
        success { echo "Pipeline terminé avec succès !" }
        failure { echo "Pipeline échoué !" }
    }
}
