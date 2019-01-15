def call(project) {
def dest = "${appName}-${apiVersion}-${buildVersion_lowercase}"
input "Switch Production?"
    sh 'oc patch route ${appName}-${apiVersion} -p \'{"spec":{"to":{"name":"' + dest + '"}}}\'' -n ${testProject}
    sh 'oc get route ${appName}-${apiVersion} -o yaml'
    
  
}