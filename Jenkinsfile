pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/KhrissiDhia/adoption-Project.git',
                    branch: 'main',
                    credentialsId: 'github-cred'
            }
        }

        stage('Prepare') {
            steps {
                sh 'chmod +x mvnw'
            }
        }

        stage('Build') {
            steps {
                // Compile le projet et construit le jar en SKIPPANT les TESTS
                sh './mvnw clean install -DskipTests'
            }
        }

        // Ici tu peux ajouter des étapes supplémentaires :
        // Exemple :
        // stage('SonarQube Analysis') { ... }
        // stage('Docker Build') { ... }
        // stage('Deploy') { ... }
    }
}
