#!/usr/bin/groovy

def call() {
	env.devProject = 'ag-pssg-is-dev'
	
	env.testProject = 'ag-pssg-is-test'
	
	dir('cicd') {
		env.cicdCommit = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
		env.cicdChangedFile = sh(script: "git show --pretty=\"\" --name-only ${cicdCommit}", returnStdout: true).trim()
	}

	openshift.withCluster() {
		openshift.withProject(devProject) {			
			env.bcDevChanged = !openshift.selector("bc", [app:"${appName}", commit:"${cicdCommit}"]).exists()
			env.cmDevChanged = !openshift.selector("cm", [commit:"${cicdCommit}"]).exists()
			env.dcDevChanged = !openshift.selector("dc", [app:"${appName}", commit:"${cicdCommit}"]).exists()
		}
		
		openshift.withProject(testProject) {
			env.cmTestChanged = !openshift.selector("cm", [commit:"${cicdCommit}"]).exists()
			env.dcTestChanged = !openshift.selector("dc", [app:"${appName}", commit:"${cicdCommit}"]).exists()
		}
	}
	
	env.devChanged = cmDevChanged.toBoolean() || dcDevChanged.toBoolean()
	
	env.testChanged = cmTestChanged.toBoolean() || dcTestChanged.toBoolean()
}