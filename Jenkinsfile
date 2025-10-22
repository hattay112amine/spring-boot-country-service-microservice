pipeline {
    agent any

    environment {
        MVN_HOME = tool name: 'mymaven', type: 'maven'
        JAVA_HOME = tool name: 'JDK17', type: 'jdk'
        NEXUS_URL = "http://172.27.132.151:8081/repository/maven-releases/"
        NEXUS_CREDENTIALS = "nexus-creds" // ID des credentials dans Jenkins
        TOMCAT_URL = "http://172.27.132.151:8080/manager/text"
        TOMCAT_CREDENTIALS = "tomcat-creds" // ID des credentials dans Jenkins
        SONARQUBE = "sonarqube-server" // Nom du serveur SonarQube configuré dans Jenkins
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/hattay112amine/spring-boot-country-service-microservice.git'
            }
        }

        stage('Build & Test') {
            steps {
                withEnv(["PATH+MAVEN=${MVN_HOME}/bin", "JAVA_HOME=${JAVA_HOME}"]) {
                    sh 'mvn clean install'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv("${SONARQUBE}") {
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('Upload to Nexus') {
            steps {
                withCredentials([usernamePassword(credentialsId: "${NEXUS_CREDENTIALS}", passwordVariable: 'NEXUS_PASS', usernameVariable: 'NEXUS_USER')]) {
                    sh """
                        mvn deploy:deploy-file \
                        -Durl=${NEXUS_URL} \
                        -DrepositoryId=nexus-releases \
                        -Dfile=target/Myapp-0.0.1-SNAPSHOT.jar \
                        -DgroupId=com.example \
                        -DartifactId=Myapp \
                        -Dversion=0.0.1-SNAPSHOT \
                        -Dpackaging=jar \
                        -DgeneratePom=true \
                        -Dusername=$NEXUS_USER \
                        -Dpassword=$NEXUS_PASS
                    """
                }
            }
        }

        stage('Deploy to Tomcat') {
            steps {
                withCredentials([usernamePassword(credentialsId: "${TOMCAT_CREDENTIALS}", passwordVariable: 'TOMCAT_PASS', usernameVariable: 'TOMCAT_USER')]) {
                    sh """
                        curl -u $TOMCAT_USER:$TOMCAT_PASS \
                        --upload-file target/Myapp-0.0.1-SNAPSHOT.war \
                        ${TOMCAT_URL}/deploy?path=/Myapp&update=true
                    """
                }
            }
        }

    }

    post {
        always {
            echo "Pipeline terminé"
        }
        failure {
            mail to: 'ton.email@exemple.com',
                 subject: "Échec du build ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                 body: "Vérifie Jenkins pour plus de détails : ${env.BUILD_URL}"
        }
    }
}
