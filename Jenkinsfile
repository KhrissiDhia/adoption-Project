pipeline {
  agent any

  environment {
    SONAR_PROJECT_KEY = 'adoption-project'
    SONAR_HOST_URL = 'http://localhost:9000'
    DOCKER_IMAGE = 'monuser/adoption-app'
    NEXUS_REPO_URL = 'http://172.30.93.238:8081/repository/maven-snapshots/'
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
          env.JAR_NAME = sh(
            script: "find target -name '*.jar' ! -name '*original*' -printf '%f'",
            returnStdout: true
          ).trim()
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
        withCredentials([
          usernamePassword(
            credentialsId: 'nexus-creds',
            usernameVariable: 'NEXUS_USER',
            passwordVariable: 'NEXUS_PASS'
          )
        ]) {
          sh """
            mvn deploy:deploy-file \
              -Durl=${NEXUS_REPO_URL} \
              -DrepositoryId=nexus \
              -Dfile=target/${env.JAR_NAME} \
              -DgroupId=com.example.adoption \
              -DartifactId=adoption-project \
              -Dversion=1.0.${env.BUILD_NUMBER} \
              -Dpackaging=jar \
              -DgeneratePom=true \
              -Dusername=${NEXUS_USER} \
              -Dpassword=${NEXUS_PASS}
          """
        }
      }
    }

    stage('🐳 Build Docker') {
      steps {
        sh "docker build --build-arg JAR_FILE=${env.JAR_NAME} -t ${DOCKER_IMAGE}:latest ."
      }
    }

    stage('📤 Push Docker') {
      steps {
        withCredentials([
          usernamePassword(
            credentialsId: 'dockerhub-creds',
            usernameVariable: 'DOCKER_USER',
            passwordVariable: 'DOCKER_PASS'
          )
        ]) {
          sh """
            echo \"${DOCKER_PASS}\" | docker login -u \"${DOCKER_USER}\" --password-stdin
            docker push ${DOCKER_IMAGE}:latest
            docker logout
          """
        }
      }
    }

    stage('🚀 Docker Compose') {
      steps {
        sh 'docker-compose down || true'
        sh 'docker-compose up -d --build'
        sh 'sleep 30'
      }
    }
  }

  post {
    always {
      echo '✅ Pipeline terminé - voir les résultats ci-dessus'
      cleanWs()
    }
    failure {
      echo '❌ ÉCHEC du pipeline - vérifiez les logs pour plus de détails'
      emailext body: 'Le pipeline a échoué, veuillez vérifier les logs',
              subject: 'Échec du Pipeline adoption-project',
              to: 'team@example.com'
    }
    success {
      echo '🎉 Pipeline exécuté avec succès!'
    }
  }
}