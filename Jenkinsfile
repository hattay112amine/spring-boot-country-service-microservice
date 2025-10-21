pipeline {
    agent any
    tools {
        jdk 'JDK17'       // Nom exact de ton JDK 17 configuré dans Jenkins
        maven 'Maven3'    // Nom exact de Maven configuré dans Jenkins
    }
    environment {
        SONAR_PROJECT_KEY = 'country-service'
        SONARQUBE_CREDENTIALS = 'sonarqubePWD'
        SONARQUBE_SERVER = 'MySonarQubeServer'
    }
    stages {
        stage('Checkout code') {
            steps {
                echo "Checkout du code depuis GitHub"
                git branch: 'main', url: 'https://github.com/hattay112amine/spring-boot-country-service-microservice.git'
            }
        }

        stage('Build & Test') {
            steps {
                echo "Compilation et tests avec Maven (Java 17)"
                sh 'mvn clean install'
            }
            post {
                success {
                    echo "Publication des rapports JUnit"
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo "Analyse SonarQube du projet"
                withSonarQubeEnv(installationName: "${SONARQUBE_SERVER}", credentialsId: "${SONARQUBE_CREDENTIALS}") {
                    sh "mvn sonar:sonar -Dsonar.projectKey=${SONAR_PROJECT_KEY}"
                }
            }
        }

        stage('Quality Gate') {
            steps {
                echo "Vérification du Quality Gate SonarQube"
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }

    post {
        always {
            echo "Pipeline terminé à ${new Date()}"
        }
    }
}
