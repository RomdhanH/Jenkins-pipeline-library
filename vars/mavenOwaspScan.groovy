#!/usr/bin/groovy

def call() {
	
	sh "mvn -X dependency-check:check"
}