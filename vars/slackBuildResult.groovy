#!/usr/bin/groovy

def call(String buildResult) {
	
	def message = appendBuildInfo "Job result: " + buildResult
																
	if ( buildResult == "SUCCESS") {
		slack.success message, true
	}
	else if( buildResult == "FAILURE" || buildResult == "NOT_BUILT" ) { 
		slack.failure message, true
	}
	else if( buildResult == "UNSTABLE" || buildResult == "ABORTED" ) { 
		slack.warning message, true
	}
	else {
		slack.failure message, true
	}
}