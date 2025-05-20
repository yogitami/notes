### Spring Boot Interview Questions :

 1. What is an embedded container in spring boot and why is it important?
    - Tomcat is a lightweight server that can run inside a Springboot application. It is lightweight because it had to be integrated with the framework and make our application to run as a standalone application. It has default configurations. It eliminates the need for developers to deploy their applications on external containers like Tomcat or Jetty, making it easier to develop & deploy applications.
    - It also supports hot reloading through some dev tools.

2. What is hot reloading.
   - Hot reloading allows developers to make changes to their code, resources or configuration files while an application is running, and see those changes immediately reflected without needing to restart the application.
   - How does it work.
     - Hot reloading is facilitated through the use of development tools and libraries that support dynamic reloading of resources, configuration and code changes. Springboot itself doesn't provide native hot reloading capabilites, it integrates seamlessly with third party tools & frameworks that enable this functionality. It works based on following steps:
       1. Monitoring for changes : continously monitors. The monitoring can be done using file system watchers, event listeners,etc.
       2. Detecting Change : identifies the nature of the modification(adding,modifying,deleting) & determines the scope of the change.
       3. Applying changes dynamically : involves reloading the modified class, recompiling changed resources, or reinitializing updated configurations.
       4. Preparing Application State : to ensure a seamless user experience, hot reloading mechanism strive to preserve the application's state during the reloading process. This includes maintaining existing session data, keeping connections alive and minimizing disruptions to ongoing transactaions or background proccess
       5. Refreshing Application Context
       6. Feedback and verification
       7. Incremental compilation and deployment
3. How can you externalise the configuration properties.
     - By using applicaition.property file & then using @Value("${property key name}") in the code to use it.
     - By using environment variables.
     - VM options

4. What are some anti patterns in microservices and how do you avoid them.
    1. The Distributed Monolith :
        - It is a situation where microservices are supposed to be independent but they are tightly coupled to one another, creating the same depedency issues found in a monolithic architecture. Services may rely on synchronous calls, shared databases, etc.
        - How to avoid ?
          - Design services to be loosely coupled : Ensure that each microservice operates indepedently, with well defined APIs and minimal reliance on other services.
          - Use asynchronous communication.
          - Single responsibility principle for microservices
          - Avoid shared databases.
    2. The God Service :
       - A single service has grown too large and handles too many responsibilities, effectively becoming a bottleneck.
       - How to avoid ?
          - Enforce single responsibility
          - Refactor as needed
          - Split services based on business capabilities(following Domain Driver Design Principle).
    4. The shared database anti pattern :
       - Multiple microservices access the same database.
       - How to avoid ?
          - Database per service.
          - Use API-based communication : have services communicate via APIs
          - Data Duplication : In case where data is needed by multiple services, replicate data to avoid direct access and use event-driven architectures to keep data in sync (CQRS & event sourcing)
    6. Tight Coupling via Synchronous Communication :
       - If one service goes down, it can cause a chain reaction that brings down other services
       - How to avoid ?
           - Async communication
           - Use circuit breaker
           - design for retries and timeouts.
    8. Lack of Service Autonomy :
       - Microservices should be autonomous .i.e. they should be independently deployable and able to function without relying on other services.
       -  How to avoid ?
            - Independent deployment.
            - Avoid dependencies on other services for data or logic : Services should be self-contained with their own logic and data store, with well-defined APIs for external communication.
            - Embrace eventual consistency : wherever possible use eventual consistency instead of real time coordination between services.
    10. Not Handling Distributed Tracing and Monitoring :
        - Without proper tracing and monitoring, it's hard to identify performance bottlenecks and other issues across a distributed system.
        - How to avoid ?
          - Implement distributed tracing using tools like Jaeger,Zipkin,etc to trace requests as they propogate through services.
          - Centralised logging : ELK stack, Prometheus with Grafana or Datadog to aggregate logs from all the services in a central location.
          - Health checks : Implement health checks at the service level and monitor them via tools like Kubernetes or Spring boot Actuator to ensure the services are operating as expected.
    12. Poor API design (Overloaded APIs) :
        - Creating an API that does too much, making it hard for clients to use, understand and maintain. This can also lead to breaking changes when modifying the API.
        - How to avoid ?
          - API Versioning
          - Design APIs around business capabilities.
          - GraphQL for complex use case
    14. Ignoring Security in Microservices :
        - Neglecting security concerns like unencrypted communication between services, weak authentication or failure to properly handle sensitive data
        - How to avoid ?
          - Use strong authentication and authorization : Implement centralized authentication and authorization via OAuth2,JWT, etc.
          - Secure communication : Use TLS/SSL to encrypt communication between services, even for internal service to service calls.
          - Access Control : Implement role based access control and ensure that services only have access to the data and resources they need.
    16. Over engineering the system :
        - How to avoid ?
          - Use strangler pattern to migrate from monolith to microservice over time
         
5. How to handle the data consistency across services in a distributed system.
   1. Clarify the consistency requirements.

              
    
   
