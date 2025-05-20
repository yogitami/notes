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
    3. The shared database anti pattern :
       - Multiple microservices access the same database.
       - How to avoid ?
          - Database per service.
          - Use API-based communication : have services communicate via APIs
          - Data Duplication : In case where data is needed by multiple services, replicate data to avoid direct access and use event-driven architectures to keep data in sync (CQRS & event sourcing)
    4. Tight Coupling via Synchronous Communication :
       - If one service goes down, it can cause a chain reaction that brings down other services
       - How to avoid ?
           - Async communication
           - Use circuit breaker
           - design for retries and timeouts.
    5. Lack of Service Autonomy :
       - Microservices should be autonomous .i.e. they should be independently deployable and able to function without relying on other services.
       -  How to avoid ?
            - Independent deployment.
            - Avoid dependencies on other services for data or logic : Services should be self-contained with their own logic and data store, with well-defined APIs for external communication.
            - Embrace eventual consistency : wherever possible use eventual consistency instead of real time coordination between services.
    6. Not Handling Distributed Tracing and Monitoring :
        - Without proper tracing and monitoring, it's hard to identify performance bottlenecks and other issues across a distributed system.
        - How to avoid ?
          - Implement distributed tracing using tools like Jaeger,Zipkin,etc to trace requests as they propogate through services.
          - Centralised logging : ELK stack, Prometheus with Grafana or Datadog to aggregate logs from all the services in a central location.
          - Health checks : Implement health checks at the service level and monitor them via tools like Kubernetes or Spring boot Actuator to ensure the services are operating as expected.
    7. Poor API design (Overloaded APIs) :
        - Creating an API that does too much, making it hard for clients to use, understand and maintain. This can also lead to breaking changes when modifying the API.
        - How to avoid ?
          - API Versioning
          - Design APIs around business capabilities.
          - GraphQL for complex use case
    8. Ignoring Security in Microservices :
        - Neglecting security concerns like unencrypted communication between services, weak authentication or failure to properly handle sensitive data
        - How to avoid ?
          - Use strong authentication and authorization : Implement centralized authentication and authorization via OAuth2,JWT, etc.
          - Secure communication : Use TLS/SSL to encrypt communication between services, even for internal service to service calls.
          - Access Control : Implement role based access control and ensure that services only have access to the data and resources they need.
    9. Over engineering the system :
        - How to avoid ?
          - Use strangler pattern to migrate from monolith to microservice over time
         
5. How to handle the data consistency across services in a distributed system.
   1. Strong consistency :
      1. 2 PC
      2. 3 PC
   2. Eventual Consistency with event sourcing and CQRS :
      - We use patterns like event sourcing with CQRS where eventual consistency is acceptable.
      - In event sourcing, every change is captured as an immutable event, which can be replicated and processed asynchronously across distributed systems.
      - CQRS can help by separating read and write workloads, which allows you to scale them independently and handle eventual consistency on the write side, while maintaining fast reads.
    3. Distributed Transactions (SAGA Pattern) :
       - In a microservice system, where services are typically decoupled and don't share a single database, you can use SAGA pattern. This is a sequence of local transactions, where each service involved in the saga performs its transactions and publishes an event. If a step fails, compensation actions are taken to revert the previous steps. SAGA provides a way to handle long-running transactions in a distributed system without locking resources.
    4. Idempotency and Retries :
       - When desiging APIs ad services, ensure that operations are idempotent. This helps avoid data duplication in case of retries.
    5. Event driven architecture :
       - Leveraging this architecture allows for reliable event propogation across services. With event driven systems, services communicate via events, which ensures that the state is updated asynchronously, promting eventual consistency.

