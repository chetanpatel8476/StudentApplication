pipeline {
    agent any

    stages {
        stage('Clone the Project'){
            steps{
                slackSend channel: '#jenkins', 
                    color: 'good', 
                    message: "New code is available on github. Now started to clone the latest code.", 
                    tokenCredentialId: 'Slack_Token'
                checkout scm
                echo "clone the project successfully."
            }
        }

        stage('Approval for Build the Artifacts'){
            steps{
                timeout(time:3, unit:'DAYS'){
                    input 'Do We have your approval for build the war?'
                } 
            }
        }

        stage('Build the Project'){
            steps{
                sh 'mvn -B -Dmaven.test.skip=true package'
            }
            
            post{
                success{
                    slackSend channel: '#jenkins', 
                            color: 'good', 
                            message: "Build is *${currentBuild.currentResult}:* *Job*: ${env.JOB_NAME} *Build Number*: ${env.BUILD_NUMBER}\n More info at: ${env.BUILD_URL}", 
                            tokenCredentialId: 'Slack_Token'
                }
                
                failure{
                    slackSend channel: '#jenkins', 
                            color: 'danger', 
                            message: "Build is *${currentBuild.currentResult}:* *Job*: ${env.JOB_NAME} *Build Number*: ${env.BUILD_NUMBER}\n More info at: ${env.BUILD_URL}", 
                            tokenCredentialId: 'Slack_Token'
                }
            }
        }

        stage('Push artifacts to Nexus repository'){
            steps{
                script{
                    def pom = readMavenPom file: 'pom.xml'
                    
                    nexusPublisher nexusInstanceId: 'localnexus3', 
                        nexusRepositoryId: 'student_application', 
                        packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: "/var/lib/jenkins/workspace/${JOB_NAME}/target/${pom.artifactId}.war"]], 
                        mavenCoordinate: [artifactId: "${pom.artifactId}", 
                        groupId: "${pom.groupId}", 
                        packaging: 'war', 
                        version: "${pom.version}-${BUILD_NUMBER}"]]]
                }
            }
            
            post{
                success{
                    slackSend channel: '#jenkins', 
                            color: 'good', 
                            message: "Artifacts uploaded successfully for *Build Number* : *${env.BUILD_NUMBER}*", 
                            tokenCredentialId: 'Slack_Token'
                }
                
                failure{
                    slackSend channel: '#jenkins', 
                            color: 'danger', 
                            message: "Artifacts failed to uploading for *Build Number* : *${env.BUILD_NUMBER}*", 
                            tokenCredentialId: 'Slack_Token'
                }
            }
        }
        
        stage('Approval for Deployment'){
            steps{
                timeout(time:3, unit:'DAYS'){
                    input 'Do we have your approval for deployment?'
                }  
            }
        }
        
        stage('Started the Deployment'){
            steps{
                slackSend channel: '#jenkins', 
                    color: 'good', 
                    message: "Deployment is *started* on for *Build Number* : *${BUILD_NUMBER}*", 
                    tokenCredentialId: 'Slack_Token' 
            }
        }
        
        stage('Download artifacts from the repository and Deploy into web servers'){
            steps{
                script{
                    def pom = readMavenPom file: 'pom.xml'
                    ansiblePlaybook extras: "--extra-vars nexus_build=${pom.version}-${BUILD_NUMBER}", 
                        inventory: 'hosts', 
                        playbook: 'student-application.yml'
                }
            }
            
            post {
                failure {
                    slackSend channel: '#jenkins', 
                        color: 'danger', 
                        message: "Deployment is *failed* for *Build Number*: *${env.BUILD_NUMBER}*", 
                        tokenCredentialId: 'Slack_Token'
               }
           }
        }
        
        stage('Completed the Deployment'){
            steps{
                slackSend channel: '#jenkins', 
                    color: 'good', 
                    message: "Deployment is *completed* for *Build Number*: *${env.BUILD_NUMBER}*", 
                    tokenCredentialId: 'Slack_Token'
            }
        }
    }
}
