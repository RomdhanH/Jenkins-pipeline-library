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
      stage ('Verify'){
        
        try {
            input id: 'Deploy', message: 'Is Blue node fine? Proceed with Green node deployment?', ok: 'Deploy!'
              sh "ls -la"
        } catch (error) {
            sh "ls -la"
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
                buildDockerImage(devProject)
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
				
              deployImage project: devProject, version: "latest", replicas: 1
            }
        }
      
       stage ('Promote to Test') {			
			when {
                expression { return branch == "develop" }
            }
			steps {
				timeout(time:30, unit:'MINUTES') {
					input message: "Promote to Test?", ok: "Promote"
				}
				script{
					// Tag for Test
					openshift.withCluster() {
                      openshift.tag("${devProject}/${appName}-${apiVersion}:latest", "${testProject}/${appName}-${apiVersion}:${buildVersion_lowercase}")
                    }
				}
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
			
				sh "oc delete hpa -l app=${appName}-${apiVersion} -n ${testProject} || true"
				
				sh "oc process -f cicd/iamp-service-config-test.yaml -l commit=${cicdCommit} | oc create -f- -n ${testProject} || true"
				
				deployImage project: testProject, version: buildVersion_lowercase, replicas: 1
            }
        }
      stage("create test route"){
       when {
                expression { return branch == "develop" }
            }
        steps{
          sh "oc process -f cicd/iamp-spring-service-prod-route-template.yaml -p APP_NAME=${appName} -p API_VERSION=${apiVersion} -l commit=${cicdCommit} -l app=${appName}-${apiVersion} | oc create -f- -n ${testProject} || true"
        }
      }
        
      stage("Switch route") {
       when {
                expression { return branch == "develop" }
            }
            steps {
			
				switchroute()
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
