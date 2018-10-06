#!/usr/bin/groovy

def info(message, appendBuildInfo) {
    slackSend color: "46c9e2", message: appendInfo(message, appendBuildInfo)
}

def warning(message, appendBuildInfo) {
    slackSend color: "warning", message: appendInfo(message, appendBuildInfo)
}

def success(message, appendBuildInfo) {
    slackSend color: "good", message: appendInfo(message, appendBuildInfo)
}

def failure(message, appendBuildInfo) {
    slackSend color: "danger", message: appendInfo(message, appendBuildInfo)
}

def appendInfo(message, appendBuildInfo) {
    if(appendBuildInfo){
        return message +
				"\n Job: ${JOB_NAME}" +
				"\n App: ${repoName} (branch: ${branch})" +
				"\n <${RUN_DISPLAY_URL}|Build #${BUILD_NUMBER}>  |  <${BUILD_URL}changes|Changes>  |  <${BUILD_URL}console|Logs>"
    }
    return message
}
