#!/usr/bin/groovy

def call() {
	
	env.NEXUS = credentials('nexus_deployment')
	
	sh "${mvnCmd} deploy -DskipTests=true -Dinternal.repo.username=$NEXUS_USR -Dinternal.repo.password=$NEXUS_PSW"
}