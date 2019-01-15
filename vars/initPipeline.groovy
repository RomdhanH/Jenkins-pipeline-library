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
  
   def content = readFile '$repoName/src/main/resources/application.properties'

Properties properties = new Properties()
InputStream is = new ByteArrayInputStream(content.getBytes());
properties.load(is)

def runtimeString = 'SERVICE_VERSION'
echo properties."$service.version"
SERVICE_VERSION = properties."$service.version"
echo SERVICE_VERSION
	
	slack.info message: 'STARTED', appendBuildInfo: true
}