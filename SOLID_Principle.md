
# SOLID Principles

SOLID Principles helps in the following ways : 

1. Loose Coupling
1. Parallel Development
1. Reduces complexity of code
1. Better Testability
1. Reduce error
1. Increase readability & maintenance

### S -> Single Responsibility Principle

1. Every java class must perform a single functionlaity.
1. Easy to maintain & understand

Ex : 
	
	1. Suppose we are asked to create an application that performs a user registration & log in. We also want to send an email to the user based on his status of login & registration. Also we might want to log any exceptions as well.

	interface User -> 
		login(), register() methods

	interface LoggerInfo ->
		logError()

	interface Email ->
		sendEmail();

	2. Suppose we have a marker class which has some properties like name,color,year,price.
	Now if we want to perform different functions like calculating total,printing invoice and saving in the db. The way to use SRP in this case is :

		InvoiceCalculator class -> calculateTotal()

		InvoicePrinter class -> printInvoice()

		InvoiceDAO class -> saveInDb()


### O -> Open Close Principle

1. Open for extension but closed for modification.

Ex : 
	Take the same InvoiceDAO from previous, we are currently using it to saveToDb() now a requirement comes to save into file.

	Now if we create a new method in InvoiceDAO class, saveToFile() it violates  the open close principle.
	The correct way to do this would be :

	interface InvoiceDAO -> save(Invoice invoice)
		|						|
	DatabaseInvoiceDAO		  FileInvoiceDAO
	       override the save() method


### L -> Liskov Substitution Principle

1. If class B is a subtype of class A, then we should be able to replace object of A with B without breaking the behaviour of the progran.
2. No new exceptions can be thrown by the subtype
3. Clients should not know which specific subtype they are calling.

Ex : 

	interface Bike ->
		turnOnEngine(), accelerate()
	class MotorCycle implements Bike ->
		turnOnEngine(), accelerate()
	class Bicycle implements Bike ->
		turnOnEngine(), accelerate() ---- Breaks logic 

Problem :

	interface Vehicle ->
		getNoOfWheels(), hasEngine()
	class Motorcycle implements Vehicle ->
		override all the methods
	class Car implements Vehicle ->
		override all the methods
	class Bicycle implements Vehicle ->
		override all the methods ------------ Breaks the logic

**Correct Solution**

	interface Vehicle -> (only generic methods)
		getNoOfWheels()
	class Bicycle implements Vehicle
	interface EngineVehicle ->
		classes -> Motorcycle & Car


### I -> Interface Segregation Principle

1. Interface should be such that client should not implement unnecessary functions they do not need.

Ex : Suppose we have 

		interface Resturant ->
			washDishes(),serveCustomers(),cookFood()

		class Waiter implements Resturant -> 
		it will have to override all the methods through the only job of waiter is to serve food.					

   **Correct Solution**
        Divide the interface into smaller interfaces.

        	interface Waiter ->
        		serveCustomer(), takeOrder() ----- waiter class needs to implement this interface
        	interface Chef ->
        		cookFood(), decideMenu()



### D -> Dependency Inversion Principle

1. Class should depend on interfaces rather than concrete classes.

Ex : 
	 interface Keyboard ->
	 	class BluetoothKeyboard & class WiredKeyboard
	 interface Mouse ->
	 	class BluetoothMouse & class WiredMouse

	 	class Macbook
	 	{ private final WiredKeyboard keyboard;
	 	  private final WiredMouse mouse;

	 	  // constructor using new WiredKeyboard --- 
	 	 } // incorrect as class is depending on concrete class

**Correct Way**

	class Macbook
	 	{ private final Keyboard keyboard;
	 	  private final Mouse mouse;

	 	  // constructor using this.keyboard = keyboard
	 	} 
