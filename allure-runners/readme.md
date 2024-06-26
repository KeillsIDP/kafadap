# Allure
В проекте используется Allure для создания отчетов о тестах.
При помощи npm была установлена Allure-Commandline, что позволяет создавать отчеты 
без использования плагина.
- Для каждого модуля есть свой .bat файл:
    - _testsGenerated: создает отчет если тесты уже сгенерированы
    - _withTests: при помощи mvn запускаем тестирование и после получаем отчет

- *_testsGenerated:
    - ```cd..``` - переходим на директорию выше 
    - ```npx allure-commandline serve kafka-consumer/target/surefire-reports``` - при помощи npx обращаемся 
        к установленной allure-commandline, при помощи команды serve из указанной директории 
        берется информация о тестировании и создается отчет
    - ```pause``` - паузим консоль

*_withTests - точно также как и *_testGenerated, но мы используем Maven 
```CALL mvn clean test -pl kafka-consumer``` - сначала очищаем папку target, после запускаем тестирование

В папку вложен пример построенного отчета созданный при помощи ```generate --single-file```