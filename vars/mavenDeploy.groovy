#!/usr/bin/groovy

def call() {
	
	def cred = getCredentials('nexus_deployment')

	env.NEXUS_USR = cred.username
	env.NEXUS_PSW = cred.password.value
	
	sh "mvn deploy -DskipTests=true -Dinternal.repo.username=$NEXUS_USR -Dinternal.repo.password=$NEXUS_PSW"
}