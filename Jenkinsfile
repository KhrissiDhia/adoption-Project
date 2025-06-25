pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/KhrissiDhia/adoption-Project.git', branch: 'main', credentialsId: 'github-cred'
            }
        }
        stage('Prepare') {
            steps {
                sh 'chmod +x mvnw'
            }
        }
        stage('Build') {
            steps {
                sh './mvnw clean install'
            }
        }
        stage('Test') {
            steps {
                sh './mvnw test'
            }
        }
    }
}
