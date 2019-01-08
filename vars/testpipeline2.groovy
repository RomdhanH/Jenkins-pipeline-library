import hudson.model.*
def call(Closure body) {
pipeline {
  
	agent { node { label 'maven' } }

	stages {
      
		stage('Init Pipeline') {
            steps {
             parallel (
                    "Init Pipeline": {
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
              //gitCheckout repoURL: repoURL, branch: branch, directory: appName, credentialsId: 'jenkins-gogs'
              
                dir("${appName}") {
                    mavenBuild()
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