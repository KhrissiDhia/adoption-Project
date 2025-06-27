pipeline {
  agent any

  environment {
    SONAR_PROJECT_KEY = 'adoption-project'
    SONAR_HOST_URL    = 'http://localhost:9000'
    SONAR_LOGIN       = credentials('sonar11')
    DOCKER_IMAGE      = 'monuser/adoption-app'
    DOCKER_CREDENTIALS= 'dockerhub-creds'
    NEXUS_CREDENTIALS = 'nexus-creds'
    NEXUS_URL         = 'http://172.30.93.238:8081/repository/maven-snapshots/'
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
        script {
          def jar = sh(script: "find target -name '*.jar' -not -name '*-original.jar' | head -n 1", returnStdout: true).trim()
          env.JAR_NAME = jar.replaceAll('target/', '')
          echo "JAR détecté : ${env.JAR_NAME}"
        }
      }
    }

    stage('🔍 Analyse SonarQube') {
      steps {
        withCredentials([string(credentialsId: 'sonar11', variable: 'SONAR_TOKEN')]) {
          withSonarQubeEnv('sonar') {
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

    stage('📤 Deploy Nexus') {
      steps {
        withCredentials([usernamePassword(
          credentialsId: NEXUS_CREDENTIALS,
          usernameVariable: 'NEXUS_USERNAME',
          passwordVariable: 'NEXUS_PASSWORD'
        )]) {
          writeFile file: 'settings-nexus.xml', text: """
    <settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                                  https://maven.apache.org/xsd/settings-1.0.0.xsd">
      <servers>
        <server>
          <id>deploymentRepo</id>
          <username>${NEXUS_USERNAME}</username>
          <password>${NEXUS_PASSWORD}</password>
        </server>
      </servers>
    </settings>
          """

          sh """
            mvn deploy -s settings-nexus.xml -DskipTests
          """
        }
      }
    }


    stage('🐳 Build Docker') {
      steps {
        script {
          sh """
            docker build \
              --build-arg JAR_FILE=${env.JAR_NAME} \
              -t ${DOCKER_IMAGE}:latest \
              -t ${DOCKER_IMAGE}:${env.BUILD_NUMBER} .
          """
        }
      }
    }

    stage('📤 Push Docker') {
      steps {
        withCredentials([usernamePassword(
          credentialsId: DOCKER_CREDENTIALS,
          usernameVariable: 'DOCKER_USER',
          passwordVariable: 'DOCKER_PASS'
        )]) {
          sh """
            echo "${DOCKER_PASS}" | docker login -u "${DOCKER_USER}" --password-stdin
            docker push ${DOCKER_IMAGE}:latest
            docker push ${DOCKER_IMAGE}:${env.BUILD_NUMBER}
            docker logout
          """
        }
      }
    }

    stage('🚀 Docker Compose') {
      steps {
        script {
          sh 'docker-compose down || true'
          sh 'docker-compose up -d --build'
          sh 'sleep 30'
        }
      }
    }
  }

  post {
    always {
      script {
        echo '✅ Pipeline terminé.'
        // Nettoyage des containers
        sh 'docker-compose down || true'
      }
    }
    failure {
      script {
        echo '❌ Pipeline échoué, vérifiez les logs.'
        // Archivage des logs en cas d'échec
        archiveArtifacts artifacts: '**/target/*.log', allowEmptyArchive: true
      }
    }
  }
}