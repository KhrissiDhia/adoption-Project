pipeline {
  agent any

  tools {
    maven 'M3'  // Assurez-vous que Maven est configuré dans Jenkins
    jdk 'jdk17' // Configurez un JDK 17 dans Jenkins
  }

  environment {
    // Configuration SonarQube
    SONAR_PROJECT_KEY = 'adoption-project'
    SONAR_HOST_URL = 'http://localhost:9000'

    // Configuration Maven
    MAVEN_OPTS = '-Xmx1024m -XX:MaxPermSize=512m'
  }

  stages {
    stage('🛠️ Initialisation') {
      steps {
        script {
          echo "Début du pipeline sur ${env.JOB_NAME}"
          echo "Version Java: ${sh(script: 'java -version', returnStdout: true)}"
          echo "Version Maven: ${sh(script: 'mvn -v', returnStdout: true)}"
        }
      }
    }

    stage('🧹 Nettoyage') {
      steps {
        sh 'mvn clean --quiet'
      }
    }

    stage('⚙️ Compilation') {
      steps {
        sh 'mvn compile --quiet'
      }
    }

    stage('🧪 Tests Unitaires') {
      steps {
        sh 'mvn test -Dtest=AdoptionServicesImplMockitoTest,AdoptionServicesImplTest'
        junit 'target/surefire-reports/**/*.xml'  // Archivage des résultats des tests
      }
    }

    stage('📦 Packaging') {
      steps {
        sh 'mvn package -DskipTests --quiet'
        archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
      }
    }

    stage('🔍 Analyse SonarQube') {
      steps {
        script {
          withSonarQubeEnv('sonar') {  // Doit correspondre au nom dans Jenkins
            withCredentials([string(credentialsId: 'sonarqu', variable: 'SONAR_TOKEN')]) {
              def sonarCmd = """
                mvn -B org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184:sonar \
                  -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                  -Dsonar.host.url=${SONAR_HOST_URL} \
                  -Dsonar.login=${SONAR_TOKEN} \
                  -Dsonar.java.source=17 \
                  -Dsonar.sourceEncoding=UTF-8 \
                  -Dsonar.qualitygate.wait=true \
                  -Dsonar.scm.disabled=true
              """
              sh(script: sonarCmd, label: "Analyse SonarQube")
            }
          }
        }
      }
    }
  }

  post {
    always {
      script {
        echo "Pipeline terminé - Statut: ${currentBuild.currentResult}"
        cleanWs()  // Nettoyage de l'espace de travail
      }
    }
    success {
      slackSend(color: 'good', message: "SUCCÈS: Job ${env.JOB_NAME} - Build ${env.BUILD_NUMBER}")
    }
    failure {
      slackSend(color: 'danger', message: "ÉCHEC: Job ${env.JOB_NAME} - Build ${env.BUILD_NUMBER}")
      emailext (
        subject: "ÉCHEC du pipeline: ${env.JOB_NAME} - Build ${env.BUILD_NUMBER}",
        body: "Veuillez vérifier le build: ${env.BUILD_URL}",
        to: 'votre-email@example.com'
      )
    }
    unstable {
      slackSend(color: 'warning', message: "INSTABLE: Job ${env.JOB_NAME} - Build ${env.BUILD_NUMBER}")
    }
  }
}