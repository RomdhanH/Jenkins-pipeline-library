#!/usr/bin/groovy

def call() {
	env.cicdRepoURL = 'http://gogs-iamp.pathfinder.gov.bc.ca/iamp/cicd.git'
	
	gitCheckout repoURL: cicdRepoURL, branch: 'master', directory: 'cicd', credentialsId: 'jenkins-gogs'
	
	sh "cp -f $WORKSPACE/cicd/maven-settings.xml /home/jenkins/.m2/settings.xml"
}