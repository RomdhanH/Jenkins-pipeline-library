#!/usr/bin/groovy

def call() {
	env.cicdRepoURL = 'http://gogs-iamp.pathfinder.gov.bc.ca/iamp/cicd.git'
	
	gitCheckout repoURL: cicdRepoURL, branch: 'master', directory: 'cicd', credentialsId: 'jenkins-gogs'
	
	env.mvnCmd = "mvn -s $WORKSPACE/cicd/maven-settings.xml"
}