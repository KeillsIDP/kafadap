cd ..
CALL mvn clean test -pl kafka-consumer
npx allure-commandline serve kafka-consumer/target/surefire-reports
pause