## Complete springboot project with Mappings

#### Object Mapping
1. This is particularly important in applications where data needs to be transformed and moved between different layers, such as from the database layer to the service layer and then to the presentation layer.
2. In Java applications, this often involves converting entities, which represent database records, into Data Transfer Objects (DTOs) used to transfer data to and from the client.
3. Entities are used for database interactions, while DTOs are used for transferring data between the server and client.
4. By using DTOs(Data Transfer objects), sensitive fields in entities (such as passwords) can be excluded from data transferred to the client.

##### Different mapping strategies.
1. Apache Commons BeanUtils
   - A utility library that provides methods to copy properties between Java beans.
     ```
     import org.apache.commons.beanutils.BeanUtils;
      public class BeanUtilsMapper {
          public static void map(Object source, Object target) {
              try {
                  BeanUtils.copyProperties(target, source);
              } catch (Exception e) {
                  e.printStackTrace();
              }}}
     ```
3. ModelMapper
   - A flexible and convention-based object mapping library that automates the mapping process.
     ```
     import org.modelmapper.ModelMapper;
      public class ModelMapperExample {
        private static final ModelMapper modelMapper = new ModelMapper();
        
        public UserDTO toDto(User user) { return modelMapper.map(user, UserDTO.class); }
        public User toEntity(UserDTO userDTO) { return modelMapper.map(userDTO, User.class); }
      }
     ```
5. MapStruct 
  - A compile-time code generator that creates type-safe mappings between Java beans.
  - MapStruct allows you to define custom mappings for fields that do not match by name or require special handling.
  - @Mapper is applied over an interface or an abstract class. It indicates that this interface is a Mapper, and the MapStruct processor will automatically generate an implementation for it.
  - 
    ```
    @Mapper(componentModel = "spring")
    public interface UserMapper {
        @Mapping(source = "login", target = "username")
        @Mapping(source = "address.street", target = "streetName")
        UserDTO toDto(User user);
    
        @Mapping(source = "username", target = "login")
        @Mapping(source = "streetName", target = "address.street")
        User toEntity(UserDTO userDTO);
    }
    ```
6. Jackson ObjectMapper
   - It allows converting Java objects to JSON and vice versa.
   - Its primary function is to handle JSON serialization and deserialization, not direct object-to-object mapping between DTOs and entities. 
   - Serialization: Converting Java objects to JSON.
   - Deserialization: Converting JSON to Java objects.
     ```
     // Serialization
     import com.fasterxml.jackson.databind.ObjectMapper;
      
      public class JacksonExample {
          public static void main(String[] args) {
              ObjectMapper objectMapper = new ObjectMapper();
              User user = new User(1L, "john.doe", "John", "Doe");
      
              try {
                  String jsonString = objectMapper.writeValueAsString(user);
                  System.out.println(jsonString);
              } catch (Exception e) { e.printStackTrace(); }}}
     // Deserialization
     import com.fasterxml.jackson.databind.ObjectMapper;

    public class JacksonExample {
        public static void main(String[] args) {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = "{\"id\":1,\"login\":\"john.doe\",\"firstName\":\"John\",\"lastName\":\"Doe\"}";
    
            try {
                User user = objectMapper.readValue(jsonString, User.class);
                System.out.println(user);
            } catch (Exception e) { e.printStackTrace(); }}}
     ```

#### Controller

```
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

  @PutMapping("/{id}")
  public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
      UserDTO updatedUser = userService.updateUser(id, userDTO);
      return ResponseEntity.ok(updatedUser);
  }
}
```

#### Service
**Returning Data in a Specific Format**
APIs often need to return data in a format that is different from the internal representation.
```
@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userMapper.updateEntityFromDto(userDTO, user);
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }
}
```

**Updating Database Entities with User-Provided Data**
- When users submit data through an API, this data often needs to be mapped to entities for database updates. This can involve validating and sanitizing user inputs before they are mapped to entities and persisted to the database.
- **Example of Partial Update**:
  - In this example, we use MapStruct for mapping objects and handle partial updates by only updating the fields that are provided in the DTO. We fetch the existing entity from the database and apply the changes from the DTO.
  - Dependencies : mapstruct, mapstruct-processor

#### MapStruct
- The @MappingTarget annotation is used to update the existing entity. By specifying @Mapping(target = “id”, ignore = true), we ensure that the ID is not overwritten during the update.
- 
```
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDto(User user);
    User toEntity(UserDTO userDTO);

    @Mapping(target = "id", ignore = true)  // Ignore the ID field during update
    void updateEntityFromDto(UserDTO userDTO, @MappingTarget User user);
}
```

#### Repo




#### Model class
1) Entity class
   ```
    import javax.persistence.Entity;
    import javax.persistence.GeneratedValue;
    import javax.persistence.GenerationType;
    import javax.persistence.Id;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import lombok.AllArgsConstructor;
    
    @Entity
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String login;
        private String firstName;
        private String lastName;
        private String password; // Sensitive data
    }
   ```
2) DTO class
   ```
    import javax.validation.constraints.NotNull;
    import javax.validation.constraints.Size;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import lombok.AllArgsConstructor;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class UserDTO {
        private Long id;
    
        @NotNull
        @Size(min = 1, max = 50)
        private String login;
    
        @NotNull
        @Size(min = 1, max = 50)
        private String firstName;
    
        @NotNull
        @Size(min = 1, max = 50)
        private String lastName;
    }
   ```



##### Tests
