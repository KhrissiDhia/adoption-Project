pipeline {
  agent any

  environment {
    SONAR_PROJECT_KEY = 'adoption-project'
    SONAR_HOST_URL = 'http://localhost:9000'
    SONAR_LOGIN = credentials('sonarqu')
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

    stage('📦 Package + Detect JAR') {
      steps {
        sh 'mvn package -DskipTests'
        script {
          sleep 10
          def jar = sh(script: "ls target/*.jar | grep -v 'original' | head -n 1", returnStdout: true).trim()
          env.JAR_NAME = jar.replaceAll('target/', '')
          echo "✅ JAR détecté : ${env.JAR_NAME}"
        }
      }
    }

    stage('🔍 Analyse SonarQube') {
      steps {
        withSonarQubeEnv('MySonarServer') {
          sh """
            mvn sonar:sonar \\
              -Dsonar.projectKey=${SONAR_PROJECT_KEY} \\
              -Dsonar.host.url=${SONAR_HOST_URL} \\
              -Dsonar.login=${SONAR_LOGIN}
          """
        }
      }
    }

    stage('📤 Deploy Nexus') {
      steps {
        sh 'mvn deploy -DskipTests'
      }
    }
  }
}
