pipeline
{
    agent any

    environment
    {
        deploymentBranch = "master"
        mavenExecutable = "mvn-jdk8"
    }

    triggers
    {
        cron('H H(0-5) * * *')
        pollSCM('@hourly')
    }

    options
    {
        buildDiscarder(logRotator(numToKeepStr:'10'))
        disableConcurrentBuilds()
        skipStagesAfterUnstable()
        timeout(time: 1, unit: 'HOURS')
    }

    stages
    {
        stage('Compile')
        {
            steps
            {
                sh '${mavenExecutable} clean compile test-compile'
            }
        }

        stage('Unit-Tests')
        {
            steps
            {
                sh '${mavenExecutable} surefire:test'
            }
        }

        stage('Integration-Tests')
        {
            when { branch "${env.deploymentBranch}" }

            steps
            {
                sh '${mavenExecutable} failsafe:integration-test failsafe:verify'
            }
        }

        stage('Release')
        {
            when { branch "${env.deploymentBranch}" }

            steps
            {
                 sh '${mavenExecutable} clean deploy -Dmaven.test.skip=true'
            }
        }

        stage('SonarQube')
        {
            when { branch "${env.deploymentBranch}" }

            steps
            {
                sh '${mavenExecutable} clean verify sonar:sonar -Dsonar.host.url=http://sonarqube.avidesmedia/'
            }
        }

        stage('POM-Analysis')
        {
            when { branch "${env.deploymentBranch}" }

            steps
            {
                 sh '${mavenExecutable} enforcer:enforce'
            }
        }
    }
}
