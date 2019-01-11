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
      stage('Unit & Integration Testing') {
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
       /* stage('OWASP Scan') {
            steps {
                dir("${appName}") {
                    mavenOwaspScan()
                }
            }
        }*/
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
      stage('Check Config Changes') {
            when {
                expression { return branch == "develop" }
            }
            steps {
                chechConfigChanges()
            }
        }
      stage("Build Image") {
            when {
                expression { return branch == "develop" }
            }
            steps {
                buildDockerImage()
            }
        }
      stage('Cleanup Dev') {
			when {
				expression{ return (branch == "develop" && devChanged.toBoolean()) }
			}
			steps {
				cleanConfig(devProject)
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
