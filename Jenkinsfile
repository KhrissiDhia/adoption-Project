pipeline {
  agent any

  environment {
    SONAR_PROJECT_KEY = 'adoption-project'
    SONAR_HOST_URL = 'http://localhost:9000'
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
        withCredentials([string(credentialsId: 'sonarqu', variable: 'SONAR_TOKEN_SECURE')]) {
          withSonarQubeEnv('sonar') {
            sh(label: "Analyse SonarQube", script: """
              mvn -B sonar:sonar \
                -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                -Dsonar.host.url=${SONAR_HOST_URL} \
                -Dsonar.login=${SONAR_TOKEN_SECURE} \
                -Dsonar.java.source=17 \
                -Dsonar.sourceEncoding=UTF-8
            """)
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
      echo '❌ ÉCHEC du pipeline - vérifiez les logs'
    }
  }
}
