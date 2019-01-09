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
      stage('Unit Testing') {
            steps {
                dir("${appName}") {
                    mavenTest()
                }
            }
        }
       stage('SonarQube analysis') {
         steps {
   			 withSonarQubeEnv('Sonar') {
    		  sh 'mvn clean package sonar:sonar'
   			 } // SonarQube taskId is automatically attached to the pipeline context
  			}
       }
     /*stage('OWASP Scan') {
            steps {
                dir("${appName}") {
                    mavenOwaspScan()
                }
            }
        }
      */
      
    }
	post('Publish Results') {
        always {
            slackBuildResult()
        	  }
    	}
    
    
}
}
