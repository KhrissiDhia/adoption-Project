pipeline {
  agent any

  environment {
    // Configuration SonarQube
    SONAR_PROJECT_KEY = 'adoption-project'
    SONAR_HOST_URL = 'http://localhost:9000' // À adapter si nécessaire
    SONAR_LOGIN = credentials('sonarqu') // Doit correspondre à l'ID dans Jenkins Credentials
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
        // 'Sonar' doit correspondre exactement au nom configuré dans Jenkins
        withSonarQubeEnv('sonar') {
          sh """
            mvn sonar:sonar \
              -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
              -Dsonar.host.url=${SONAR_HOST_URL} \
              -Dsonar.login=${SONAR_LOGIN}
          """
        }
      }
    }
  }


}