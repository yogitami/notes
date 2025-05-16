# Spring

- Using SpringBoot over Spring?
    - Springboot provides autoconfiguration, built over spring mainly for REST APIs
    - help create stand only application.
    - provides embedded servers and provides support for in memory db (h2).
    - pom.xml internally handles the dependencies.

### Exception Handling
  - Creating Custom Exceptions
    ```
    public class NoSuchCustomerExistsException extends RuntimeException {
    private String message;

    public NoSuchCustomerExistsException() {}

    public NoSuchCustomerExistsException(String msg) {
        super(msg);
        this.message = msg;
        }
      }
    ```
    - Service class will use the throw keyword to throw the exception and then we can use @ExceptionHandler(value = NoSuchCustomerExistsException.class) in the controller to handle the exception
    - In controller the logic will be :
      ```
      @GetMapping("/getCustomer/{id}")
      public Customer getCustomer(@PathVariable("id") Long id) {
        return customerService.getCustomer(id);
      }

      // Adding exception handlers for NoSuchCustomerExistsException 
      // and NoSuchElementException.
      @ExceptionHandler(value = NoSuchCustomerExistsException.class)
      @ResponseStatus(HttpStatus.NOT_FOUND)
      public ErrorResponse handleNoSuchCustomerExistsException(NoSuchCustomerExistsException ex) {
          return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
      }
      @ExceptionHandler(value = NoSuchElementException.class)
      @ResponseStatus(HttpStatus.NOT_FOUND)
      public ErrorResponse handleNoSuchElementException(NoSuchElementException ex) {
          return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
      }
      @Data
      @AllArgsConstructor
      @NoArgsConstructor
      public class ErrorResponse {
        private int statusCode;
        private String message;
        public ErrorResponse(String message)
        {
          super();
          this.message = message;
        }
      }
      ```

### Service class
  - Code
    ```
        public Customer getCustomer(Long id) {
        return customerRespository.findById(id).orElseThrow(
            () -> new NoSuchElementException("NO CUSTOMER PRESENT WITH ID = " + id));
        }

        public String addCustomer(Customer customer) {
        Optional<Customer> existingCustomer = customerRespository.findById(customer.getId());
        if (!existingCustomer.isPresent()) {
            customerRespository.save(customer);
            return "Customer added successfully";
          } else {
            throw new CustomerAlreadyExistsException("Customer already exists!!");
          }
        }

        public String updateCustomer(Customer customer) {
        Optional<Customer> existingCustomer = customerRespository.findById(customer.getId());
        if (!existingCustomer.isPresent()) {
            throw new NoSuchCustomerExistsException("No Such Customer exists!!");
            } else {
            existingCustomer.get().setName(customer.getName());
            existingCustomer.get().setAddress(customer.getAddress());
            customerRespository.save(existingCustomer.get());
            return "Record updated Successfully";
            }
        }
    ```


### POJO & DAO with JPA
1. DAO :
  - interface Repo_Name extends JpaRepository <POJO_CLASSName,(Primary Key type (String,etc))>
  - We have to annotate it with @Repository
  - if we are using @query here, then write in this way
      - @Query(Select c from customers where cutomerId = :cid AND status IN (:stat)); --- here cutomerId and status are the way values is stored in POJO and not DB
2. POJO :
  - @Entity
  - @Table(table_name)
  - attribute level : @Id, @GeneratedValue(strategy = GenerationType.AUTO), @Column(name = "customer_id", length = 1024), @Column(name = "metadata", columnDefinition = "json") , @Nonnull, @Convert(converter = AssetTransactionMetadataConverter.class), @Enumerated(EnumType.STRING), @Version,
  - @Type(type = "com.getbux.broker.backoffice.commons.jpa.PersistentBigMoneyType")
    @Columns(columns = {@Column(name = "currency"), @Column(name = "transaction_value")})
    BigMoney txnValue;
  - @MappedSuperclass : and the class is abstract at this level, we extend this class and other main classes and then define the @entity there. However we can define and @id here.

### Scopes
Scopes in Spring : Used with @Bean, @Scope("prototype") or @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  - Singleton : single application context
  - Prototype : Person personPrototypeA = (Person) applicationContext.getBean("personPrototype");
  - Request : Creates a bean instance for a single HTTP request.

    (@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS) or @RequestScope public HelloMessageGenerator requestScopedBean() {
    return new HelloMessageGenerator();})
    
    (@Resource(name = "requestScopedBean"))
  - Session : Creates a bean instance for http session.
  - Application : Creates a bean instance for the lifecycle of a ServletContext.
  - Web socket : Creates a bean instance for a particular WebSocket session.

### Transactions
Transactions in Spring : Metadata used for managing transactions in the springboot application.
  - We can either enable it by adding @EnableTransactionManagement with configuration class or it will be enabled by default if we have spring-data-* or spring-tx dependencies.
  - We can now annotate a bean with @Transactional either at the class or method level. Ususally used with @Service.
  - Configuration :
        - Propagation Type, solation Level, Timeout, readOnly flag (a hint for the persistence provider that the transaction should be read only), Rollback rules.
        - Rollback happens for runtime, unchecked exceptions only. The checked exception does not trigger a rollback of the transaction. We can, of course, configure this behavior            with the rollbackFor and noRollbackFor annotation parameters.
        - @Transactional(rollbackFor = { SQLException.class })
  - Only public methods should be annotated with @Transactional.
  - Spring creates proxies for all the classes annotated with @Transactional that's why only external calls will be intercepted. Any self invocation calls will not start transaction.

### Database
Database
  - if with JPA, spring.data.source.url, username, password, spring.jpa.properties.hibernate.dialect, spring.jpa.hibernate.ddl-auto=update, spring.jpa.show-sql=true

### Model Mapper
There would be cases where we have sensitive data stored in our database and we don't want everything to go away with get request in such a case we can use **Model Mapper** which is provided by maven.
  - Create a DTO class for model : like an adapter it will just have information then we want to send out.
  - add the model mapper dependency
  - code in service class.
    ```
     @Autowired
    private ModelMapper modelMapper;
    
    @Override
    public UserDto getUser(int userId) {
        User user = this.userRepository.findById(userId).get();
        UserDto userDto = this.modelMapper.map(user, UserDto.class);
        return userDto;
    }
    ```
    - Add the model mapper bean in configuration class
      ```
      @Bean
      public ModelMapper getModelMapper() {
        return new ModelMapper();
        }
      ```
