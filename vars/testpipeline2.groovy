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
     /* stage("Quality Gate"){
        steps {
          timeout(time: 1, unit: 'HOURS') { 
              if (waitForQualityGate().status != 'OK') {
                  error "Pipeline aborted due to quality gate failure: ${qwaitForQualityGate().status}"
              }
          }
        }
      }*/
      
       stage('Sonar Scan') {
            steps {
                dir("${appName}") {
                    mavenSonarScan()
                }
            }
        }
       stage('Push Artifacts') {
            when {
                expression { return branch == "develop" }
            }
            steps {
                dir("${appName}") {
                    mavenDeploy()
                }
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
