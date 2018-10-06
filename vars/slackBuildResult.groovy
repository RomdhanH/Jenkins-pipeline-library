#!/usr/bin/groovy

def call(String buildResult) {
	
	def message = appendBuildInfo "Job result: " + buildResult
																
	if ( buildResult == "SUCCESS") {
		slack.success message
	}
	else if( buildResult == "FAILURE" || buildResult == "NOT_BUILT" ) { 
		slack.failure message
	}
	else if( buildResult == "UNSTABLE" || buildResult == "ABORTED" ) { 
		slack.warning message
	}
	else {
		slack.failure message
	}
}