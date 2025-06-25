pipeline {
    agent any

    environment {
        // Variables utiles, si nécessaire
        GIT_REPO = 'https://github.com/KhrissiDhia/adoption-Project.git'
        GIT_CRED = 'github-cred' // ID des identifiants configurés
    }

    stages {
        stage('Checkout') {
            steps {
                git(
                    url: "${env.GIT_REPO}",
                    branch: 'main',
                    credentialsId: "${env.GIT_CRED}"
                )
            }
        }

        stage('Build') {
            steps {
                sh './mvnw clean install' // Pour Windows : bat 'mvnw clean install'
            }
        }



        // Exemple de placeholder pour SonarQube
        // stage('SonarQube Analysis') {
        //     steps {
        //         withSonarQubeEnv('SonarQubeServer') {
        //             sh './mvnw sonar:sonar'
        //         }
        //     }
        // }

        // Exemple de placeholder pour construire une image Docker
        // stage('Build Docker Image') {
        //     steps {
        //         sh 'docker build -t khrissidhia/adoption-project .'
        //     }
        // }
    }
}
