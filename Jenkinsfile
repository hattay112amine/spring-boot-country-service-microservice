pipeline {
    agent any
    tools {
        maven 'mymaven' // configure Maven dans Jenkins
        jdk 'jdk-17'    // JDK 17 installé sur Jenkins
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/hattay112amine/spring-boot-country-service-microservice.git'
            }
        }

        stage('Build & Test') {
            steps {
                echo 'Compilation et tests Maven'
                sh 'mvn clean install'
            }
        }

        stage('SonarQube Analysis') {
            environment {
                SONAR_HOST_URL = 'http://172.27.132.151:9000'
                SONAR_LOGIN = credentials('sonar-token') // créer un token dans Jenkins
            }
            steps {
                echo 'Analyse SonarQube'
                sh "mvn sonar:sonar -Dsonar.login=${SONAR_LOGIN}"
            }
        }

        stage('Deploy to Nexus') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'admin', passwordVariable: 'admin')]) {
                    sh 'mvn deploy -Dnexus.user=$NEXUS_USER -Dnexus.password=$NEXUS_PASS'
                }
            }
        }

        stage('Deploy to Tomcat') {
            steps {
                echo 'Déploiement du WAR sur Tomcat'
                sh 'scp target/Myapp.war user@tomcat-server:/opt/tomcat/webapps/'
            }
        }
    }

    post {
        always {
            echo 'Pipeline terminé.'
        }
    }
}
