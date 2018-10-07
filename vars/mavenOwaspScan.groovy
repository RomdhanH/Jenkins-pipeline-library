#!/usr/bin/groovy

def call() {
	
	sh "mvn dependency-check:check -DcentralAnalyzerEnabled=true"
}