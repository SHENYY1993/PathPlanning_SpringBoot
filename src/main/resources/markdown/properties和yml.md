##说明
application.properties和application.yml都是Spring Boot应用程序中的配置文件，用于配置应用程序的各种属性。

application.properties是一种基于键值对的配置文件，常用的配置项以key=value的形式表示。例如：
```
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/mydb
spring.datasource.username=root
spring.datasource.password=123456
```

application.yml是一种基于缩进的配置文件，常用的配置项以key: value的形式表示。例如：
```
server:
port: 8080
spring:
datasource:
url: jdbc:mysql://localhost:3306/mydb
username: root
password: 123456
```

两种配置文件的作用相同，只是表现形式不同，可以根据自己的喜好和项目的需要选择使用哪种形式。

读者已经了解到无论是Properties配置还是YAML配置，最终都会被加载到Spring Environment中。