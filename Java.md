# JAVA

1) Association :
    - A generic term to indicate the relationship between two independent classes.
    - one-to-one, one-to-many, or many-to-many
    - Ex: Bank can have many employees, Teacher and Student
2) Aggregation :
   - Specific form of association, in which one class, the whole, contains a collection of other classes, the parts; here, however, the lifecycle of the parts does not depend upon the whole.
   - **"has a"** relationship.
   - In Aggregation, both entries can survive individually which means ending one entity will not affect the other entity.
   - One-to-one, one-to-many, many-to-one, many-to-many
   - It is a unidirectional association i.e. a one-way relationship. For example, a department can have students but vice versa is not possible and thus unidirectional in nature.
   - Ex : Institute ---> List<Department> departments, Department ---> List<Student> , Library and Books
   - Student Has-A name. Student Has-A ID. Department Has-A Students
3) Composition :
   - is a stronger form of aggregation that means ownership and lifecycle dependence; if the whole gets destroyed, then the parts no longer exist. For composition, a good example would be a house and its different rooms; a room cannot exist without a house.
   - **"part-of"** relationship between classes.
   - In composition, both entities are dependent on each other.
   - One-to-one, one-to-many
   - When there is a composition between two entities, the composed object cannot exist without the other entity.
   - Ex: House ---> List<Room> rooms , Car and Engine
