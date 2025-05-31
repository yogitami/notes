# JAVA

1) Association :
    - A generic term to indicate the relationship between two independent classes.
    - one-to-one, one-to-many, or many-to-many
    - Assocation is different from inheritance
    - Inheritance is a "IS-A" relation and associatio is a "HAS-A" relationship. (Car IS A Vehicle, Hotel HAS Rooms).
    - Inheritance : All these examples denote that sub type is in the form of super type creating is-a relationship.
    - Association : This means the Inter Connection between 2 entities/objects in real world which simply called Association. This represents the ownership of an entity in another. One entity belongs to another.
    - Ex: Bank can have many employees, Teacher and Student
      ```
      // Class 1 // Bank class
      class Bank {
          private String bankName;
          private Set<Employee> employees;
  
          public Bank(String bankName){this.bankName = bankName;}
          public String getBankName() {return this.bankName;}
          public void setEmployees(Set<Employee> employees){this.employees = employees;}
                  public Set<Employee> getEmployees(){return this.employees;}
        }
        
        // Class 2 // Employee class
        class Employee {
            private String name;
            public Employee(String name) {this.name = name;}
            public String getEmployeeName() {return this.name;}
        }
        
        // Class 3 // Association between both the classes in main method
        class AssociationExample {
            public static void main(String[] args)
            {
                Employee emp1 = new Employee("Ridhi");
                Employee emp2 = new Employee("Vijay");
                Set<Employee> employees = new HashSet<>();
                employees.add(emp1);
                employees.add(emp2);
                Bank bank = new Bank("ICICI");
                bank.setEmployees(employees); 
                for (Employee emp : bank.getEmployees()) {
                      System.out.println(emp.getEmployeeName()+ " belongs to bank " + bank.getBankName());
                }
            }
        }
      ```
2) Aggregation :
   - Specific form of association, in which one class, the whole, contains a collection of other classes, the parts; here, however, the lifecycle of the parts does not depend upon the whole.
   - **"has a"** relationship.
   - In Aggregation, both entries can survive individually which means ending one entity will not affect the other entity.
   - One-to-one, one-to-many, many-to-one, many-to-many
   - It is a unidirectional association i.e. a one-way relationship. For example, a department can have students but vice versa is not possible and thus unidirectional in nature.
   - Ex : Institute ---> List<Department> departments, Department ---> List<Student> , Library and Books
   - Student Has-A name. Student Has-A ID. Department Has-A Students
     ```
     class Student {
         private String studentName;
         private int studentId;
         public Student(String studentName, int studentId){
             this.studentName = studentName;
             this.studentId = studentId;
            }
         public int getstudentId() {return studentId;}
         public String getstudentName() {return studentName; }
     }
        
        // Department class contains list of Students
     class Department {
         private String deptName;
         private List<Student> students;
         public Department(String deptName, List<Student> students){
            this.deptName = deptName;
            this.students = students;
         }
         public List<Student> getStudents() {return students; }
         public void addStudent(Student student){students.add(student);}
     }

     class Institute {
         private String instituteName;
         private List<Department> departments;
         public Institute(String instituteName,List<Department> departments){
            this.instituteName = instituteName;
            this.departments = departments;
         }
    
         public void addDepartment(Department department) { departments.add(department);}
    
        // Method of Institute class
        // Counting total students in the institute
         public int getTotalStudentsInInstitute()
         {
             int noOfStudents = 0;
             List<Student> students = null;
             for (Department dept : departments) {
                students = dept.getStudents();
                for (Student s : students) {
                    noOfStudents++;}}
            return noOfStudents;
         }
     }
     
     class AggregationExample {
        public static void main(String[] args){
            Student s1 = new Student("Parul", 1);
            Student s2 = new Student("Sachin", 2);
            Student s3 = new Student("Priya", 1);
            Student s4 = new Student("Rahul", 2);
    
            // Creating an list of CSE Students
            List<Student> cse_students = new ArrayList<Student>();
            cse_students.add(s1);
            cse_students.add(s2);
    
            // Creating an initial list of EE Students
            List<Student> ee_students = new ArrayList<Student>();
            ee_students.add(s3);
            ee_students.add(s4);
    
            // Creating Department object with a Students list
            // using Aggregation (Department "has" students)
            Department CSE = new Department("CSE", cse_students);
            Department EE = new Department("EE", ee_students);
    
            // Creating an initial list of Departments
            List<Department> departments = new ArrayList<Department>();
            departments.add(CSE);
            departments.add(EE);
    
            // Creating an Institute object with Departments list
            // using Aggregation (Institute "has" Departments)
            Institute institute = new Institute("BITS", departments);
    
            // Display message for better readability
            System.out.print("Total students in institute: ");
    
            // Calling method to get total number of students
            // in the institute and printing on console
            System.out.print(
                institute.getTotalStudentsInInstitute());
        }}
     ```
3) Composition :
   - is a stronger form of aggregation that means ownership and lifecycle dependence; if the whole gets destroyed, then the parts no longer exist. For composition, a good example would be a house and its different rooms; a room cannot exist without a house.
   - **"part-of"** relationship between classes.
   - In composition, both entities are dependent on each other.
   - One-to-one, one-to-many
   - When there is a composition between two entities, the composed object cannot exist without the other entity.
   - Ex: House ---> List<Room> rooms , Car and Engine
     ```
     class Room {
         private String roomName;
         public Room(String roomName) {this.roomName = roomName;}
         public String getRoomName() {return roomName;}
     }
     class House {
         private String houseName;
         private List<Room> rooms;
         public House(String houseName) {
             this.houseName = houseName;
             this.rooms = new ArrayList<>();
         }
         public void addRoom(Room room) { rooms.add(room); }   
         public List<Room> getRooms() { return new ArrayList<>(rooms); }
         public int getTotalRooms() { return rooms.size(); }
     }
            
     class CompositionExample {
           public static void main(String[] args) {
             House house = new House("Dream House");
                house.addRoom(new Room("Living Room"));
                house.addRoom(new Room("Bedroom"));
                house.addRoom(new Room("Kitchen"));
                house.addRoom(new Room("Bathroom"));
                int r = house.getTotalRooms();
                System.out.println("Total Rooms: " + r);
        
                System.out.println("Room names: ");
                for (Room room : house.getRooms()) {
                    System.out.println("- " + room.getRoomName());
                }
             }
     }
     ```

