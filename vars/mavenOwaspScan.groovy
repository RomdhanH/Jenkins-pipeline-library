#!/usr/bin/groovy

def call() {
	
	sh "${mvnCmd} dependency-check:check"
}