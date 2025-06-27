pipeline {
  agent any

  environment {
    // === SONARQUBE CONFIGURATION ===
    SONAR_PROJECT_KEY = 'adoption-project'
    SONAR_HOST_URL = 'http://localhost:9000'
    SONAR_LOGIN = credentials('jenkinsDevops')  // token SonarQube valide

    // === DOCKER CONFIGURATION ===
    DOCKER_REGISTRY = 'docker.io'
    DOCKER_REPO = 'dhiakhrissi'                // Nom d'utilisateur Docker Hub
    DOCKER_IMAGE = 'adoption-project'          // Nom de l'image Docker
    DOCKER_CREDENTIALS = 'dockerhub-cred'      // ID du credential Docker dans Jenkins
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
          def jar = sh(script: "ls target/*.jar | grep -v 'original' | head -n 1", returnStdout: true).trim()
          env.JAR_NAME = jar.replaceAll('target/', '')
          echo "✅ JAR détecté : ${env.JAR_NAME}"
        }
      }
    }

    stage('🔍 Analyse SonarQube') {
      steps {
        withSonarQubeEnv('MySonarServer') {  // Nom du serveur configuré dans Jenkins > Configure System
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

    stage('🐳 Build Docker Image') {
      steps {
        script {
          dockerImage = docker.build("${DOCKER_REPO}/${DOCKER_IMAGE}:${env.BUILD_NUMBER}")
        }
      }
    }

    stage('🚀 Push Docker Image') {
      steps {
        script {
          docker.withRegistry("https://${DOCKER_REGISTRY}", DOCKER_CREDENTIALS) {
            dockerImage.push()
            dockerImage.push('latest')
          }
        }
      }
    }

    // ✅ Optionnel : Déploiement automatique d’un conteneur (si serveur supporte Docker)
    /*
    stage('⚡ Deploy Container') {
      steps {
        sh """
          docker pull ${DOCKER_REPO}/${DOCKER_IMAGE}:${env.BUILD_NUMBER}
          docker stop adoption-app || true
          docker rm adoption-app || true
          docker run -d --name adoption-app -p 8089:8089 ${DOCKER_REPO}/${DOCKER_IMAGE}:${env.BUILD_NUMBER}
        """
      }
    }
    */
  }
}
