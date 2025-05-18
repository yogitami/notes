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

#### Proxy Pattern :
  - Useful to reduce expensive API calls. Used to check for authentication.
  - Advantages : access control/protection given by proxy object, caching, logging
  - Ex: Suppose you have a third party API for payment & you allow user to check the status of payment. Now the third party charges some amount for every API call to them so you will be charged very big amount. In this case use Proxy Pattern to reduce the API calls to reduce unnecessary API calls while ensuring upto date transaction status.
  - You use Caching (Proxy pattern) in this case.
  - @Transactional in Springboot works on this pattern.
    **Design**
      - Create a proxy later : which will add as an intermediary between your application & third party payment API. This proxy will cache the transaction status responses, and before forwarding the request to the third party service, it will check whether the transaction status is already cached.
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
     
#### Template Pattern :
   - defines the skeleton of an algorithm in the superclass but let subclasses override the specific steps of the algo without changing it's structure.
   - Ex: JDBCTemplate -> execute() method -- accepts a callback obj that defines the SQL statement to execute & parameters to pass the statement.
   - JDBCTemplate takes care of all the opening/closing db connections,executing the statement and handling exceptions. We pass the datasource to the JDBCTemplate constructor.
   - Concept of abstract class works like this.

#### Observer Pattern :
   - Allows one object to observe the changes in another object & react to those changes.
   - Kafka & RabbitMQ
   - EventListener

#### Strategy Design Pattern :
   - Selecting an algo at runtime.
   - Comparator works on this.
   - Implementation :
        1. Make an interface and it's implementing classes.
        2. Make a strategy class which will take the parent interface as variable and use this keyword for it's construction. It has two methods : change the stategy at runtime & execute()
    

## Design Patterns in Microservices.

#### API Gateway
#### 











