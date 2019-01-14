import hudson.model.*
  def version    = getVersionFromPom("./pom.xml")
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
 /*     stage('Determine Deployment color') {
         steps {
    determinedeploymentcolor()
         }
       }*/
        
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
      
  

  
      stage("Deploy to Dev") {
            when {
                expression { return branch == "develop" }
            }
            steps {
				sh "oc process -f cicd/iamp-service-config-dev.yaml -l commit=${cicdCommit} | oc create -f- -n ${devProject} || true"
				
				deployImage project: devProject, version: 'latest', replicas: 1
            }
        }
      stage('Cleanup Test') {
			when {
				expression{ return (branch == "develop" && testChanged.toBoolean()) }
			}
			steps {
				cleanConfig(testProject)
			}
        }
      stage("Deploy to Test") {
            when {
                expression { return branch == "develop" }
            }
            steps {
              
				sh "oc process -f cicd/iamp-service-config-dev.yaml -l commit=${cicdCommit} | oc create -f- -n ${testProject} || true"
				
              deployImage project: testProject, version: ${version}, replicas: 1
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
