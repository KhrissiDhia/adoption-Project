pipeline {
  agent any

  environment {
    SONAR_PROJECT_KEY = 'adoption-project'
    SONAR_HOST_URL    = 'http://localhost:9000'
    SONAR_LOGIN       = credentials('sonar11')
    DOCKER_IMAGE      = 'monuser/adoption-app'
    DOCKER_CREDENTIALS= 'dockerhub-creds'
    NEXUS_CREDENTIALS = 'nexus-creds'
    NEXUS_URL         = 'http://nexus.example.com/repository/maven-releases/'
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
      steps {
        sh 'mvn package -DskipTests'
        script {
          def jar = sh(script: "ls target/*.jar | grep -v 'original' | head -n 1", returnStdout: true).trim()
          env.JAR_NAME = jar.replaceAll('target/', '')
          echo "JAR détecté : ${env.JAR_NAME}"
        }
      }
    }

    stage('🔍 Analyse SonarQube') {
      steps {
        withCredentials([string(credentialsId: 'sonar11', variable: 'SONAR_TOKEN_SECURE')]) {
          withSonarQubeEnv('sonar') {
            sh '''
              mvn sonar:sonar \
                -Dsonar.projectKey=$SONAR_PROJECT_KEY \
                -Dsonar.host.url=$SONAR_HOST_URL \
                -Dsonar.login=$SONAR_TOKEN_SECURE
            '''
          }
        }
      }
    }
   stage('🛡️ Quality Gate') {
     steps {
       timeout(time: 1, unit: 'HOURS') {
         waitForQualityGate abortPipeline: false
       }
     }
   }
    stage('📤 Deploy Nexus') {
      steps {
        withCredentials([usernamePassword(credentialsId: NEXUS_CREDENTIALS, usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {
          sh """
            mvn deploy:deploy-file \
              -Durl=$NEXUS_URL \
              -DrepositoryId=nexus \
              -Dfile=target/${env.JAR_NAME} \
              -DgroupId=com.example.adoption \
              -DartifactId=adoption-project \
              -Dversion=1.0.${env.BUILD_NUMBER} \
              -Dpackaging=jar \
              -DgeneratePom=true \
              -DrepositoryLayout=default \
              -Dusername=$NEXUS_USER \
              -Dpassword=$NEXUS_PASS
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
        withCredentials([usernamePassword(credentialsId: DOCKER_CREDENTIALS, usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
          sh """
            echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
            docker push ${DOCKER_IMAGE}:latest
            docker logout
          """
        }
      }
    }

    stage('🚀 Docker Compose') {
      steps {
        sh 'docker-compose stop springboot-app || true'
        sh 'docker-compose up -d --build springboot-app'
        sh 'sleep 30'
      }
    }
  }

  post {
    always {
      echo '✅ Pipeline terminé.'
    }
    failure {
      echo '❌ Pipeline échoué, vérifie les logs.'
    }
  }
}
