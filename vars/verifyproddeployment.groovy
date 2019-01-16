 
def call () {

try {
            input id: 'Rollback', message: 'Is the realise fine? Proceed with the rollback?', ok: 'Rollback!'
            sh  'oc patch route ${appName}-${apiVersion} -p \'{"spec":{"to":{"name":"' + dest + '"}}}\' -n ${testProject} || true'
  			sh 'oc patch route ${appName}-${apiVersion}-${buildVersion_lowercase} -p \'{"spec":{"to":{"name":"' + xy + '"}}}\' -n ${testProject} || true'
        } catch (error) {
         sh  'oc delete pods,services -l name=${appName}-${apiVersion}-${buildVersion_lowercase}'
        }
  
}