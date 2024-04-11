cd ..
CALL mvn clean test -pl kafka-producer
npx allure-commandline serve kafka-producer/target/surefire-reports
pause