def call(project) {
def dest = "${appName}-${apiVersion}-${buildVersion_lowercase}"
input "Switch Production?"
    sh 'oc patch route ${appName}-${apiVersion} -p \'{"spec":{"to":{"name":"' + dest + '"}}}\''
    sh 'oc get route ${appName}-${apiVersion} > oc_out.txt'
    oc_out = readFile('oc_out.txt')
    echo "Current route configuration: " + oc_out
  
}