6. Challenges you faced when implementing microservices, and how do you address them ?
   1. Complexity in service communication :
      - As the number of microservices grow, so does the complexity of communication between them. Microservices often need to interact with one another through APIs, leading to potential issues like :
      - Sync communication causes bottlenecks
      - Unreliable communication leading to cascading failures
      - Difficulty in managing inter-service dependencies.
      - Solution :
        - Async communication.
        - Event Driven Architecture
        - API Gateway
        - Service Mesh
   2. Data Management and consistency :
       - Each service typically has its own db, which introduces challenges like
         - Ensuring all services have the latest and correct data.
         - Avoid unnecessary duplication of data across services.
         - Handling complex transactions accross multiple services.
        - Solution :
          - CQRS and Event sourcing
          - SAGA design pattern
          - event driven design
          - database per service
          - data replication : for certain cases, you might replicate data to ensure services have access to the required data. 
   3. Service Discover & Mangement :
      - As microservices are deployed across dynamic environment (e.g. un containers or serverless functions), managing their locations(IPs,ports) becomes difficult.
      - Service discovery becomes critical, especially when services scale up and down or are deployed in the ephermeral environments like Kubernetes or AWS Lambda.
      - Managing the state and health of services is also complex.
      - Soluton :
        - Service Discovery : Consul, Eureka or Kubernetes built in service discovery to automatically register and discover services in your application.
        - Health Checks : Implement health checks (via spring boot actuator or kubernetes liveness/readiness probes) to monitor service health and trigger automatic scaling or restarts as needed.
        - Centralized Configuration : Use Spring Cloud Config or Consul to manage configuration centrally across services, ensuring consistency and simplifying management.
   4. Fault tolerance and Resilience :
      - Distributed systems are inherently prone to failures, whether due to network issues, service crashes or dependency failures. A failure in one service can cascade and bring down the entire system if not properly managed.
      - Solutions :
        - Circuit Breaker Pattern
        - Retries and Backoff
        - Timeout and Failover
        - Bulkheads.
   5. Monitoring , Logging and Debugging :
       - Solutions :
         - Use tools like Jaeger, Zipkin to trace requests as they flow across services.
         - Centralised logging : ELK Stack or Splunk
         - Metrics and Dashboards : Prometheus with Grafana
         - Alerting
   6. Managing Distributed Transactions :
       - Microservices often need to coordinate actions across different services, which introduces challenges around atomicity,consistency and reliability of transactions.
       - Solution :
         - SAGA or Event Driven Transactions (using Kafka)
   7. Security :
       - Solution :
         - API Gateway
         - OAuth2/JWT : for secure authentication and authorization
         - Transport security : TLS/SSL
         - Service Mesh
   8. CI/ CD :
       - Solution :
         - use tools like jenkins
         - use docker to containerize each microservice and kubernetes for its management
         - blue/green deployment
        
 6. How to maintain session between microservices.
    1. Use a centralised session store :(bad option)
       - Session data is stored in a centralized location, and both microservices access this session data when needed. Use an external session store such as Redis,Memcached or a distributed cache that all microservices can accesss to read/write session data.
         - Adv : Centralised session management, scalable across multiple microservices.
         - Disadv : single point of failure, latency      
    2. Use JWT (good approach)
       - Popular method for maintaing session state in a stateless microservices environment. JWTs can be used to store sesssion-related information in the form of a secure,encoded token that is passed between microservices with each request
       - When a use logs in, the authentication service generates a JWT containing the session information(e.g. user id,roles,etc) and signs it with a private key
       - The JWT is returned to the client(usually in an http set-cookie header or as part of the Authorization header)
       - The client sends this JWT with every subsequent request to other microservices.
       - Each microservice can then verify the JWT using a shared public key to authenticate the user and extract session-related data.
       - Adv : stateless (Each request is self contained eliminating the need for session management within the microservices themselves), scalabale (JWT tokens can be passed across microservices with minimal overhead), Secure(JWTs are cryptographically signed, ensuring data integrity and authenticity.)
       - Disadv : Token Epiration(Handling token expiration and renewal can be tricky. If token is compromised, you'll need a way to invalidate it), Token Size (JWTs can become large if they contain too much data which might impact network performance)
    3. Database backed session management :
       - This approach stores session data in a relational database(or NoSQL db) where it is managed centrally, and services query the database to retrieve session-related information.
       - When the user logs in, the authentication service generates a unique session ID and stores session information (like user id,roles,preferences, etc) in a db.
       - The microservices check the session id on each request and retrieve the associated session data from db to authenticate and authorise the user.
       - Adv : Reliable (centralised session management ensures that the session state is consistent across microservices), Persistence(session data is persisted and can be accessed even if the user disconnects and reconnects later)
       - Disadv : performance, scaling (requires careful database design (eg read replicas,caching) to avoid bottlenecks as the system scales)
    4. Session propogation via headers or custom metadata (mostly used)
       - Microservices can propogate session information (such as user identity or authentication data) via HTTP headers or custom metadata in API calls between services.
       - A central authentication service or API gateway authenticates users and propogates the session or user identity information in HTTP headers (eg: X-User-Id, X-User-Role, X-Auth-Token)
       - Each microservice extracts the session-related information from the headers and uses it to authorize and process the request.
       - This can be done using a lightweight token (like session ID or JWT) or custom headers containing session data.
       - Adv : decoupling(Each service doesn't need to manage sessions directly. The session data is simply passed along with the request), Centralised Control (Centralized authentication and authorisation simplify the security and session management process).
       - Disadv : Sensitive data exposure, limited to request lifetime(Unlike persistent session stores, session information is only available for the duration of the request and must be passed on every request)
    5. Use a distributed Cache (ed Redis,Memcached) (good approach)
       - This method involves using a distributed cache to store session data that needs to be accessible by multiple microservices. The cache is distributed, meaning all services can access it independently, making it highly scalable.
       - When a user logs in, the authentication service stores session data in a distributed cache like redis or memcached.
       - Each microservice accesses the session data from the distributed cache by using a session key (eg session ID).
       - Services use the cache to validate the user session or retrieve session-related information.
       - Adv : high performance(Caching provides fast access to session data with low latency), Scalable (distributed caches can handle high loads and scale horizontally)
       - Disadv : Consistency (Ensuring the cache is consistent and synchronised across multiple services can be challenging, esp if data changes in one service), Eviction (session data may be evicted or expire from the cache, causing potential session loss if not managed properly)
 


              
    
   
