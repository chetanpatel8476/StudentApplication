pipeline {
    agent any

    stages {
        stage('Clone the Project'){
            steps{
                checkout scm
                echo "clone the project successfully."
            }
        }
        stage('Build the Project'){
            steps{
                sh 'mvn clean package'
                echo "artifacts created successfully."
            }
        }
        stage('Push artifacts to Nexus repository'){
            steps{
                nexusPublisher nexusInstanceId: 'localnexus3', nexusRepositoryId: 'student_application', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: '/var/lib/jenkins/workspace/Nexus-Example/target/student-service.war']], mavenCoordinate: [artifactId: 'student_application', groupId: 'com.einfochips.student', packaging: 'war', version: '1.0']]]
            }
        }
    }
}