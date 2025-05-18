# Design Patterns

### Creational
This pattern is used to define and describe how objects are created at class instantistion time.

#### Singleton :
   - Default time of bean in springboot.
   - Only one instance of class is created.
   - We can break Singleton by using Reflection, Cloneable and also Singleton if we don't rewrite the readResolve() method.
   - How to create a Singleton class?
       - Create private constructor
       - Create only getter method, no setter method
       - create a private static variable
         ```
            public class SingletonClass {
            private static SingletonClass instance;
        
            // Private constructor to prevent instantiation
            private SingletonClass() {}
        
            public static SingletonClass getInstance() {
                if (instance == null) {
                    synchronized (SingletonClass.class) {
                        if (instance == null) {
                            instance = new SingletonClass();
                        }
                    }
                }
                return instance;
            }
         }
         ```
     
    We can also make it as Serialised by implementing Serializable. In this case, we can serialise the singleton instance but after deseralizing it, it will not return a Singleton object. To resolve this, override the readResolve().
      ```
      protected Object readResolve() {
        return getInstance();
      }
      ```
#### Factory :
  - Create instances of beans dynamically.
  - Provides a way to delegate the object creation logic to a central class ("factory")
  - Supports open close principle
  - Root of Dependency Injection framework
  - Ex: Calendar.getInstance() from java
  - @Bean is an example where the type of object being invoked is decided at runtime.
      ```
        interface PaymentService, method : processPayment(double amount);
          |-------- implementation class : CreditCardPayment -- have it's own implementation of processPayment(double amount)
          |-------- implementation class : PayPalPayment -- have it's own implementation of processPayment(double amount)
          |-------- implmentation class : UPIPayment -- have it's own implementation of processPayment(double amount)

        Factory class :
          class PaymentFactory{
      
            public static PaymentService getPaymentService(String paymentType){
              if(paymentType == null) return null;
              if(paymentType.equalsIgnoreCase("CREDITCARD")) return new CreditCardPayment();
              if(paymentType.equalsIgnoreCase("PAYPAL")) return new PayPalPayment();
              if(paymentType.equalsIgnoreCase("UPI")) return new UPIPayment();
              return null;
            }
          }
      ```
  
#### Builder
  - Ensures **immutability** and flexibility by creating object step by step.
  - Prevents constructor overloading.
  - Lombok , use @Builder over the class you want to create the builder for and make all the fields as private final
  - Order.builder().id(..).name(..).build();
    
#### Prototype : 
   - Cloneable
   - To be used when object creation is costly.
   - Here we can cache the object and return its clone on next request.

### Structural
- how to assemble different parts of the system so that changes in one doesn't affect the entire system.
- They help organize relationships between objects, making systems more flexible, reusable, and maintainable.
- focus on how classes and objects are arranged to create larger, more complex structures

#### Proxy Pattern :
  - Useful to reduce expensive API calls. Used to check for authentication.
  - Advantages : access control/protection given by proxy object, caching, logging
  - Ex: Suppose you have a third party API for payment & you allow user to check the status of payment. Now the third party charges some amount for every API call to them so you will be charged very big amount. In this case use Proxy Pattern to reduce the API calls to reduce unnecessary API calls while ensuring upto date transaction status.
  - You use Caching (Proxy pattern) in this case.
  - @Transactional in Springboot works on this pattern.
    **Design**
      - Create a proxy later : which will add as an intermediary between your application & third party payment API. This proxy will cache the transaction status responses, and before forwarding the request to the third party service, it will check whether the transaction status is already cached. (Zomato)
      - Caching Mechanism :
        The cache will store the transaction statuses, and a cache expiration time will be set to ensure that the information is periodically refreshed.
    ```
    interface PaymentService -- getTransactionStatus(String transactionId)
      |-- RealPaymentService ---- it's getTransactionStatus has the expensive third party call
      |-- CachedPaymentService

    public class CachedPaymentService implements PaymentService {
      private RealPaymentService rps = new RealPaymentService();
      private Map<String, CachedTransactionStatus> cache = new HashMap<>();
    
      public String getTransactionStatus(String transactionId){
        CachedTransactionStatus cacheStatus = cache.get(transactionId);
        if(cacheStatus != null && !isExpired(cacheStatus)) return cacheStatus.getStatus();
    
        String status = rps.getTransactionStatus(transactionId);
        cache.put(transactionId, new CachedTransactionStatus(status, System.currentTimeMillis()));  // CachedTransactionStatus has status and long timestamp as fields
      }

      private boolean isExpired(CachedTransactionStatus cacheStatus ){
        long expiryTime = 5 * 60 * 1000; // 5 mins
        return System.currentTimeMillis() - cacheStatus.getTimestamp() > expiryTime;
      }

    } 
    ```
    - if we are not allowed to go into the Zomato code to add the caching functionality. It can be done using decorator pattern. In case of spring boot, we can use @Cacheable
