pipeline {
  agent any

  environment {
    // Token SonarQube
    SONAR_LOGIN = credentials('sonarqu')

    // Variables GitHub
    GIT_CREDENTIALS = 'GitJenkinsDevops'
    GIT_REPO = 'https://github.com/KhrissiDhia/adoption-Project.git'

    // Variables Docker (adapter si besoin)
    DOCKER_REGISTRY = 'docker.io'
    DOCKER_REPO = 'dhiakhrissi'
    DOCKER_IMAGE = 'adoption-project'
    DOCKER_CREDENTIALS = 'dockerhub-cred'  // à configurer dans Jenkins si tu fais Docker push
  }

  stages {

    stage('Checkout') {
      steps {
        git url: "${GIT_REPO}", credentialsId: "${GIT_CREDENTIALS}", branch: 'main'
      }
    }

    stage('Clean') {
      steps {
        sh 'mvn clean'
      }
    }

    stage('Compile') {
      steps {
        sh 'mvn compile'
      }
    }

    stage('Test') {
      steps {
        sh 'mvn test -Dtest=AdoptionServicesImplMockitoTest,AdoptionServicesImplTest'
      }
    }

    stage('Package') {
      steps {
        sh 'mvn package -DskipTests'
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


    // Optionnel : ajouter Docker build & push si tu veux
  }
}
