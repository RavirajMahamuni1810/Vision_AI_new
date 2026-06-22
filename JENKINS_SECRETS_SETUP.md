# Jenkins / CI Secrets Setup

The test-data workbook (`data/MinopTandATestData.xlsm`) no longer stores any real
credentials. All secrets were replaced with `${ENV_VAR}` placeholders that are
resolved **at runtime** from environment variables (see
`PWBaseTest.resolveEnvPlaceholder(...)`).

You must provide the following environment variables in your Jenkins job
(use the **Credentials Binding** plugin → "Secret text", or `withCredentials { ... }`).

| Environment variable | Used for | Excel location (was) |
|----------------------|----------|----------------------|
| `EMAIL_APP_PASSWORD` | Zoho app token used to send email reports      | `settings!B11/C11` |
| `MINOP_PASSWORD`     | Minop login password (login / cms / notification tests) | `login/cms/Notificationrule` col E |
| `VISIONAI_PASSWORD`  | VisionAI login password                        | `loginVisionAI` col D |
| `MYSQL_CONN_QC`      | Full JDBC URL incl. user/password for the QC DB   | `settings!B18` |
| `MYSQL_CONN_STAGE`   | Full JDBC URL incl. user/password for the Stage DB | `settings!C18` |

> **AI failure analysis is optional and disabled by default.** `GEMINI_API_KEY` is
> **not** required for the pipeline. If you ever want AI root-cause analysis on
> failures, set `GEMINI_API_KEY`; otherwise the AI step is skipped automatically
> (see `PWBaseTest.isAIConfigured()`).

### Example `MYSQL_CONN_QC` value
```
jdbc:mysql://<host>:3306/<db>?user=<user>&password=<password>&autoReconnect=true&useSSL=false&maxReconnects=3
```

### Jenkins pipeline example
```groovy
pipeline {
  agent any
  environment {
    EMAIL_APP_PASSWORD = credentials('email-app-password')
    MINOP_PASSWORD     = credentials('minop-password')
    VISIONAI_PASSWORD  = credentials('visionai-password')
    MYSQL_CONN_QC      = credentials('mysql-conn-qc')
    MYSQL_CONN_STAGE   = credentials('mysql-conn-stage')
  }
  stages {
    stage('Test') {
      steps {
        bat 'mvn -B clean test -DsuiteXmlFile=VisionAI.xml'
      }
    }
  }
}
```

> If an env var is not set, the test simply receives the literal `${VAR}` string,
> which makes the missing-secret failure obvious in the logs.

### Running locally
Set the same variables in your shell before running Maven, e.g. (PowerShell):
```powershell
$env:MINOP_PASSWORD="..."; $env:VISIONAI_PASSWORD="..."; $env:EMAIL_APP_PASSWORD="..."
mvn test -DsuiteXmlFile=VisionAI.xml
```
