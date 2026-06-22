pipeline {
    agent any

    environment {
        GEMINI_API_KEY     = credentials('gemini-api-key')
        EMAIL_APP_PASSWORD = credentials('zoho-email-token')
        MINOP_PASSWORD     = credentials('minop-password')
        VISIONAI_PASSWORD  = credentials('visionai-password')
        MYSQL_CONN_QC      = credentials('mysql-conn-qc')
        MYSQL_CONN_STAGE   = credentials('mysql-conn-stage')
    }

    stages {

        stage('Checkout') {
            steps {
                git 'https://github.com/RavirajMahamuni1810/VisionAi_Mikshi.git'
            }
        }

        stage('Build & Test') {
            steps {
                bat 'mvn clean test -DsuiteXmlFile=VisionAI.xml'
            }
        }
    }
}
