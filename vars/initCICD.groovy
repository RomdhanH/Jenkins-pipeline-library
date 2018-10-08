#!/usr/bin/groovy

def call() {
	env.cicdRepoURL = 'http://gogs-iamp.pathfinder.gov.bc.ca/iamp/cicd.git'
	
	gitCheckout repoURL: cicdRepoURL, branch: 'master', directory: 'cicd', credentialsId: 'jenkins-gogs'
	
	sh "cp -f $WORKSPACE/cicd/maven-settings.xml /home/jenkins/.m2/settings.xml"
	
	changedFiles = getChangedFiles()	
	
	if (changedFiles.contains("iamp-image-builder-template.yaml")) { env.imageBuilderTemplateChanged = true }
	
	if (changedFiles.contains("iamp-spring-service-template.yaml")) { env.deploymentTemplateChanged = true }
	
	if (changedFiles.contains("service-autoscaler-cpu-template.yaml")) { env.autoScaleTemplateChanged = true }
	
	if (changedFiles.contains("iamp-service-config-dev.yaml")) { env.devConfigMapChanged = true }
	
	if (changedFiles.contains("iamp-service-config-test.yaml")) { env.testConfigMapChanged = true }
	
	env.devChanged = deploymentTemplateChanged || devConfigMapChanged
	
	env.testChanged = deploymentTemplateChanged || testConfigMapChanged
	
	env.devProject = 'ag-pssg-is-dev'
	
	env.testProject = 'ag-pssg-is-test'
}