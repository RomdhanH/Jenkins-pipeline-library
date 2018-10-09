#!/usr/bin/groovy

def call() {
	env.devProject = 'ag-pssg-is-dev'
	
	env.testProject = 'ag-pssg-is-test'
	
	env.cicdCommit = sh(script: 'cd cicd;git rev-parse --short HEAD;cd ..', returnStdout: true).trim()
	echo cicdCommit
	
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
	
	env.devChanged = cmDevChanged || dcDevChanged
	
	env.testChanged = cmTestChanged || dcTestChanged
}