#### Decorator Pattern :
   - Allow behaviour to be added to individual objects either dynamically, without affecting the behaviour of the other objects from the same class
   - Extend or modify the behaviour at runtime .i.e. allows dynamic addition of responsibilities to objects without modifying their exisitng code.
   - Aggregation or composition(dynamic) : PART OF relationship
   - Cabilities of inheritance with subclasses.
   - Supports open close principle
   - It allows developers to compose objects with different combinations of functionalities at runtime.
   - It is to be used when we want to avoid having too many different classes for all the possible combinations of features.
   - Ex: InputStream, OutputStream, Reader, Writer, etc.
   - Ex : video streaming platform , where video content is the base content and other features such as enable subtitle, subtitle language, video quality, etc are decorators.
   - Ex :
        Suppose we are building a coffee shop application where customers can order different types of coffee. Each coffee can have various optional add-ons such as milk, sugar, whipped cream, etc. We want to implement a system where we can dynamically add these add-ons to a coffee order without modifying the coffee classes themselves.

     ```
     interface Coffee ---- getDescription(),getCost()
        |-- PlainCoffee
        |-- CoffeeDecorator (abstract class) which implements Coffee and also holds its reference
              |-- MilkDecorator (have a parameterised constructor which uses super to refer to parent)
              |-- SugarDecorator () (have a parameterised constructor which uses super to refer to parent)

        public abstract class CoffeeDecorator implements Coffee {
          protected Coffee decoratedCoffee;
      
          public CoffeeDecorator(Coffee decoratedCoffee) { this.decoratedCoffee = decoratedCoffee;}
      
          @Override
          public String getDescription() { return decoratedCoffee.getDescription();}
      
          @Override
          public double getCost() { return decoratedCoffee.getCost();}

         // MilkDecorator.java
            public class MilkDecorator extends CoffeeDecorator {
                public MilkDecorator(Coffee decoratedCoffee) {
                    super(decoratedCoffee);
                }
            
                @Override
                public String getDescription() {
                    return decoratedCoffee.getDescription() + ", Milk";
                }
            
                @Override
                public double getCost() {
                    return decoratedCoffee.getCost() + 0.5;
                }
            }

         // SugarDecorator.java
            public class SugarDecorator extends CoffeeDecorator {
                public SugarDecorator(Coffee decoratedCoffee) {super(decoratedCoffee);}
            
                @Override
                public String getDescription() {return decoratedCoffee.getDescription() + ", Sugar";}
            
                @Override
                public double getCost() {return decoratedCoffee.getCost() + 0.2;}
            }

        // Main class
        Coffee coffee = new PlainCoffee();
        Coffee milkCoffee = new MilkDecorator(new PlainCoffee());
        Coffee sugarMilkCoffee = new SugarDecorator(new MilkDecorator(new PlainCoffee()));

        or we can also use the same object
        Coffee coffee = new PlainCoffee();
        coffee = new MilkDecorator(coffee);
        coffee = new SugarDecorator(coffee);
         
     ```
**famous question is Pizza question where we need to add different toppings on Pizza** 
     

#### Flyweight Pattern :
   - is a shared object that can be used in multiple contexts simultaneously.
   - Use : applciation needs large number of objects that share most of the common attribute.
   - Memory consumption by shared objects.
   - Ex : Integer.valueOf(int) -- returns cached object instead of creating new

