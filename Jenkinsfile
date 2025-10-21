pipeline {
    // Exécuter le pipeline sur n'importe quel agent disponible
    agent any
    
    // Définir les outils à utiliser
    tools {
        // Le nom 'myMaven' doit correspondre à la configuration de l'outil Maven dans Jenkins
        maven 'myMaven' 
    }
    
    // Définir les variables d'environnement (si besoin, comme les infos de déploiement)
    environment {
        // Ces variables sont des exemples. Remplacez par vos vraies valeurs.
        TOMCAT_USER = 'tomcat_user' 
        TOMCAT_PASSWORD = credentials('tomcat-creds') // Utilise les identifiants stockés dans Jenkins
        TOMCAT_HOST = 'votre.ip.du.serveur.tomcat'
        TOMCAT_PORT = '8080' // Port par défaut de Tomcat Manager
        APP_NAME = 'country-service' // Le nom de votre application/fichier WAR sans l'extension
    }

    stages {
        // Étape 1: Extraction du code (Checkout)
        stage('Checkout code') {
            steps {
                // Le SCM est généralement géré par la configuration du job pipeline, 
                // mais on peut le spécifier si on veut le faire explicitement ici.
                // Pour un pipeline configuré pour lire le Jenkinsfile depuis SCM, cette étape est souvent implicite ou non nécessaire.
                echo 'Checking out code...' 
            }
        }
        
        // Étape 2: Compilation
        stage('Compile code') {
            steps {
                sh 'mvn clean compile'
            }
        }
        
        // Étape 3: Tests unitaires et intégration
        stage('Test code') {
            steps {
                sh 'mvn test'
            }
            // Section post pour archiver les résultats des tests même si le build échoue
            post {
                always {
                    // Archive les résultats JUnit au format XML (par défaut sous target/surefire-reports/*.xml)
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml' 
                }
            }
        }
        
        // Étape 4: Création du package (WAR/JAR)
        stage('Package code') {
            steps {
                sh 'mvn package'
            }
        }
        
        // Étape 5: Déploiement Continu (CD) sur Apache Tomcat
        stage('Continuous Delivery (Tomcat Deploy)') {
            steps {
                echo 'Starting deployment to Apache Tomcat...'
                
                // 1. Déplacer le fichier WAR vers le dossier webapps de Tomcat via SSH ou SCP.
                //    Ici on utilise 'sshpass' pour l'exemple mais il est préférable d'utiliser le plugin 'SSH Steps' de Jenkins 
                //    ou une librairie dédiée dans un script plus robuste.
                //
                //    Alternative plus courante : Utiliser l'API du Tomcat Manager (voir Tomcat Manager Plugin pour Jenkins)
                //    
                //    Méthode avec Tomcat Manager via cURL (nécessite l'accès au Tomcat Manager)
                //    Le nom du fichier WAR est supposé être 'target/${APP_NAME}.war'
                sh """
                    # URL de déploiement: http://TOMCAT_HOST:TOMCAT_PORT/manager/text/deploy
                    # Utilisation des variables d'environnement définies ci-dessus
                    curl -u ${TOMCAT_USER}:${TOMCAT_PASSWORD} \\
                         -T target/${APP_NAME}.war \\
                         "http://${TOMCAT_HOST}:${TOMCAT_PORT}/manager/text/deploy?path=/${APP_NAME}&update=true"
                """
                echo 'Deployment successful via Tomcat Manager.'
            }
        }
    }
    
    // Section post (à la fin de tout le pipeline)
    post {
        success {
            echo 'Pipeline executed successfully! ✅'
            // Envoyer une notification (ex: email, Slack...)
        }
        failure {
            echo 'Pipeline failed! ❌'
            // Envoyer une notification d'échec
        }
    }
}
