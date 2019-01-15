import jenkins.model.*

def call() {
	   

def project  = "testProject"
  def dest     = "${appName}-${apiVersion}-${version}"
  def active   = ""


// Determine current project
    sh "oc get project|grep -v NAME|awk '{print \$1}' >project.txt"
    project = readFile('project.txt').trim()
  sh "oc get route ${appName} -n ${project} -o jsonpath='{ .spec.to.name }' > activesvc.txt"

    // Determine currently active Service
    active = readFile('activesvc.txt').trim()
    if (active == "${appName}-${apiVersion}-${version}") {
      dest = "${appName}-${apiVersion}-${version}"
    }
  
}