#### Adapter Pattern : 
  - Adapter Pattern is particularly useful for integrating classes with incompatible interfaces. (Just like a normal adapter)
  - It works as a bridge between two interfaces, enabling classes that couldn’t otherwise work together to communicate by converting the interface of a class into another interface that clients expect.
  - Encourages following SRP by delegating the responsibility of interface adaptation to a separate class.
  - Structure of Adapter pattern :
    1. Target - domain specific interface that client uses.
    2. Adapter - implements Target interface and adapts the Adaptee to it.
    3. Adaptee: An existing class with an incompatible interface that needs adapting.
    4. Client: The class that interacts with the Target interface.
    5. Real world examples : File Format Converters, Arrays.asList().
    6. Ex : You are developing a modern e-commerce platform that interacts with various payment gateways. However, one of the payment gateways uses an outdated interface that is incompatible with your platform’s current payment interface. Instead of rewriting the legacy code, you decide to use the Adapter Pattern to integrate it seamlessly.
       
    ```
    interface IPaymentProcessor --- void processPayment(String accountNumber, BigDecimal amount);
    OldPaymentSystem class --- void makePayment(String cardNumber, double amount)

    public class PaymentAdapter implements IPaymentProcessor {
      @Autowired OldPaymentSystem ops;

      public void processPayment(String accountNumber, BigDecimal amount){
          // Convert decimal to double for the legacy system
        double convertedAmount = amount.doubleValue();
        ops.makePayment(accountNumber, convertedAmount);
      }
    }
    ```
  

### Behavioural
   - Assignment of responsibilities between objects.
   - focus on how objects and classes interact and communicate

#### State Method Design Pattern
   - When an object modifies its behavior according to its internal state, the state design pattern is applied.
   - If we have to change the behavior of an object based on its state, we can have a state variable in the Object and use the if-else condition block to perform different actions based on the state.
   - Used in CQRS with event sourcing.
     
#### Template Pattern :
   - defines the skeleton of an algorithm in the superclass but let subclasses override the specific steps of the algo without changing it's structure.
   - Ex: JDBCTemplate -> execute() method -- accepts a callback obj that defines the SQL statement to execute & parameters to pass the statement.
   - JDBCTemplate takes care of all the opening/closing db connections,executing the statement and handling exceptions. We pass the datasource to the JDBCTemplate constructor.
   - Concept of abstract class works like this.

#### Observer Pattern :
   - Allows one object to observe the changes in another object & react to those changes.
   - It establishes a one-to-many dependency between objects, meaning that all of the dependents (observers) of the subject are immediately updated and notified when the subject changes.
   - Kafka & RabbitMQ
   - EventListener
   - Ex : Consider a scenario where you have a weather monitoring system. Different parts of your application need to be updated when the weather conditions change.
     ```
     public interface Subject {
          void addObserver(Observer observer);
          void removeObserver(Observer observer);
          void notifyObservers();
      }

     public interface Observer {
       void update(String weather);
      }

     public class WeatherStation implements Subject {
          private List<Observer> observers = new ArrayList<>();
          private String weather;
      
          @Override
          public void addObserver(Observer observer) {observers.add(observer);}
      
          @Override
          public void removeObserver(Observer observer) {observers.remove(observer);}
      
          @Override
          public void notifyObservers() {
              for (Observer observer : observers) {observer.update(weather);}
          }
      
          public void setWeather(String newWeather) {
              this.weather = newWeather;
              notifyObservers();
          }
      }

        public class PhoneDisplay implements Observer {
          private String weather;
      
          @Override
          public void update(String weather) {
              this.weather = weather;
              display();
          }
      
          private void display() {System.out.println("Phone Display: Weather updated - " + weather);}
      }

      class TVDisplay implements Observer {
       private String weather;
    
       @Override
       public void update(String weather) {
           this.weather = weather;
           display();
       }
    
       private void display() { System.out.println("TV Display: Weather updated - " + weather); }
      }

     // Main
     WeatherStation weatherStation = new WeatherStation();
      Observer phoneDisplay = new PhoneDisplay();
      Observer tvDisplay = new TVDisplay();
      weatherStation.addObserver(phoneDisplay);
      weatherStation.addObserver(tvDisplay);
        // Simulating weather change
      weatherStation.setWeather("Sunny");
     ```

