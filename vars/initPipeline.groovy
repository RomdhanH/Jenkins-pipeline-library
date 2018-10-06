#!/usr/bin/groovy

def call() {
	def payloadString = "$payload"
	parseWebhookPayload payload: payloadString
	
	currentBuild.displayName = repoName + ":" + branch + " #" + BUILD_NUMBER
	currentBuild.description = "Building repository: " + repoName + ", branch: " + branch
	
	slack.info message: 'STARTED', appendBuildInfo: true
		
	def cred = getCredentials('nexus_deployment')

	echo cred
	echo cred.username
	echo cred.password
}