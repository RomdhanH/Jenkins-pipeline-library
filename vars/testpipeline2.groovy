import hudson.model.*
def call(Closure body) {
pipeline {
  
	agent { node { label 'maven' } }

	stages {
      
		stage("Init Pipeline") {
            steps {
             	initPipeline()
                 }
        }
      stage("Init cicd") {
            steps {
             	initCICD()
            	}
         }
 
        
        stage ("Checkout & Build") {
            steps {
              //gitCheckout repoURL: repoURL, branch: branch, directory: appName, credentialsId: 'jenkins-gogs'
              
                dir("${appName}") {
                    mavenBuild()
                }
            }
        }
       stage('SonarQube analysis') {
   			 withSonarQubeEnv('Sonar') {
    		  sh 'mvn clean package sonar:sonar'
   			 } // SonarQube taskId is automatically attached to the pipeline context
  			}
      stage("Quality Gate"){
 			 timeout(time: 1, unit: 'HOURS') {
   			 def qg = waitForQualityGate() 
   			 if (qg.status != 'OK') {
     		 error "Pipeline aborted due to quality gate failure: ${qg.status}"
   		 		}
  			}
		}
      
      
    }
	post('Publish Results') {
        always {
            slackBuildResult()
        	  }
    	}
    
    
}
}
