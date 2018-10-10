#!/usr/bin/groovy

def call(Map config) {
	def soapuiProject = "src/test/soapui/" + appName + "-soapui-project.xml"
	def soapuiProperties = "src/test/soapui/" + config.env + ".properties"
	def command = "mvn com.smartbear.soapui:soapui-maven-plugin:5.4.0:" + config.goal + " -Dsoapui.project=" + soapuiProject + " -Dsoapui.properties=" + soapuiProperties
	
	sh command
}