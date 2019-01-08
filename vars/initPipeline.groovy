#!/usr/bin/groovy

def call() {
	//def payloadString = "$payload"
	//parseWebhookPayload payload: payloadString
  
    //env.repoURL = payloadObject.repository.clone_url                            
	env.repoName = readMavenPom().getArtifactId() //repoURL.tokenize('/').last().replace(".git", "")
	env.appName = repoName
	
	env.branch = BRANCH_NAME //ref.replace("refs/heads/", "")
	
	//currentBuild.displayName = repoName + ":" + branch + " #" + BUILD_NUMBER
	//currentBuild.description = "Building repository: " + repoName + ", branch: " + branch
	
	slack.info message: 'STARTED', appendBuildInfo: true
}