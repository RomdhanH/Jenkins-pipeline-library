#!/usr/bin/groovy

def call() {  
  
  sh "ls -la"
  
	env.repoName = readMavenPom().getArtifactId()
  sh "mkdir $repoName"
  sh "mv * ./$repoName"
  sh "ls -la"
  sh "ls -la $repoName"
	env.appName = repoName
	
	env.branch = BRANCH_NAME 
	
	slack.info message: 'STARTED', appendBuildInfo: true
}