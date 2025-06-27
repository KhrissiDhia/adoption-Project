pipeline {
  agent any

  environment {
    SONAR_PROJECT_KEY = 'adoption-project'
    SONAR_HOST_URL = 'http://localhost:9000'  // Doit correspondre à l'URL configurée
  }

  stages {
    stage('🧹 Clean') {
      steps { sh 'mvn clean' }
    }

    stage('⚙️ Compile') {
      steps { sh 'mvn compile' }
    }

    stage('🧪 Tests') {
      steps { sh 'mvn test -Dtest=AdoptionServicesImplMockitoTest,AdoptionServicesImplTest' }
    }

    stage('📦 Package') {
      steps { sh 'mvn package -DskipTests' }
    }

    stage('🔍 Analyse SonarQube') {
      steps {
        withSonarQubeEnv('sonar') {  // Doit exactement matcher le nom dans Jenkins
          withCredentials([string(credentialsId: 'sonarqu', variable: 'SONAR_TOKEN')]) {
            sh """
              mvn sonar:sonar \
                -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                -Dsonar.host.url=${SONAR_HOST_URL} \
                -Dsonar.login=${SONAR_TOKEN}
            """
          }
        }
      }
    }
  }

  post {
    always {
      echo 'Pipeline terminé - voir les résultats ci-dessus'
    }
    failure {
      echo 'ÉCHEC du pipeline - vérifiez les logs'
    }
  }
}