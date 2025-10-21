pipeline {
    agent any
    description 'CI/CD pipeline for Country Service microservice'
    tools {
        maven 'mymaven'
    }
    environment {
        MAVEN_OPTS = '-Xmx1024m'
        SONAR_PROJECT_KEY = 'country-service'
    }
    stages {
        stage('Checkout code') {
            steps {
                // Pull code from GitHub
                git branch: 'master', url: 'https://github.com/hattay112amine/spring-boot-country-service-microservice.git'
            }
        }
        stage('Compile, test code, package in war file and store it in maven repo') {
            steps {
                sh 'mvn clean install'
            }
            post {
                success {
                    // Publish JUnit test reports
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv(installationName: 'MySonarQubeServer', credentialsId: 'sonarqubePWD') {
                    // Run SonarQube analysis
                    sh "mvn sonar:sonar -Dsonar.projectKey=${SONAR_PROJECT_KEY}"
                }
            }
        }
    }
    post {
        always {
            echo "Pipeline finished at ${new Date()}"
        }
    }
}
