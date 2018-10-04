#!/usr/bin/groovy
def call(body) {
    // evaluate the body block, and collect configuration into the object
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

	echo body
	echo config
	echo config.payload
	
	def payloadObject = new groovy.json.JsonSlurper().parseText(config.payload)
	
	env.repoURL = payloadObject.repository.clone_url                            
	env.repoName = repoURL.tokenize('/').last().replace(".git", "")
	env.appName = repoName
	
	def ref = payloadObject.ref
	env.branch = ref.replace("refs/heads/", "")
	
	echo payloadString
	echo repoName
	echo branch
}