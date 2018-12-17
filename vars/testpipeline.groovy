#!groovy
@Library('jenkins-pipeline-library')
import hudson.model.*
stage ('Build')
node ('maven') {
    checkout scm
    initCICD()
    sh 'mvn clean package'
}