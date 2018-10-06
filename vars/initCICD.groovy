#!/usr/bin/groovy

def call(Map config) {
	echo "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXx"
	env.cicdRepo = 'http://gogs-iamp.pathfinder.gov.bc.ca/iamp/cicd.git'
	
	echo cicdRepo
	
	checkout([$class: 'GitSCM', 
		branches: [[name: '*/master']], 
		doGenerateSubmoduleConfigurations: false, 
		extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'cicd']], 
		submoduleCfg: [], 
		userRemoteConfigs: [[credentialsId: 'jenkins-gogs', url: "${cicdRepo}"]]
	])
	
	env.mvnCmd = "mvn -s $WORKSPACE/cicd/maven-settings.xml"
	
	echo mvnCmd
}