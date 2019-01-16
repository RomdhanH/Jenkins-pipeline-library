#!/usr/bin/groovy

def call() {  
  
  
        //sh "ls -la"

        env.version = readMavenPom().getVersion()
        env.repoName = readMavenPom().getArtifactId()
        //env.APIVersion = 
        //sh "printenv | sort"
        sh "mkdir $repoName"
        sh "rsync -a * ./$repoName"

env.WORKSPACE = pwd()
def version = readFile "${env.WORKSPACE}/src/main/resources/application.properties"



        env.appName = repoName
        env.branch = BRANCH_NAME 
        env.apiVersion = "v1"

      	//def content = readFile 'hello-world/src/main/resources/application.properties'
        /*
        Properties properties = new Properties()
        InputStream is = new ByteArrayInputStream(content.getBytes());
        properties.load(is)

        def runtimeString = 'SERVICE_VERSION'
        echo properties."$service.version"
        SERVICE_VERSION = properties."$service.version"
        echo SERVICE_VERSION*/

        slack.info message: 'STARTED', appendBuildInfo: true
}