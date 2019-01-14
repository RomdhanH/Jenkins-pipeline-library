#!/usr/bin/groovy

def call(project) {
	
	def appJar = appName + "/target/" + appName + "-" + version + "-bin.jar"
		
	sh "rm -rf oc-build && mkdir -p oc-build/deployments"
	sh "cp ${appJar} oc-build/deployments/"
	
	// Create build and image streams
	sh "oc new-build --name=${appName} registry.access.redhat.com/jboss-fuse-6/fis-java-openshift:2.0 --binary=true --labels=app=${appName} -n project || true"
	
	// Start the build
	sh "oc start-build ${appName} --from-dir=oc-build/deployments --wait=true -n project"
}