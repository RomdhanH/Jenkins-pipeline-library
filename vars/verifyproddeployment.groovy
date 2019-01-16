 
def call () {

try {
            input id: 'Deploy', message: 'Is Blue node fine? Proceed with Green node deployment?', ok: 'Deploy!'
            sh "ls -la"
        } catch (error) {
           sh "ls -la"
        }
  
}