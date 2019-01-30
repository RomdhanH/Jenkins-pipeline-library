#!/usr/bin/groovy

def call() {
	env.cicdRepoURL = 'https://github.com/RomdhanH/cicd.git'
	
	gitCheckout repoURL: cicdRepoURL, branch: 'master', directory: 'cicd', credentialsId: 'iamp-jenkins'
	
	sh "cp -f $WORKSPACE/cicd/maven-settings.xml /home/jenkins/.m2/settings.xml"
}
