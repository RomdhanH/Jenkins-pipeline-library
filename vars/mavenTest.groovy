#!/usr/bin/groovy

def call() {
	
	sh "${mvnCmd} test"
}