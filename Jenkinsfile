pipeline {
  agent any

  environment {
    SONAR_PROJECT_KEY = 'adoption-project'
    SONAR_TOKEN = credentials('sonarqu') // ID token Jenkins Credentials
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
        withSonarQubeEnv('sonar') {
          sh """
            mvn sonar:sonar \
              -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
              -Dsonar.login=${SONAR_TOKEN}
          """
        }
      }
    }
  }
}
