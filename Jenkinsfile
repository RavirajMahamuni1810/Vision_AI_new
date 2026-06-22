pipeline {
    agent any

    // Tool names must match Jenkins > Manage Jenkins > Tools exactly.
    tools {
        jdk 'JDK17'
        maven 'Maven3'
    }

    options {
        timestamps()
        timeout(time: 60, unit: 'MINUTES')
    }

    parameters {
        choice(name: 'SUITE_XML',
               choices: ['VisionAI.xml', 'PWTestNg.xml', 'MinopCMS.xml', 'MinopNotificationRule.xml', 'minoplogin.xml', 'PWTestNGLeave.xml', 'Old_UploadVideoTest.xml'],
               description: 'Select the TestNG suite XML to execute (default: VisionAI.xml)')
    }

    environment {
        // Jenkins credentials -> environment variables consumed at runtime by
        // PWBaseTest.resolveEnvPlaceholder() (which replaces ${ENV_VAR} placeholders in the settings Excel).
        // NOTE: No AI / Gemini key is required.
        EMAIL_APP_PASSWORD = credentials('email-app-password')
        MINOP_PASSWORD     = credentials('minop-password')
        VISIONAI_PASSWORD  = credentials('visionai-password')
        MYSQL_CONN_QC      = credentials('mysql-conn-qc')
        MYSQL_CONN_STAGE   = credentials('mysql-conn-stage')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Install Playwright Browsers') {
            steps {
                echo 'Installing Playwright browsers...'
                // Downloads the browsers Playwright needs into the agent's Playwright cache.
                bat 'mvn -B exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"'
            }
        }

        stage('Execute Tests') {
            steps {
                echo "Executing Test Suite: ${params.SUITE_XML}"
                bat "mvn -B clean test -DsuiteXmlFile=${params.SUITE_XML}"
            }
        }
    }

    post {
        always {
            // Archive Surefire/TestNG reports and any recorded videos
            archiveArtifacts artifacts: 'target/surefire-reports/**/*, target/videos/**/*', allowEmptyArchive: true

            // Generate and publish the Allure report
            allure includeProperties: false, jdk: '', results: [[path: 'target/allure-results']]
        }
        success {
            echo 'Tests executed successfully!'
        }
        failure {
            echo 'Some tests failed. Check the Allure Report for details.'
        }
    }
}
