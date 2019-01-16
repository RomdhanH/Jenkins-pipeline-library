#!/usr/bin/groovy

def call() {  
  
  
        //sh "ls -la"

        env.version = readMavenPom().getVersion()
        env.repoName = readMavenPom().getArtifactId()
        //env.APIVersion = 
        //sh "printenv | sort"
        sh "mkdir $repoName"
        sh "rsync -a * ./$repoName"


		 env.workspace = pwd()

 		 def props = readProperties file: '${env.workspace}/src/main/resources/application.properties'
 		 def Var1= props['service.version']
         echo "Var1=${Var1}"

        env.appName = repoName
        env.branch = BRANCH_NAME 
        env.apiVersion = "v1"

      

        slack.info message: 'STARTED', appendBuildInfo: true
}