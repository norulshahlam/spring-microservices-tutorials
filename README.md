# [Learn to build RESTful Microservices with Spring Boot and Spring Cloud](https://www.udemy.com/course/spring-boot-microservices-and-spring-cloud/)

![Image](./service-registry/src/main/resources/microservice-architecture.png)

## Stacks used

- Java 11
- Spring boot
- Docker
- MySql docker container
- Postman

## How to run?

- Start your docker DB

```docker run --detach --env MYSQL_ROOT_PASSWORD=root --env MYSQL_DATABASE=mydb --env MYSQL_PASSWORD=root --env MYSQL_USER=admin --name localhost --publish 3306:3306 mysql:8.0```

- Start service-discovery
- Start config-service
- start other services: dept, user, hystrix, apigateway

<details>
<summary>Debugging your DB server</summary><br>

Run mysql in cli using docker

```
docker exec -it localhost bash
```

Connect to mysql

```
mysql -u admin -proot;
```

Test

```
use mydb;
show tables;
desc users;
select * from users;
```

Stop & remove all running processes

```
docker rm $(docker ps -a -q) -f
```

</details>

## Use case

user-service will call dept-service

## service registry

Eureka Server is an application that holds the information about all client-service applications. Every Micro service
will register into the Eureka server and Eureka server knows all the client applications running on each port and IP
address. Eureka Server is also known as Discovery Server.

one of the benefits is you can call the service name INTERNALLY instead of the hostname:port number.

## API gateway

A programming that sits in front of an API ( Application Programming Interface) and is the single-entry point for
defined back-end APIs and microservices. Primarily responsible for request routing, the gateway intercepts all requests
from clients. It then routes the requests to the appropriate microservice.

`Routing Handler`

Being focused on routing requests, it forwards requests to a Gateway Handler Mapping, which determines what should be
done with requests matching a specific route. With configs in place the url will be slightly changed:

from calling the ms directly,

    http://localhost:9001/departments/

to calling the api-gateway

    http://localhost:9191/departments/

Without this config you can still use the gateway like this:

    http://localhost:9191/department-service/departments/

`Circuit breaker`

API gateway can serve as circuit breaker which identifies which services are not running and implements a fallback
method available which informs the user as the response.

stop any service and run any request to see the fallback response.

## Multiple instances

how to run multiple instance?

1. Duplicate run configurations and run it
2. use command line:

   mvn spring-boot:run -Dspring-boot.run.arguments="--spring.application.instance_id=shah45 --server.port=1234"

We can now start multiple instances but the prob is service registry will register only one as the port assigned is
fixed in property file. we can ask Spring to assign static port number by assigning server.port=0

If you run multiple instances using Spring, service registry will still show one instance id running as the port number
we assigned was fixed i.e 0. this won't be the case if we start instances using mvn command as we manually assign a port
and instance value. to solve this, we create a property name eureka.instance.instance-id with our own randomly generated
value. Now try to run another instance again using Spring and you can see multiple instance registering in Eureka.

![Image](./service-registry/src/main/resources/eureka-dashboard.PNG)

`Warning`

Make sure to have dedicated port for your DB with each service calling a unique DB port else each instance will call their own DB based on the assigned dynamic port.

## Load Balancing

Load balancing is the process of distributing traffic among different instances of the same application. API gateway has
a built-in load balancer, so we can use it right away.

To test for this, simply run multiple instances of user / dept service and check the status through the endpoint
status/check. this endpoint shows the port number and everytime we run this, it will show diff port number indicating
that requests are being distributed across different instances.

![Image](./service-registry/src/main/resources/load-balancer1.PNG)
![Image](./service-registry/src/main/resources/load-balancer2.PNG)

## Hystrix dashboard

A nice optional feature of Hystrix is the ability to monitor its status on a dashboard.

A new service will be created with `spring-cloud-starter-hystrix-dashboard` and `spring-boot-starter-actuator` in the
pom.xml. Once done, we will run: `http://localhost:9295/hystrix` to view the dashboard:
![Image](./service-registry/src/main/resources/hystrix-dashboard.PNG)

To allow the dashboard to monitor, we need to give hystrix streams (in the form of url) in the field of the dashboard.
the url is `localhost:9191/actuator/hystrix.stream` which is from the api-gateway service (which we have added
additional config). run this url to ensure there is a stream coming.
![Image](./service-registry/src/main/resources/hystrix-stream.PNG)

Once done, paste this url in hystrix dashboard:
![Image](./service-registry/src/main/resources/hystrix-dashboard2.PNG )

Re-run all the requests from user-service and dept-service and view the dashboard. you will get something like this:  
![Image](./service-registry/src/main/resources/hystrix-dashboard3.PNG )

## [Externalized configuration](https://springframework.guru/spring-external-configuration-data/)

Spring Boot likes you to externalize your configuration, so you can work with the same application code in different environments. You can use properties files, YAML files, environment variables and command-line arguments to externalize configuration. Property values can be injected directly into your beans using the @Value annotation, accessed via Spring’s Environment abstraction or bound to structured objects. 

Externalised properties is usually being fetched through bootstrap property file as this will run before Spring context loads application property file. 
There are few ways for externalization:

![Image](./service-registry/src/main/resources/externalized-configuration-diagram.drawio.png)

Firstly have a bootstrap file in services that is externalized. in that file,  point to the uri of the config-server:

      spring.cloud.config.enabled=true
      spring.cloud.config.uri=http://localhost:9296

`Configuration for all services`
1. By using application.yml in git repo

`Configuration for individual service`
1. By using {service-name}.yml in git repo
2. Having `spring.cloud.config.name={service-name}` in application.properties of {service-name} in source code

Make sure to run config-service first before running your other services as other services will fetch the externalized properties from your config-service on start. 

`17 levels of loading configuration properties`  [More information](https://piotrminkowski.com/2019/03/11/a-magic-around-spring-boot-externalized-configuration/)

It is possible to have multiple property sources in a Spring Boot application. Therefore, it is important to be aware of the property source that will take precedence over others. You can view the precedence [here](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config). 

#### [Profiles for multiple environments](https://www.baeldung.com/spring-profiles)

Spring Profiles provide a way to segregate parts of your application configuration and make it only available in certain environments. It automatically loads the properties in a application file for all profiles and the ones in profile-specific property files only for the specified profile. The properties in the profile-specific configuration override the ones in the master configuration.
Here is a sample diagram:

![Image](./service-registry/src/main/resources/externalized-configuration-diagram-2.drawio.png)

Endpoint to test:

      http://localhost:9191/users/status-check
      http://localhost:9191/departments/status-check

`Sample`
We have unique app-description values in:

1. department.yml in git repo  
2. application.yml in git repo  
3. application.yml in source code in department-service  
4. application.yml in source code in user-service  

according to level of priority, the status check:
- for dept-service will show from department.yml in git repo  
- for user-service will show from application.yml in git repo  

![Image](./service-registry/src/main/resources/status-check-dept-service.PNG)
![Image](./service-registry/src/main/resources/status-check-user-service.PNG)



