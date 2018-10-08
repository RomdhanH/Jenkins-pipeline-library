#!/usr/bin/groovy

import jenkins.model.*

def call() {

	def changedFiles = []
	
	for (changeLog in currentBuild.changeSets) {
        for(entry in changeLog.items) {
            for(file in entry.affectedFiles) {
                changedFiles.add(file.editType.name)
            }
        }
    }

	return changedFiles;
}