# JAVA

1) Association :
    - A generic term to indicate the relationship between two independent classes.
    - one-to-one, one-to-many, or many-to-many
    - Assocation is different from inheritance
    - Inheritance is a "IS-A" relation and associatio is a "HAS-A" relationship. (Car IS A Vehicle, Hotel HAS Rooms).
    - Inheritance : All these examples denote that sub type is in the form of super type creating is-a relationship.
    - Association : This means the Inter Connection between 2 entities/objects in real world which simply called Association
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
4) Composition :
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
