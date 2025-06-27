pipeline {
  agent any

  environment {
    SONAR_PROJECT_KEY = 'adoption-project'
  }

  stages {
    stage('🧹 Clean') {
      steps {
        sh 'mvn clean'
      }
    }

    stage('⚙️ Compile') {
      steps {
        sh 'mvn compile'
      }
    }

    stage('🧪 Tests') {
      steps {
        sh 'mvn test -Dtest=AdoptionServicesImplMockitoTest,AdoptionServicesImplTest'
      }
    }

    stage('📦 Package') {
      steps {
        sh 'mvn package -DskipTests'
      }
    }

    stage('🔍 Analyse SonarQube') {
      steps {
        withCredentials([string(credentialsId: 'sonar11', variable: 'SONAR_TOKEN')]) {
          withSonarQubeEnv('sonar') {
            sh '''
              mvn -B sonar:sonar \
                -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                -Dsonar.host.url=http://localhost:9000 \
                -Dsonar.login=${SONAR_TOKEN} \
                -Dsonar.java.source=17 \
                -Dsonar.sourceEncoding=UTF-8
            '''
          }
        }
      }
    }
  }

  post {
    always {
      echo '✅ Pipeline terminé - voir les résultats ci-dessus'
    }
    failure {
      echo '❌ ÉCHEC du pipeline - vérifiez les logs pour plus de détails'
    }
  }
}