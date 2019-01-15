def call(project) {
def dest = "${appName}-${apiVersion}-${buildVersion_lowercase}"
def xy = "abord"
input "Switch Production?"
    sh 'oc patch route ${appName}-${apiVersion} -p \'{"spec":{"to":{"name":"' + dest + '"}}}\' -n ${testProject} || true'
  sh 'oc patch route ${appName}-${apiVersion}-${buildVersion_lowercase} -p \'{"spec":{"to":{"name":"' + xy + '"}}}\' -n ${testProject} || true'
   // sh 'oc get route ${appName}-${apiVersion} -o yaml'
    
  
}