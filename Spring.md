# Spring

- Using SpringBoot over Spring?
    - Springboot provides autoconfiguration, built over spring mainly for REST APIs
    - help create stand only application.
    - provides embedded servers and provides support for in memory db (h2).
    - pom.xml internally handles the dependencies.
 
1. [Annotations](#Annotations)
2. [Hibernate Validator](#HibernateValidator)
3. [JPAAnnotations](#JPAAnnotations)
4. [IOCAndDI](#IOCAndDI)
5. [ExceptionHandling](#ExceptionHandling)
6. [Service](#Service)
7. [Controller](#Controller)
8. [Scopes](#Scopes)
9. [POJO](#POJO)
10. [Transactions](#Transactions)
11. [Database](#Database)
12. [Model Mapper](#ModelMapper)
13. [Security](#Security)
14. [SpringBoot 3](#SpringBoot)
15. [Flyway and Liquibase](#FlywayLiquibase)
16. [Hibernate Mappings](#Hibernate_Mappings)


<a name = "Annotations" />

### Annotations
1. @ComponentScan(basePackages = "com.example")
2. @Value: This annotation injects values into fields from property files.
3. @Lazy: This annotation indicates that the bean should be lazily initialized.
4. @Required: This annotation marks a property as required for injection.
5. @PropertySource: This annotation specifies the location of property files for the application context.
6. @Import: This annotation imports other configuration classes.
7. @SpringBootApplication: This annotation is used to mark the main class of a Spring Boot application. It encapsulates @Configuration, @EnableAutoConfiguration, and @ComponentScan annotations with their default attributes.
8. @RequestBody: This annotation binds the body of the HTTP request to a Java object.
9. @ResponseBody: This annotation indicates that the return value of the method should be used as the response body.
10. @RestController: This annotation is a combination of @Controller and @ResponseBody, used for RESTful web services.
11. @ResponseStatus: This annotation specifies the HTTP status code for the response.
12. @Profile: This annotation is used to indicate that a @Component class or a @Bean method should only be used when a specific profile is active.
13. @Configuration: This annotation indicates that the class can be used by the Spring IoC container as a source of bean definitions.
14. @ComponentScan: This annotation is used to specify the base packages for scanning components.
15. @EnableAsync: This annotation enables asynchronous processing in Spring.
16. @Async: This annotation marks a method to be executed asynchronously.
17. @EnableScheduling: This annotation enables scheduling in Spring.
18. @Scheduled: This annotation marks a method to be executed on a fixed schedule.
19. @Transactional: This annotation marks a method or class as transactional.
24. @ConditionalOnProperty :
```
@Bean(name = "smsNotification")
@ConditionalOnProperty(prefix = "notification", name = "service", havingValue = "sms")
public NotificationSender notificationSender2() {
    return new SmsNotification();
}
In application.property file : notification.service=sms
```
25. @RequestMapping
```
@RequestMapping(
value = "/../.../...",
produces = MediaType.APPLICATION_JSON_VALUE,
consumes = MediaType.APPLICATION_JSON_VALUE)
```

<a name = "HibernateValidator" />

### Validation using Hibernate Validator
spring-boot-starter-validation dependency and hibernate-validator.
1. @NotNull(message = "Enter a valid Employee Id")
2. @NotEmpty(message = "Must not be Empty and NULL")
3. @NotBlank(message = "employee name can't be left empty")
4. @Min(value=18, message = "Minimum working age 18")
5. @Max(value=60, message = "Maximum working age 60") private Integer age;
6. @Size(min = 10, max = 100, message= "Address should have a length between 10 and 100 characters."))
7. @Email(message = "Please enter a valid email Id")
8. @Pattern(regexp = "^[0-9]{5}$", message = "Employee postal code must be a 5-digit number.")
9. Global exception handler.
    ```
        @ControllerAdvice
        public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
        
            @Override
            protected ResponseEntity<Object> handleMethodArgumentNotValid(
                    MethodArgumentNotValidException ex, HttpHeaders headers,
                    HttpStatus status, WebRequest request) {
        
                // Create a response body map
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("timestamp", Instant.now().toString()); // ISO-8601 timestamp
                responseBody.put("status", status.value());
                responseBody.put("error", status.getReasonPhrase()); // e.g., "Bad Request"
        
                // Collect all validation errors
                List<String> errors = ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(fieldError -> fieldError.getDefaultMessage())
                        .collect(Collectors.toList());
        
                responseBody.put("errors", errors);
        
                return new ResponseEntity<>(responseBody, headers, status);
            }
        }
    ```
10. We can also add @Validated at the class level on @RestController
11. The validations can also be added like this *@PathVariable("id") @Min(5) int id* in the controller.
12. @Positive @RequestParam(name = "id")  int id
13. @NegativeOrZero @RequestParam(name = "id")  int id

<a name = "JPAAnnotations" />

### Data JPA Annotations
1. @CreatedBy
2. @LastModifiedBy
3. @Query("FROM Person p WHERE p.name = :name") Person findByName(@Param("name") String name);
4. @Transient
5. @CreatedDate
6. @LastModifiedDate
7. @Procedure: This annotation marks a method for calling a stored procedure.
8. @Lock: This annotation specifies locking behavior for methods. We can configure the lock mode when we execute a repository query method : @Lock(LockModeType.NONE) with @Query. There are different types of lock mode available.
9. @Modifying: This annotation marks a query as modifying (e.g., for update or delete operations). It is used with @Query
10. @EnableJpaRepositories: This annotation enables JPA repositories in a Spring application. It has to be used with @Configuration
    
<a name = "IOCAndDI" />

### IOC & DI
**IOC :** It is a design principle where the control of object creation and lifecycle is managed by a framework or container rather than by the developer. Spring IOC Container is responsible for creating, configuring, and managing the lifecycle of objects called beans.

**DI:** It is a design pattern and a part of IOC container. It allows objects to be injected with their dependencies rather than creating those dependencies themselves.

1. Constructor Injection : immutable object, We can either annotate the constructor with @Autowired with this or just use this.

<a name = "ExceptionHandling" />

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
    - @ContollerAdvice : Global Exception Handler
      ```
        @ControllerAdvice
        public class GlobalExceptionHandler {
        
            @ExceptionHandler(value = NoSuchCustomerExistsException.class)
            @ResponseStatus(HttpStatus.NOT_FOUND)
            public @ResponseBody ErrorResponse handleException(NoSuchCustomerExistsException ex) {
                return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
            }
        }
      ```
<a name = "Service" />

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
<a name = "Controller" />

### Controller
    - for the parameters in the method, @Valid @RequestBody Department department

<a name = "Scopes" />

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

<a name = "POJO" />

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
  - GenerationType :
    - AUTO : automatically assigns the strategy based on type of db used.
    - IDENTITY : mysql, postgres , if sequential order is essential
    - SEQUENCE : mysql,oracle,postgres, if sequentail order is essential
    - TABLE : for full control

3. ** N+1 Problem **
    - N+1 query problem happens when hibernate or JPA executes one query to fetch the parent entities(N) & then executes N separate queries to fetch the associated child entities.
    - Ex : Department and List<Employee> ---> one to many mapping, if we execute findAll() on departmentRepo then N number of queries for employee  & 1 for department.
    - FIX : JOIN_FETCH :
      - @Query("SELECT d from Department d JOIN FETCH d.employees") List<Department> findAllWithEmployees(); --- only one query executes
      - SELECT d.*,e.* FROM department d LEFT JOIN employee e ON d.id = e.department_id;
    - FIX : Using EntityGraph **good option**, @BatchSize(size=10) used in model class.

4. Soft delete implementation in JPA/Hibernate.
   - It means making a record as inactive instead of physically deleting it from the database. This is useful when we want to retain historical data or recover deleted record.
   - Add a isDeleted flag and istead of deleting a record, update the flag to true.
     ```
     softDeleteUser(Long userId) ---> findbyId and if found then set deleted to true and do a entityManager.merge(user) // Marks as deleted instead of removing
     ```
   - We have to also make sure that those entities do not get fetched then use @Where(clause = "is_deleted = false") with @Entity

<a name = "Transactions" />

### Transactions
Transactions in Spring : Metadata used for managing transactions in the springboot application.
  - We can either enable it by adding @EnableTransactionManagement with configuration class or it will be enabled by default if we have spring-data-* or spring-tx dependencies.
  - We can now annotate a bean with @Transactional either at the class or method level. Ususally used with @Service.
  - ACID
    - Atomicity : all or nothing
    - Consistency : ensures that our transaction takes a system from one consistent state to another consistent state.
    - Isolation : changes that we perform within a transcation are not visible to anyother transaction until we commit them successfully.
    - Durability : ensures that committed changes get persisted.
  - Propogation :
    - REQUIRED is default type. Method needs a transaction either open a new one if it doesn't exist or use the existing one.
  - Configuration :
        - Propagation Type, solation Level, Timeout, readOnly flag (a hint for the persistence provider that the transaction should be read only) -- @Transactional(readOnly = true) , Rollback rules.
  - Only public methods should be annotated with @Transactional.
  - Spring creates proxies for all the classes annotated with @Transactional that's why only external calls will be intercepted. Any self invocation calls will not start transaction.

  - **IMP** Example Transaction :
    ```
    @Service
    public class UserService{
        @Transactional // Proxy is created on class level
        public void saveUser(User user){
            userReposioty.save(user);
        }
        public void createUser(){  // INCORRECT
            saveUser(new User());
        }
    }
    ```
So what happened above is that, when we are calling the saveUser in the same class then the proxy is not created and hence the transactional doesn't work. To make it work we need to call saveUser(User user) from some other class managed by Spring IOC.

    - @Transactional(propogation = Propogation.NEVER) //INCORRECT, transaction will never work. Use proper Propogation level. Default : Propogation.REQUIRED
    - @Transactional(isolation = Isolation.READ_COMMITTED) or Isolation.REPEATABLE_READ // Use these isolation levels generally. Never use READ_UNCOMMITTED
    
Rollback happens for runtime, unchecked exceptions only. 
The checked exception does not trigger a rollback of the transaction. 
We can, of course, configure this behavior with the rollbackFor and noRollbackFor annotation parameters.
    - @Transactional(rollbackFor = { SQLException.class, IOException.class,... })
    
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(User user) throws Exception{
        userReposioty.save(user);
        throw new Exception("Checked Exception");   --- it is not necessary to always use throw, there might be a case where we use a method which throws an exception                                                                 automatically. In any case, it has to be handled in the same way.
    }
    

<a name = "Database" />

### Database
1) H2 :
  - add the spring-boot-starter-data-jpa and h2 dependency.
  - spring.h2.console.enabled=true,spring.data.source.url, username, password, spring.jpa.properties.hibernate.dialect, spring.jpa.hibernate.ddl-auto=update, spring.jpa.generate-ddl=true.
    
2) MySQL :
  - spring.jpa.hibernate.ddl-auto=update, spring.jpa.show-sql=true, spring.data.source.url, username, password, spring.datasource.driver-class-name

3) Flyway database migration
   - In my project, database schema is maintained by using flyway but insertion and updation happens using JPA.
   - Built around a linear db versioning system.
   - flyway-core dependency & add the location of the sql files in property file
   - set the flyway enabled in spring to true
   - Purpose of flyway is to establish a consistent sequence of migrations that always result in the same db state.
   - Flyway tracks the applied migrations in a metadata table called flyway_schema_history ensuring they are executed only once.
   - 

4) Database configuration for springboot
      - We would need a DatabaseConfig class, the class will look like below.
          ```
            @Configuration
            @EnableJpaRepositories(basePackages = {"com.gfg.jpaquerymethods"})
            @EnableTransactionManagement
            @Profile({ "production", "development" })
            public class DatabaseConfig {
              @Bean
                @DependsOn("liquibase")
                public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
                    LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();
                    lef.setDataSource(dataSource);
                    lef.setJpaVendorAdapter(jpaVendorAdapter);
                    lef.setPackagesToScan("package_path");
                    return lef;
                }
            
                @Bean
                public JpaVendorAdapter jpaVendorAdapter() {
                    HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
                    hibernateJpaVendorAdapter.setDatabase(Database.MYSQL);
                    hibernateJpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQL5InnoDBDialect");
                    return hibernateJpaVendorAdapter;
                }
            
                @Bean
                public PlatformTransactionManager transactionManager() {
                    return new JpaTransactionManager();
                }
            
                @Bean
                public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
                    return new TransactionTemplate(transactionManager);
                }
            
                @Bean(destroyMethod = "close")
                @Lazy(false)
                public HikariDataSource dataSource(Environment environment) {
                    final HikariDataSource ds = new HikariDataSource();
                    ds.setMaximumPoolSize(10);
                    ds.setDataSourceClassName("com.mysql.cj.jdbc.MysqlDataSource");
                    ds.addDataSourceProperty("url", environment.getRequiredProperty("database.url"));
                    ds.addDataSourceProperty("user", environment.getProperty("DB_USER", "name"));
                    ds.addDataSourceProperty("password", environment.getProperty("DB_PASSWORD", "name"));
                    ds.addDataSourceProperty("cachePrepStmts", true);
                    ds.addDataSourceProperty("prepStmtCacheSize", 250);
                    ds.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
                    ds.addDataSourceProperty("useServerPrepStmts", true);
                    return ds;
                }
            
                @Bean(name = "liquibase")
                public SpringLiquibase createLiquibase(DataSource dataSource, Environment environment) {
                    SpringLiquibase liquibase = new SpringLiquibase();
                    liquibase.setDataSource(dataSource);
                    liquibase.setChangeLog("classpath:/schema/changelog.xml");
                    liquibase.setContexts(environment.getProperty("liquibase.context", ""));
                    return liquibase;
                }
            }
          ```
   5) In repository class,
      ```
        @Async
        @Query("SELECT contest.contestName FROM Contest contest where contest.id = :id") 
        Future<String> findContestAsyncById(@Param("id") Long id); 
      ```
   6) transactionManager & entityManagerFactory is required in the configuration class. We actually set the entityManagerFactory in the transactionManager.
   
<a name = "ModelMapper" />

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
<a name = "Security"/>

### Security
- add the spring-boot-starter-security and @EnableSpringSecurity

<a name = "SpringBoot"/>

### SpringBoot 3.0

**IMP** Keep in mind that if you upgraded one service to springboot 3 but that service has a parent depedency on some other service (ex: for getting JWT tokens) then that service has to be upgraded to springboot 3 as well otherwise everything will break. All the child dependencies have to be moved to springboot 3 then only the parent can be moved to springboot 3. 
    
- New Features :
1. Java 17+ : minimum version required is Java 17
2. Spring framwork 6, which comes with new features like support for native compilation using GraalVM.
3. Native Image Support
4. Jakarta EE 9 : package names changed from javax.* to jakarta.*
5. Improved DI : constructor injection and nullability
6. Updated dependencies

- Improving startup time and memory usage
  - **How ?**
    - Only the necessary classes are scanned, improving both startup speed and memory usage
    - Spring AOT (Ahead of time) process -> AOT processing optimizes application's code during build time, which reduces runtime reflection and improves performance (.i.e. if some classes can be loaded on complie time and not necessarily have to be loaded only on runtime then it would be done by AOT).
    - GraalVM Native Compilation.
   
- Impact of Jakarta EE 9 migration in Spring Boot 3.
  - We use jakarta.* namespace now instead of javax.* (ex: javax.persistence is now jakarta.persistence)
 
- If we talk in terms of configuration improvements and database then there is not much improved between springboot 2 and springboot 3. Some  minor improvements are :
  - Enhanced support for JAVA records in configuration (Java records are a part of Java 17)
  - Improved validation mechanisms & error handling.
  - Better integrations with Jakarta EE.
  - Flyway has to be upgraded to Jakarta EE compatibility.
 
-  Spring boot 3 simplifies authentication with JWT,OAuth2.0 and OpenID Connect. WebSecurityConfigurationAdapter is deprecated. In terms of configuration, we configure a bean has to be created for SecurityFilterChain with no @EnableWebSecurity and with no WebSecurityConfigurationAdapter.

<a name = "FlywayLiquibase"/>

#### Difference between Flyway and Liquibase.
- Flyway uses SQL scripts.
- Liquibase uses XML/YAML changelog.
- Flyway is more SQL-centric.
- Liquibase offers more database-agnostic migrations.
- property file :
  - flyway :
    spring.flyway.locations=classpath:db/migration
    spring.flyway.baseline-on-migration=true
  - liquibase :
    spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.xml

### Other annotations :

1. @JsonTypeName("CASH_PAYLOAD") @JsonInclude(NON_NULL) public class DetailsPayload implements ActionPayload { .... }
   - @JsonTypeName("CASH_DIVIDEND_PAYLOAD"):
     - Specifies the type name to be used when serializing or deserializing this class in JSON.
     - When this class is serialized, the JSON will include a type identifier with the value "CASH_DIVIDEND_PAYLOAD".
     - Useful in polymorphic type handling, where multiple classes implement the same interface or extend the same base class (in this case, ActionPayload).
3. @JsonTypeInfo(use = NAME, property = "type", include = EXTERNAL_PROPERTY) @JsonSubTypes({ @JsonSubTypes.Type(DetailsPayload.class) }) public interface ActionPayload { }
   - This setup allows Jackson to serialize and deserialize objects of different subtypes (e.g., CashDividendCorporateActionDetailsPayload) based on the type property in the JSON. It is useful for handling polymorphic data structures in APIs or data exchange formats.
   - @JsonTypeInfo(use = NAME, property = "type", include = EXTERNAL_PROPERTY):
     - Configures Jackson to handle polymorphic types by including a type identifier in the JSON.
     - use = NAME: Specifies that the type identifier will be the name of the class (or a custom name defined in @JsonSubTypes).
     - property = "type": Indicates the JSON property that will hold the type identifier.
     - include = EXTERNAL_PROPERTY: Specifies that the type property is not part of the object itself but is provided externally in the JSON.
    - @JsonSubTypes :
      - Defines the possible subtypes of the ActionPayload interface.
      - @JsonSubTypes.Type(DetailsPayload.class): Maps the DetailsPayload class as a valid subtype.
5. @JsonProperty(value = "grossDividend", required = true) BigMoney grossDividend
6. @JsonCreato :
   - The @JsonCreator annotation is part of the Jackson library and is used to define a constructor or factory method that Jackson should use when deserializing JSON into an object.
   - Ex :
     ```
     @JsonCreator
     public User(
       @JsonProperty("username") String username, @JsonProperty("email") String email) {
            this.username = username;
            this.email = email;
     }
     ```

<a name = "Hibernate_Mappings"/>

### Hibernate Mappings

1. @OneToOne Mapping
   - ```
     public class Account {
     }
     ```


