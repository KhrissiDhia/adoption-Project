Exécution analyse SonarQube
12 s
Warning: A secret was passed to "sh" using Groovy String interpolation, which is insecure.
		 Affected argument(s) used the following variable(s): [SONAR_TOKEN_SECURE]
		 See https://jenkins.io/redirect/groovy-string-interpolation for details.
+ mvn -B sonar:sonar -Dsonar.projectKey=adoption-project -Dsonar.host.url=http://localhost:9000 -Dsonar.login=**** -Dsonar.java.source=17 -Dsonar.sourceEncoding=UTF-8
[INFO] Scanning for projects...
[INFO]
[INFO] --------------------< com.example:adoption-Project >--------------------
[INFO] Building adoption-Project 0.0.1-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- sonar-maven-plugin:3.9.1.2184:sonar (default-cli) @ adoption-Project ---
[INFO] User cache: /var/lib/jenkins/.sonar/cache
[INFO] SonarQube version: 8.9.7
[INFO] Default locale: "en", source code encoding: "UTF-8"
[INFO] Load global settings
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  6.766 s
[INFO] Finished at: 2025-06-27T15:50:48+02:00
[INFO] ------------------------------------------------------------------------
[ERROR] Failed to execute goal org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184:sonar (default-cli) on project adoption-Project: Not authorized. Please check the properties sonar.login and sonar.password. -> [Help 1]
[ERROR]
[ERROR] To see the full stack trace of the errors, re-run Maven with the -e switch.
[ERROR] Re-run Maven using the -X switch to enable full debug logging.
