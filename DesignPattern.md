# Design Patterns

### Creational
This pattern is used to define and describe how objects are created at class instantistion time.

#### Singleton :
   - Default time of bean in springboot.
   - Only one instance of class is created.
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
    
#### Prototype : Cloneable

### Structural

#### Proxy Pattern :
  - Useful to reduce expensive API calls.
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
