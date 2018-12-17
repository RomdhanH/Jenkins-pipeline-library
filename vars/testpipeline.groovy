#!groovy
//@Library('jenkins-pipeline-library')
import hudson.model.*
def call(Closure body) {
    stage ('Build')
        node ('maven') {
            checkout scm
            initCICD()
            sh 'mvn clean package'
      }
  body()
}