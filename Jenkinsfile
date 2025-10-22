pipeline {
    agent any

    tools {
        // Assure-toi que le nom correspond à l'installation Maven dans Jenkins
        maven 'Maven3'
        jdk 'JDK17'
    }

    environment {
        // SonarQube
        SONARQUBE_SERVER = 'MySonarQubeServer'
        SONARQ_CREDENTIALS = 'sonarqubePWD'

        // Nexus
        NEXUS_URL = 'http://localhost:8081/repository/maven-releases/'
        NEXUS_CREDENTIALS = 'nexus-user'

        // Tomcat
        TOMCAT_URL = 'http://localhost:8080/manager/text'
        TOMCAT_USER = 'admin'
        TOMCAT_PASS = 'admin123'
    }

    stages {
        stage('Checkout Code') {
            steps {
                echo "Récupération du code depuis GitHub"
                git branch: 'main', url: 'https://github.com/hattay112amine/spring-boot-country-service-microservice.git'
            }
        }

        stage('Build & Test') {
            steps {
                echo "Compilation et tests Maven (Java 17)"
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
                withSonarQubeEnv(installationName: "${SONARQUBE_SERVER}", credentialsId: "${SONARQ_CREDENTIALS}") {
                    sh 'mvn sonar:sonar -Dsonar.projectKey=country-service'
                }
            }
        }

        stage('Upload to Nexus') {
            steps {
                echo "Upload du WAR vers Nexus"
                sh """
                mvn deploy:deploy-file \
                    -Durl=${NEXUS_URL} \
                    -DrepositoryId=${NEXUS_CREDENTIALS} \
                    -Dfile=target/*.war \
                    -DgroupId=com.example \
                    -DartifactId=Myapp \
                    -Dversion=0.0.1-SNAPSHOT \
                    -Dpackaging=war
                """
            }
        }

        stage('Deploy to Tomcat') {
            steps {
                echo "Déploiement du WAR sur Tomcat"
                sh """
                curl -u ${TOMCAT_USER}:${TOMCAT_PASS} \
                     -T target/*.war \
                     "${TOMCAT_URL}/deploy?path=/Myapp&update=true"
                """
            }
        }
    }

    post {
        always {
            echo "Pipeline terminé"
        }
        failure {
            echo "Pipeline échoué"
        }
        success {
            echo "Pipeline réussi"
        }
    }
}