4) JAVA 21
   - Sequenced Collection interface : ability to remove/find first and last items and reverse the order in collections with defined encounter order.
   - Adds geberational collection capability to the Z garbage collector.
   - record patterns : allows for easier and safer retrieval of data out of a record class
     ```
     record Point(int x, int y) {}
     Point point = new Point(10, 20);
     if (point instanceof Point(var x, var y)) {
         System.out.println("x: " + x + ", y: " + y);
     }
     ```
   - Pattern matching for switch : better than using instanceOf
     ```
     switch (obj) {
            case String s -> System.out.println("String: " + s);
            case Integer i -> System.out.println("Integer: " + i);
            default -> System.out.println("Unknown type");
     }
     ```
     second example :
     ```
     static double getBalanceWithSwitchPattern(Account account) { //  Account is the parent class and others are child class
        double result = 0;
        switch (account) {
            case null -> throw new RuntimeException("Oops, account is null");
            case SavingsAccount sa -> result = sa.getSavings();
            case TermAccount ta -> result = ta.getTermAccount();
            case CurrentAccount ca -> result = ca.getCurrentAccount();
            default -> result = account.getBalance();
        };
        return result;
        }
     ```
   - Virtual threads : lightweight threads that dramatically reduce the effort of writing,maintaining and observing high-throughput concurrent applications.
     ```
     var executor = Executors.newVirtualThreadPerTaskExecutor();
     executor.submit(() -> System.out.println("Hello, Virtual Thread!"));
     ```
   - String templates
    ```
       String name = "Baeldung";
       String welcomeText = STR."Welcome to \{name}";
       System.out.println(welcomeText);
     ```
   - unnamed patterns and variables : improves experience of deconstructing records, particularly those with many components.

5) Java 17
   - Sealed Classes (Finalized) — Restrict class inheritance.
     ```
     // Sealed class allowing only specific subclasses
        public sealed class Vehicle permits Car, Bike {
            public void start() {
                System.out.println("Vehicle is starting...");
            }
        }
        
        // Permitted subclass
        final class Car extends Vehicle {
            public void drive() {
                System.out.println("Car is driving...");
            }
        }
        
        // Permitted subclass
        public non-sealed class Bike extends Vehicle {
            public void ride() {
                System.out.println("Bike is riding...");
            }
        }

         // ✅ Allowed: ElectricBike can extend Bike (since Bike is non-sealed)
        public class ElectricBike extends Bike {}
        
        // ❌ The following will cause a compilation error
        // class Truck extends Vehicle {} // Not permitted!
     ```
    - Pattern matching for switch
      ```
      static void process(Object obj) {
        switch (obj) {
            case Integer i -> System.out.println("Integer: " + (i * 2));
            case String s -> System.out.println("String: " + s.toUpperCase());
            case Double d -> System.out.println("Double: " + (d + 10.5));
            case null -> System.out.println("Null value provided!");
            default -> System.out.println("Unknown type!");
            }
        }
      ```
    - Records : Before records, we had to manually write constructors, getters, toString(), equals(), and hashCode(), making the code verbose.
      - Records are immutable data classes, They simplify the creation of data-carrying classes by automatically providing:
        - Immutable fields (no setters).
        - Generated constructor, equals(), hashCode(), and toString().
          ```
          public record Employee(String name, int age) {
            public String greet() {
                return "Hello, my name is " + name + " and I am " + age + " years old.";
            }
          }
          ```
    - Text Blocks – Multi-line string literals (""")
      ```
      String json = """
        {
          "name": "Alice",
          "age": 25,
          "city": "New York"
        }
        """.stripIndent();
        System.out.println(json);
      ```
    - InstanceOf
      ```
      Object obj = 42;
        if (obj instanceof Integer num && num > 10) { //  No need for explicit null checks, instanceof always returns false for null values
            System.out.println("Number is greater than 10");
        }
      ```

6) Java 11
   - New methods to String class : isBlank, lines, strip, stripLeading, stripTrailing, and repeat.
     ```
     String multilineString = "Baeldung helps \n \n developers \n explore Java.";
     List<String> lines = multilineString.lines()
          .filter(line -> !line.isBlank())
          .map(String::strip) // strip is similar to trim() method of string
          .collect(Collectors.toList());
     assertThat(lines).containsExactly("Baeldung helps", "developers", "explore Java.");
     ```
   - New static File methods : readString, writeString
   -  java.util.Collection interface contains a new default toArray method which takes an IntFunction argument :
     ```
     List sampleList = Arrays.asList("Java", "Kotlin");
     String[] sampleArray = sampleList.toArray(String[]::new);
     ```
   - The not predicate method
     ```
     List<String> sampleList = Arrays.asList("Java", "\n \n", "Kotlin", " ");
     List withoutBlanks = sampleList.stream()
          .filter(Predicate.not(String::isBlank))
          .collect(Collectors.toList());
     assertThat(withoutBlanks).containsExactly("Java", "Kotlin");
     ```
   - We don’t need to compile the Java source files with javac explicitly anymore. Just run using java command.
   - In lambda expressions, var variables are used.
   - With the help of the asMatchPredicate() method, pattern recognition is possible
