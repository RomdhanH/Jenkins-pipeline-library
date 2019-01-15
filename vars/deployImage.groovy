#!/usr/bin/groovy

def call(template) {
  
				
  def command = "oc process -f ${template} -p APP_NAME=${appName} -p IMAGE_NAME=${appName} -p IMAGE_TAG=" + config.version + " -p REPLICAS=" + config.replicas + " -l app=${appName},commit=${cicdCommit},hystrix.enabled=true | oc create -f- -n " + config.project + " || true"
	
	sh command
	
	script{
		openshift.withCluster() {
			openshift.withProject(config.project) {
				def dcSelector = openshift.selector("dc", "${appName}")
				dcSelector.rollout().latest()
				def deployment = "${appName}-${dcSelector.object().status.latestVersion}"
				def pods = openshift.selector('pods', [deployment: "${deployment}"])
				
				timeout(5) {
					pods.untilEach(config.replicas) {
						return it.object().status.containerStatuses.every {
							it.ready
						}
					}
				}
			}
		}
	}
}