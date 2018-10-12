pipeline {
  agent {
    docker {
      image 'maven:3-alpine'
      args '-v /root/.m2:/root/.m2'
    }

  }
  stages {
    stage('Build') {
      steps {
        echo 'Building...'
        sh 'mvn -B -DskipTests clean package'
      }
    }
    stage('Deploy') {
      steps {
        echo 'Deploying...'
        sh 'mvn test'
      }
    }
  }
}