#### Strategy Design Pattern :
   - Selecting an algo at runtime.
   - Comparator works on this.
   - Ex : different sort algo at runtime, different emailing system to send emails
   - Implementation :
        1. Make an interface and it's implementing classes.
        2. Make a strategy class which will take the parent interface as variable and use this keyword for it's construction. It has two methods : change the stategy at runtime & execute()
    
     ```
     Enum EncodingPatternEnum ---> MD5,SHA1,SHA2
     interface Encryption ---> void encrypt(String), EncodingPatternEnum getEncryptionType()
        |-- MD5Encryption
        |-- SHA1Encryption
        |-- SHA2Encryption

     EncryptionFactory --->
           Map<EncodingPatternEnum,Encryption> map;
            public EncryptionFactory(Set<Encryption> encryptionTypeSet){
              createStrategy(encryptionTypeSet);
           }
           private void createStrategy(Set<Encryption> encryptionTypeSet){
              map = new HashMap<EncodingPatternEnum,Encryption>();
              encryptionTypeSet.stream().forEach(m -> map.put(encryptionTypeSet.getEncryptionType,encryptionTypeSet));
           }
           public Encryption findEncryptionType(EncodingPatternEnum epn){
              return map.get(epn);
           }

        // Controller
           @Autowired EncryptionFactory ef;
           // call in getmapping which accepts EncodingPatternEnum as argument
           ef.findEncryptionType(epn).encrypt("testing);
     ```
    

## Design Patterns in Microservices.

#### API Gateway
- Single point of entry for all the clients
- We can also use Zuul API gateway : @EnableZuulProxy, @EnableDiscoveryClient
- For spring cloud gateway :
     - We can perform authentication here and also modify request and response.
     - Spring cloud gateway forwards requests to a Gateway Mapping, which determines what should be done with requests matching a specific route.
     - Route configuration can be resolved by using RouteLocator. Create a Bean of it. We have to add thhe host,filters,uril etc. It uses builder design pattern.
     - We can create RouteLocator either by using @Bean or by using properties configuration (dynamic routing).

#### Service Registry & Discovery Pattern
   - A service registry keeps track of all the services in the system, making it easier for them to find each other.
   - Every service needs to regsiter itself with service registry when it starts up & deregister when it shuts down.
   - We don't need to hardcode hostnames and port like this --- Eureka Integration
   - Ex :
      - 3 services & a netflix eureka discovery server using spring cloud.
      - Server will have spring-cloud-starter-netflix-eureka-server dependency and client services will have spring-cloud-starter-netflix-eureka-client dependency.
      - Add @EnableEurekaServer with the discovery server class & also set other properties in property file like eureka instance hostname, client.enabled= true, service url,server.port,etc.
      - In services too, add some properties to enable the interaction.
      - To call Service A from Service B, we need to @EnableEurekaClient and we would need a DiscoveryClient & restclient. And call the service using them.
      - Service Registry : Every microservice registers itself with Eureka Server.
      - Service Discovery : One microservice discovers another microservice with the help of its entry in Eureka Server.

#### CQRS
   - Separates read(query) & write(command) operations into different models.
   - This can involve using distint methods,objects or even separate dbs for handling data retrieval & data modification operations.
   - Uses kafka generally.
     
#### SAGA
   - A SAGA is a sequence of local transactions where each transaction updates data within a single service. If one transaction fails, saga ensure that the overall business transaction is rolled back through compensatory transaction.
#### Circuit Breaker
   - This pattern prevents a network/service failure from cascading to other services. If a service fails to respond, the circuit breaker trips and the call is redirected or fails gracefully.
   - Resilience4j integration
   - Hystrix
   - States : Closed(idel),open,half-open
   - Circuit breaker types : count-based, time-based
   - Implementation :
        - add the resilience4j-spring-boot2 & resilience4j-reactor dependency.
        - In Controller
          ```
          @GetMapping("..")
          @CircuitBreaker(name = "..",fallbackMethod = "getInvoiceFallBack")
          public String getInvoice() {....}
          public String getInvoiceFallBack(Exception e) { return "some string"; }
          ```
        - add properties for resilience4j in applicaiton.properties.



