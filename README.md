# SergioMejiaSolution
Sergio Mejia Code assessment solution


This is my solution. I explain my decission on each point

### 1) REST API Implementation 

- Valid payload should be printed in the console log from the method 


Using AOP the request is printed using @Before


- Return corresponding http response code including vehicle id (something like UUID.randomUUID() would be sufficient) to the client as a part of a location header. 


I decided to use this library because has a better performance https://github.com/cowtowncoder/java-uuid-generator.
Maybe for this test is not necessary but is so simple to integrate in the project and use it.


----------------------------------------------------------------------------------------------------------------------


### 2) Error Handling Implement validation for "transmissionType" from the task 2 "Create Vehicle" , MUST accept only "MANUAL" or "AUTO" in the request body. 
Invalid payload use case: 


- Invalid payload MUST be thrown with a corresponding error code and error message in the response body; 


I use error code Bad Request = 404 if any property in the request is not valid. Also I use @Valid annotation to verify the properties in the request
I used @NotNull for the string, Year is int so I decided to use @Min=2000. I applied simple validation because I do not have the rules about this properties.

For transmissionType is specified that must contains Manual or Auto, so I decided to use

@NotEmpty(message="Transmission type property must contain information like Manual or Auto")
@Size(min=4, message="Transmission type must not be less than 4 characters")

See com.sgma.controller.request.VehicleRequest for more information


- Any other error MUST throw 500 "Internal Server Error" http response code with an informational message in the response body. 


For this case I decide to use Http status 500 Internal Server Error (as is specified) for any unexpected exception at runtime

To catch this exceptions I decided to use @ControllerAdvice and @ExceptionHandler for a crosscutting exception handler

see com.sgma.controller.advice.ErrorHandlerControllerAdvice for more information


----------------------------------------------------------------------------------------------------------------------


### 3) Logging 

Logging Cross-cutting concern We want to keep logging before and after a method, however we don't want to put a log statement in every method. As an example, below are two classes - "DBServiceA" and "HttpServiceB" - and the corresponding methods getData(int id)and sendMessage(String message). 
 
- Implement a Java logging/cross-cutting concern solution for these two classes.  Feel free to use any framework and libraries (Spring preferably). You can create/modify existing. 


I decided to use AOP with 3 advices:

 @Before 
  Prints method name and parameters, 
 @AfterReturning 
  Prints method name and the response
 @AfterThrowing
  Prints method name and stacktrace information if any exception occurs
  
See com.sgma.aop.VehicleCrossCuttingLog for more information


About this two classes DBServiceA and HttpServiceB, I added in the project and VehicleService (is a service) is using the methods of both classes to show AOP functionality explained previously and also to use an asynchronous interaction creating a new Vehicle.

See com.sgma.services.VehicleService for more information
 
 
About "Implement an asyncrhonous Rest API" I was confused becuase I did not have a clear idea about this requirement.


I was thinking two cases:

1. When the REST web service receives a request, verifes the request data, send back a response with status 202 Accepted and execure a Runnable task with an ExecutorService to process the request,
   but with this situation we can use any messaging service like Apache Kafka, RabbitMQ, IBM MQTT. So the REST web service receives the request, validates request data, sends the request to the messaging service and send back a response.
   I think that this solution is better to prevent data lost in the case that the Web service is about to shutdown for any bad performance or hardware issue.
2. The Rest Web Service receives the request, verifies the request data, starts multiple asynchronous tasks with Future objects, wait for all tasks to complete and then send back a response

I decided to implement the second option.


I created a repository class com.sgma.jpa.VehiclesRepository and entity com.sgma.jpa.domains.VehicleEntity to show an Integration tier using JPA repository with H2 database.
I create a simple table to store vehicle data, you can find it on sql.data file and in the code below.

```
DROP TABLE IF EXISTS vehicles;
 
CREATE TABLE vehicles (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  uuid VARCHAR(50) NOT NULL,
  vin VARCHAR(10) NOT NULL,
  year INT(5) DEFAULT 2000,
  make VARCHAR(10) NOT NULL,
  model VARCHAR(10) NOT NULL,
  transmissionType VARCHAR(10) NOT NULL
);
```

TESTS

I created two Test classes:

com.sgma.RestVehicleApplicationTests to test the Controller
com.sgma.ServiceVehicleApplicationTests to test the Service
com.sgma.VehicleRepositoryApplicationTest to test the Repository

Thinks to add in the project in real scenarios:

- The use fo spring cloud, Eureka server, zuul when we are going to create many instance of the same REST web service with different ports, this tools are usefull for service discovery, load balancer.
- Spring acctuator to have metrics and hardware usage
- The use of Actuator and Prometheus to monitoring the web service or the usage of any other monitoring tool
- Spring security with OAUTH2 + JWT using AuthorizationServerConfigurerAdapter, ResourceServerconfigurerAdapter, WebSecurityConfigurerAdapter, jks file
- Spring JPA repository to handler database interaction, pagging and sorting or we can use Hibernate
- Use of HandlerInterceptor interface to intercept the request before the controller or after the controller if necessary
- When I was working on IBM one customer requirement was to send an email everytime an exception use to occurs at runtime, this can be a good idea


I did not create Entities (@Entity), in this case @Entity Vehicle, because is not mentioned in the code assignment.
I tried to create a solution simple to understand for the reviewers, I do not want to confuse any reviewers.

How to USE the service

I decided to use Spring Boot, java 8, log4j, maven. I deciede not to use Lombok because I do not know If you have installed Lombok.
So If you have Eclipse/IntelliJ just inport the project, maven clean install and execute it.

Rest - Create new vehicle
POST
http://127.0.0.1:8085/vehicles/registration

```
>Sucessful response:
{
	"vin": "1A4AABBC5KD501999",   
	"year": 1990,   
	"make": "S",   
	"model": "ASD",
	"transmissionType": "Manual"
}

{
	"vin": "1A4AABBC5KD501999",   
	"year": 1990,   
	"make": "S",   
	"model": "ASD",
	"transmissionType": "Auto"
}
```

> Exception:

```
{
	"vin": "1A4AABBC5KD501999",   
	"year": 1990,   
	"make": "FFF",   
	"model": "ASD",
	"transmissionType": "Manual"
}
```
Just use "make": "FFF" to get an exception and verify AOP AfterThrowing and ControllerAdvice

Find a vehicle by uuid
GET
http://localhost:8085/vehicles/registration/id/be4de345-04ae-4978-8de3-4504ae597865

Swagger
http://127.0.0.1:8085/vehicles/swagger-ui.html#/vehicle-rest-controller

application.properties file just contains

server.servlet.context-path=/vehicles
server.port=8085

