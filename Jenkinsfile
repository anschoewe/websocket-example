pipeline {
  agent {
    docker {
      image 'maven:3-alpine'
      args '-v /root/.m2:/root/.m2'
    }

  }
  stages {
    stage('Build Jar') {
      steps {
        echo 'Building...'
        sh 'mvn -B -DskipTests clean package'
      }
    }
    stage('Test Java') {
      steps {
        echo 'Testing...'
        sh 'mvn test'
      }
    }
    stage('Build Docker Image') {
      agent {
        dockerfile {
          filename 'Dockerfile'
        }

      }
      steps {
        echo 'Deploying...'
      }
    }
  }
}
