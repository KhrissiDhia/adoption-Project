pipeline {
  agent any

  environment {
    SONAR_PROJECT_KEY = 'adoption-project'
    SONAR_HOST_URL = 'http://localhost:9000' // À adapter selon votre installation
    SONAR_TOKEN = credentials('sonarqu')
  }

  stages {
    // [Les autres stages restent identiques...]

    stage('🔍 Analyse SonarQube') {
      steps {
        withSonarQubeEnv('sonar') { // Doit correspondre au nom dans Jenkins
          // Solution sécurisée pour éviter l'interpolation Groovy
          script {
            withCredentials([string(credentialsId: 'sonarqu', variable: 'SONAR_TOKEN_SECURE')]) {
              sh """
                mvn sonar:sonar \
                  -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                  -Dsonar.host.url=${SONAR_HOST_URL} \
                  -Dsonar.login=${SONAR_TOKEN_SECURE}
              """
            }
          }
        }
      }
    }
  }
}