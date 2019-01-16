#!/usr/bin/groovy

def call() {  
  
  
        //sh "ls -la"

        env.version = readMavenPom().getVersion()
        env.repoName = readMavenPom().getArtifactId()
        //env.APIVersion = 
        //sh "printenv | sort"
        sh "mkdir $repoName"
        sh "rsync -a * ./$repoName"

env.WORKSPACE = pwd()
/*  Properties properties = new Properties()
def propertiesFile = readFile "${env.WORKSPACE}/src/main/resources/application.properties"
  propertiesFile.withInputStream {
    properties.load(it)
}
  println properties."service.version"*/
  
 /* File propertiesFile = new File('${env.WORKSPACE}/src/main/resources/application.properties')
def config = new ConfigSlurper().parse(propertiesFile.toURL())
println(config.service.version)*/
/*	def properties = new Properties()
    //both leading / and no / is fine
    this.getClass().getResource( '${env.WORKSPACE}/src/main/resources/application.properties' ).withInputStream {
        properties.load(it)
    }
  properties."service.name"*/
sh "ls -la ${env.WORKSPACE}/src/main/resources/"
  def props = readProperties file: '${env.WORKSPACE}/src/main/resources/'
 def Var1= props['service.version']
  echo "Var1=${Var1}"

        env.appName = repoName
        env.branch = BRANCH_NAME 
        env.apiVersion = "v1"

      	//def content = readFile 'hello-world/src/main/resources/application.properties'
        /*
        Properties properties = new Properties()
        InputStream is = new ByteArrayInputStream(content.getBytes());
        properties.load(is)

        def runtimeString = 'SERVICE_VERSION'
        echo properties."$service.version"
        SERVICE_VERSION = properties."$service.version"
        echo SERVICE_VERSION*/

        slack.info message: 'STARTED', appendBuildInfo: true
}