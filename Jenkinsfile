pipeline {
    agent any
    tools {
        maven 'mymaven'
    }
    stages {
        stage('Checkout code')
        {
            steps {
                git branch: 'master', url: 'https://github.com/Jamina-ENSI/Country-service'
            }
        }
        stage('Compile, test code, package in war file and store it in maven repo')
        {
            steps {
                sh 'mvn clean install'
            }
            post {
                success {
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports'
                }
            }
        }
        stage('SonarQube Analysis')
        {
            steps {
                withSonarQubeEnv(installationName: 'MySonarQubeServer', credentialsId: 'sonarqubePWD') {
                    sh "mvn sonar:sonar -Dsonar.projectKey=country-service -Dsonar.project"
                }
            }
        }
    }
}
