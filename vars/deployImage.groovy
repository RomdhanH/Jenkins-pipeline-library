#!/usr/bin/groovy

def call(Map config) {
  
				
  def command = "oc process -f cicd/iamp-spring-service-template.yaml -p APP_NAME=${appName} -p API_VERSION=${apiVersion} -p BUILD_VERSION=${buildVersion_lowercase} -p IMAGE_NAME=${appName}-${apiVersion} -p IMAGE_TAG=" + config.version + " -p REPLICAS=" + config.replicas + " -l app=${appName}-${apiVersion},release=${buildVersion_lowercase},commit=${cicdCommit},hystrix.enabled=true | oc create -f- -n " + config.project + " || true"
	
	sh command
	
	script{
		openshift.withCluster() {
			openshift.withProject(config.project) {
				def dcSelector = openshift.selector("dc", "${appName}-${apiVersion}-${buildVersion_lowercase}")
				dcSelector.rollout().latest()
				def deployment = "${appName}-${apiVersion}-${buildVersion_lowercase}-${dcSelector.object().status.latestVersion}"
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