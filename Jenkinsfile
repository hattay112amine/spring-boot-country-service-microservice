pipeline {
    agent any
    tools {
        maven 'Maven3'
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
                sh 'mvn clean install'
            }
            post {
                success {
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv(installationName: "${SONARQUBE_SERVER}", credentialsId: "${SONARQUBE_CREDENTIALS}") {
                    sh "mvn sonar:sonar -Dsonar.projectKey=${SONAR_PROJECT_KEY}"
                }
            }
        }
        stage('Quality Gate') {
            steps {
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
