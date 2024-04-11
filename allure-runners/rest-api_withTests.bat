cd ..
CALL mvn clean test -pl rest-api
npx allure-commandline serve rest-api/target/surefire-reports
pause