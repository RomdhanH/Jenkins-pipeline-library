@Library('jenkins-pipeline-library')
import hudson.model.*
pipeline {
	agent { node { label 'maven' } }
	stages {
		stage('Init Pipeline') {
            steps {
                parallel (
                    "Parse WebHook": {
                        initPipeline()
                    },
                    "Init CICD": {
                        initCICD()
                    }
                )
            }
        }
        
        stage ("Checkout & Build") {
            steps {
                gitCheckout repoURL: repoURL, branch: branch, directory: appName, credentialsId: 'jenkins-gogs'
                
                dir("${appName}") {
                    mavenBuild()
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
		
        stage("Deploy to Dev") {
            when {
                expression { return branch == "develop" }
            }
            steps {
				sh "oc process -f cicd/iamp-service-config-dev.yaml -l commit=${cicdCommit} | oc create -f- -n ${devProject} || true"
				
				deployImage project: devProject, version: 'latest', replicas: 1
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
						openshift.tag("${devProject}/${appName}:latest", "${testProject}/${appName}:${version}")
                    }
				}
			}
		}
		
		/*stage('Cleanup Test') {
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
			
				sh "oc delete hpa -l app=${appName} -n ${testProject} || true"
				
				sh "oc process -f cicd/iamp-service-config-test.yaml -l commit=${cicdCommit} | oc create -f- -n ${testProject} || true"
				
				deployImage project: testProject, version: version, replicas: 2
            }
        }*/
    }
	
	post('Publish Results') {
        always {
            slackBuildResult()
        }
    }
}