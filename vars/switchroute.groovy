def call(project) {
def dest = ${APP_NAME}-${API_VERSION}-${BUILD_VERSION}
input "Switch Production?"
    sh 'oc patch route ${APP_NAME}-${API_VERSION} -p \'{"spec":{"to":{"name":"' + dest + '"}}}\''
    sh 'oc get route ${APP_NAME}-${API_VERSION} > oc_out.txt'
    oc_out = readFile('oc_out.txt')
    echo "Current route configuration: " + oc_out
  
}