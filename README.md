### Demo Spring Boot App

## How to run
Run the Application:

- In IntelliJ IDEA, right-click on MyFrameworkApplication.java and select Run 'SpringBootPlaygroundApplication'
- Using local terminal `mvn spring-boot:run`

## Test
- Do `curl -X GET http://localhost:8080/api/persons` and it should return the results
```[{"id":1,"name":"Alice","age":30},{"id":2,"name":"Bob","age":25}]```
