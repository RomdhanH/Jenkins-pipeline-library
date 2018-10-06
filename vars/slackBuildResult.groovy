#!/usr/bin/groovy

def call(String buildResult) {
	echo buildResult																
	if ( buildResult == "SUCCESS") {
		slack.success message: buildResult, appendBuildInfo: true
	}
	else if( buildResult == "FAILURE" || buildResult == "NOT_BUILT" ) { 
		slack.failure message: buildResult, appendBuildInfo: true
	}
	else if( buildResult == "UNSTABLE" || buildResult == "ABORTED" ) { 
		slack.warning message: buildResult, appendBuildInfo: true
	}
	else {
		slack.failure message: buildResult, appendBuildInfo: true
	}
}