#!/usr/bin/groovy

def call() {
	def payloadString = "$payload"
	//parseWebhookPayload payload: payloadString
  
    env.repoURL = REPO_URL
    print REPO_URL
	env.repoName = repoURL.tokenize('/').last().replace(".git", "")
	env.appName = repoName
	
	env.branch = BRANCH_NAME
	
	currentBuild.displayName = repoName + ":" + branch + " #" + BUILD_NUMBER
	currentBuild.description = "Building repository: " + repoName + ", branch: " + branch
	
	slack.info message: 'STARTED', appendBuildInfo: true
}