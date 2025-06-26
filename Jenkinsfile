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
                sh './mvnw clean install -DskipTests'
            }
        }

        stage('Maven Release') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'github-cred', usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD')]) {
                    sh '''
                        git config --global user.email "jenkins@localhost"
                        git config --global user.name "Jenkins"
                        ./mvnw release:prepare release:perform -B \
                          -Dusername=$GIT_USERNAME \
                          -Dpassword=$GIT_PASSWORD \
                          -DskipTests=true
                    '''
                }
            }
        }
    }
}
