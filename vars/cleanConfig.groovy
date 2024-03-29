#!/usr/bin/groovy

def call(project) {
	
	def command
	
	// Clean ConfigMap
	if((project == devProject && cmDevChanged.toBoolean()) || (project == testProject && cmTestChanged.toBoolean())){
		command = "oc delete cm iamp-service-config -n " + project + " || true"
		
		sh command
	}
	
	// Clean deployment configurations
	if((project == devProject && dcDevChanged.toBoolean()) || (project == testProject && dcTestChanged.toBoolean())){
		command = "oc process -f cicd/iamp-spring-service-template.yaml -p APP_NAME=${appName} -p API_VERSION=${apiVersion} -p BUILD_VERSION=${buildVersion_lowercase} -p IMAGE_NAME=${appName}-${apiVersion} -p IMAGE_TAG=latest | oc delete -f- -n " + project + " || true"
		
		sh command
	}	
}