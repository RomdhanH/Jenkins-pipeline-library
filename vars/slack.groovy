#!/usr/bin/groovy

def info(message) {
    slackSend color: "46c9e2", message: ${message}
}

def warning(message) {
    slackSend color: "warning", message: ${message}
}

def success(message) {
    slackSend color: "good", message: ${message}
}

def failure(message) {
    slackSend color: "danger", message: ${message}
}