 
def call () {

try {
            input id: 'Rollback', message: 'Is the realise fine? Proceed with the rollback?', ok: 'Rollback!'
            sh "ls -la"
        } catch (error) {
           sh "ls -la"
        }
  
}