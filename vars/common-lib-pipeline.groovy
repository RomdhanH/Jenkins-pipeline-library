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
    /* stage('OWASP Scan') {
            steps {
                dir("${appName}") {
                    mavenOwaspScan()
                }
            }
        }*/
      
      
    }
	post('Publish Results') {
        always {
            slackBuildResult()
        	  }
    	}
    
    
}
}
