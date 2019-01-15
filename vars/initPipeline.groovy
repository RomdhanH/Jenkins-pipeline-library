#!/usr/bin/groovy

def call() {  
   
  
  sh "ls -la"
  
  	env.version = readMavenPom().getVersion()
	env.repoName = readMavenPom().getArtifactId()
  	//env.APIVersion = 
  //sh "printenv | sort"
  sh "mkdir $repoName"
  sh "rsync -a * ./$repoName"
  sh "ls -la"
  sh "ls -la $repoName"
	env.appName = repoName
	
	env.branch = BRANCH_NAME 
	
	slack.info message: 'STARTED', appendBuildInfo: true
}