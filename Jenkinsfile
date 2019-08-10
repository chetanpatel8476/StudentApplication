node{
    stage('Clone the Project'){
        checkout scm
        echo "clone the project successfully."
    }
    stage('Build the Project'){
        sh 'mvn -B -Dmaven.test.skip=true clean package'
        echo "artifacts created successfully."
    }
    stage('Execute Unit Tests'){
        sh 'mvn -B clean test'
        echo "Performed unit tests successfully."
    }
    stage('Execute Integration Tests'){
        sh 'mvn -B clean verify -Dsurefire.skip=true'
        echo "Performed integration tests successfully."
    }
    stage('Generate Code Coverage Report'){
        sh 'mvn test jacoco:report'
        echo "Performed integration tests successfully."
    }
    stage('Perform Quality Analysis'){
        sh 'mvn sonar:sonar -Dsonar.host.url=http://localhost:9000'
        echo "Performed code quality analysis and push reports to sonar server."
    }
    stage('Approval'){
        timeout(time:3, unit:'DAYS'){
                    input 'Do I have your approval for deployment?'
        } 
    }
    stage('Push artifacts to Nexus repository'){
        def pom = readMavenPom file: 'pom.xml'
        nexusPublisher nexusInstanceId: 'localnexus3', nexusRepositoryId: 'student_application', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: '/var/lib/jenkins/workspace/Nexus-Example/target/student-service.war']], mavenCoordinate: [artifactId: 'student_application', groupId: 'com.einfochips.student', packaging: 'war', version: "${pom.version}"]]]
    }
}
