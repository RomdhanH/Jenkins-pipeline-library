#!/usr/bin/groovy

def call() {  
  
  
        //sh "ls -la"
		//sh "printenv | sort"
        env.version = readMavenPom().getVersion()
        env.repoName = readMavenPom().getArtifactId()
        
       
        	sh "mkdir $repoName"
        	sh "rsync -a * ./$repoName"


		 	env.workspace = pwd()
 			def props = readProperties file: './src/main/resources/application.properties'
 		 	def Var1= props['service.version']
        	echo "Var1=${Var1}"

        env.appName = repoName
        env.branch = BRANCH_NAME 
        env.apiVersion = "${Var1}"

      

        slack.info message: 'STARTED', appendBuildInfo: true
}