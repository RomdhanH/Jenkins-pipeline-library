#!/usr/bin/groovy

def call() {
	env.devProject = 'ag-pssg-is-dev'
	
	env.testProject = 'ag-pssg-is-test'
	
	dir('cicd') {
		env.cicdCommit = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
		env.cicdChangedFile = sh(script: "git show --pretty=\"\" --name-only ${cicdCommit}", returnStdout: true).trim()
		echo cicdCommit
		echo cicdChangedFile
	}

	openshift.withCluster() {
		openshift.withProject(devProject) {			
			env.bcDevChanged = (!openshift.selector("bc", [app:"${appName}", commit:"${cicdCommit}"]).exists() && cicdChangedFile.contains('iamp-image-builder-template.yaml')
			env.cmDevChanged = !openshift.selector("cm", [commit:"${cicdCommit}"]).exists() && cicdChangedFile.contains('iamp-service-config-dev.yaml')
			env.dcDevChanged = !openshift.selector("dc", [app:"${appName}", commit:"${cicdCommit}"]).exists() && cicdChangedFile.contains('iamp-spring-service-template.yaml')
		}
		
		openshift.withProject(testProject) {
			env.cmTestChanged = !openshift.selector("cm", [commit:"${cicdCommit}"]).exists() && cicdChangedFile.contains('iamp-service-config-test.yaml')
			env.dcTestChanged = !openshift.selector("dc", [app:"${appName}", commit:"${cicdCommit}"]).exists() && cicdChangedFile.contains('iamp-spring-service-template.yaml')
		}
	}
	
	env.devChanged = cmDevChanged.toBoolean() || dcDevChanged.toBoolean()
	
	env.testChanged = cmTestChanged.toBoolean() || dcTestChanged.toBoolean()
}