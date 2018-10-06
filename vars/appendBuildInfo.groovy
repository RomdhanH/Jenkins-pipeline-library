#!/usr/bin/groovy

def call(String message) {
	def msg = 	message +
					"\n Job: ${JOB_NAME}" +
					"\n App: ${repoName} (branch: ${branch})" +
					"\n <${RUN_DISPLAY_URL}|Build #${BUILD_NUMBER}>  |  <${BUILD_URL}changes|Changes>  |  <${BUILD_URL}console|Logs>"
	return msg
}