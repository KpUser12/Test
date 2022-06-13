pipeline {
  agent { docker { image 'cameronmcnz/ant-jdk8-git:latest' } }
  stages {
    stage('GitHub Jenkins Ant Docker Build') {
      steps {
        git 'https://github.com/learn-devops-fast/rps-ant.git'
        sh 'ant clean compile test package war'
      }
    }
  }
}
