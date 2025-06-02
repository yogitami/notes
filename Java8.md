# Java 8

### IntStream
:
1. IntStream is a specialized stream for working with int values.
2. Ways to create IntStream : Arrays.stream(int[] array), IntStream.range(start, end) or IntStream.rangeClosed(start, end).
3. 

| Methods              |  |  |
| :---------------- | :------: | ----: |
| filter()          | count()         | empty()      |
| map()             | findFirst()         |  collect()     |
| sum()             | findAny()          |       |
| average()         | noneMatch()         |       |
| min()/max()       | anyMatch()         |       |
| sorted()          | allMatch()         |       |
| forEach()         | flatMap()         |       |
| average()         |   peek()       |       |
| distinct()        |   skip()       |       |
| limit()           | mapToDouble         |       |
| reduce()          | mapToLong         |       |

Example of reduce():
```
public static void main(String[] args) {
        int number = 5;
        int factorial = IntStream.rangeClosed(1, number)
                                 .reduce(1, (a, b) -> a * b);
        System.out.println("Factorial of " + number + ": " + factorial);
    }
```

### Stream

| Methods              |  |  |
| :---------------- | :------: | ----: |
| filter()          | count()         | empty()      |
| map()             | findFirst()         |  of()     |
| sum()             | findAny()          |  collect()     |
| average()         | noneMatch()         | flatMapToDoube()       |
| min()/max()       | anyMatch()         | flatMapToInt()      |
| sorted()          | allMatch()         |  toList()     |
| forEach()         | flatMap()         | ofNullable()      |
| average()         |   peek()       |       |
| distinct()        |   skip()       |       |
| limit()           |  concat()        |       |
| reduce()          | toArray()         |       |

### Collectors

| Methods              |  |  |
| :---------------- | :------: | ----: |
| toList()          |  summarizingDouble()       |      |
| toMap()             | maxBy() : Optional<Person> oldestPerson = people.stream().collect(Collectors.maxBy(Comparator.comparingInt(Person::getAge)))        |       |
| groupingBy()             |           |       |
| counting()         |          |        |
| joining()       |          |       |
| partitioningBy()          |          |       |
| mapping()         |          |       |
| summarzingInt() : getCount(),getMax(),getMin(),getAverage()        |         |       |
| summingDouble()        |         |       |
| reducing() : Collectors.reducing(Integer::sum)           |         |       |
| averagingInt()          |         |       |

#### toMap()
```
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmployeeDepartmentExample {
    static class Employee {
        String name;
        String department;

        Employee(String name, String department) {
            this.name = name;
            this.department = department;
        }

        String getName() {
            return name;
        }

        String getDepartment() {
            return department;
        }
    }

    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
            new Employee("Alice", "HR"),
            new Employee("Bob", "IT"),
            new Employee("Charlie", "HR"),
            new Employee("David", "Finance")
        );

        // Collect the employees into a Map with the department as the key and names as the value
        Map<String, String> departmentMap = employees.stream()
                                                     .collect(Collectors.toMap(
                                                         Employee::getDepartment,
                                                         Employee::getName,
                                                         (existing, replacement) -> existing + ", " + replacement
                                                     ));

        System.out.println("Department Map: " + departmentMap);
    }
}
```


1. Given a list of strings, find out those strings which starts with a number.

```
        List<String> list = Arrays.asList("1apple","banana","4watermelon","mango");
        list.stream().filter(str -> !str.isEmpty() && Character.isDigit(str.charAt(0))).forEach(System.out::println);
```
2. Intstream is used to create a number with some range.
   ```
    // Java 7

               for(int i=0;i<str.length()/2;i++){
                if(str.charAt(i) != str.charAt(str.length()-1-i)){
                    System.out.println("Is not palindrom");
                    }
            }

   // Java 8
   IntStream is being used as a replacement for 'for' loop
     boolean isPalindrome = IntStream.range(0, str.length()/2).allMatch(i -> str.charAt(i) == str.charAt(str.length()-1-i));
   ```
   here allMatch() means that the condition we are adding it should match with all the characters.
   
3. Check if the number is prime
   ```
   public static boolean isPrimeOrNot(int num){
   if(num <= 1 ) return false;
    boolean isPrime = IntStream.rangeClosed(2, (int) Math.sqrt(num)).noneMatch(i -> num%i == 0);
   return isPrime;
   }
   ```

4. Sort the numbers in descending order.
   ```
   List<Double> decimals = Arrays.asList(3.14,0.89,6.9,3.4,1.3);
   decimals.stream().sorted(Comparator.reverseOrder()).forEach(System.out::println);
   ```
   
5. Find the nth smallest element in an array.
```
    int[] arr = {3,8,6,9,1};
        int n = 3;
        Arrays.stream(arr).sorted().skip(n-1).findFirst().ifPresent(System.out::println);

```

6. Given a list of strings, join them using '[' as prefix, ']' as suffix and ',' as delimiter.
   ```
   str.stream().collect(Collectors.joining("," , "[", "]"));
   ```
   
7. Find the sum of first 2 elements from the list.
   ```
    arr.stream().limit(2).mapToInt(Integer::intValue).sum();

   // if asked to multiply then use reduce
   int mul = arr.stream().limit(2).reduce( (a,b) -> a*b).get();
   ```
   Here, limit() will give the integer as output (autobox) we have to convert it to primary int and hence we are using mapToInt.
   
8. Find the words with k vowels in a given sentence.
   ```
        String sentence = "Hi I have two oranges and one apple";
        //vowels -> a,e,i,o,u
        int k = 3;
            Arrays.stream(sentence.split(" ")).filter(word -> countVowels(word) == k).forEach(System.out::println);
        }
        public static long countVowels(String word){
            return word.chars().mapToObj(ch -> (char)ch).filter(ch -> "aeiouAEIOU".indexOf(ch) != -1).count();
        }
   ```
   
9. Given a string, find the first non-repeating character
    ```
        String input = "swiss";

        // Java 7, use Map to store the characters and count and then iterate over the string and map again to check if the character has count==1, print it.

        // Java 8
        input.chars() // stream of integers basically the characters are converted to ASCII's
                .mapToObj(ch -> (char)ch) // convert it back to character
                .collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() == 1)
                .map(entry -> entry.getKey())
                .skip(1) // if we want the 2nd non-repeating character
                .findFirst().ifPresent(System.out::println);
    ```
10. Given a list of integer, find the max,min,avg,count
    ```
        List<Integer> numbers = Arrays.asList(1,2,3,4,5,9,7);
        IntSummaryStatistics stats = numbers.stream().mapToInt(n -> n).summaryStatistics();
            stats.getAverage();
            stats.getMax();
            stats.getMin();
            stats.getSum();
    ```
    *We can also use mapToInt(condition) and then use methods from it like sum(),average(),etc.*
    
    *Also, we can use .collect(Collectors.averagingInt(condition));*
12. Find the word with thrid highest length
    ```
    words.stream().sorted( (w1,w2) -> Integer.compare(w2.length(),w1.length())).skip(2).findFirst().ifPresent(System.out::println);
    words.stream().sorted(Comparator.comparingInt(String::length).reversed()).skip(2).findFirst().ifPresent(System.out::println); // Option 2
    ```
13. Given an interger array return true if array contains duplicate element return false otherwise.
    ```
    Set<Integer> result = new HashSet<>();
    boolean res = Arrays.stream(arr).anyMatch(value -> !result.add(value)); // Option 1
    Arrays.stream(arr).filter( value -> !result.add(value)).findAny().ifPresent(System.out::println); //Option 2
    boolean res = Arrays.stream(arr).distinct().count() != arr.length; // Option 3
    ```
14. Write a program to print the count of each character in a string.
    ```
    Map<String, Long> res = Arrays.stream(input.split("")).filter( s -> !s.isBlank())
                .collect(Collectors.groupingBy(Function.identity(),Collectors.counting()));
    ```
15. Find the intersection of two lists.
    ```
    List<Integer> combinedList = list1.stream().filter(list2::contains).toList();
    ```
16. How to patition the list of numbers in even and odd groups.
    ```
    Map<Boolean, List<Integer>> res  = num.stream().collect(Collectors.partitioningBy(o -> o % 2 == 0));
    ```
17. Sort by multiple fields using stream.
    ```
    List<Employee> employees = .....
    employees.stream().sorted(Comparator.comparingInt(Employee::getAge).thenComparing(Employee::getName).reversed()).forEach(System.out::println);
    ```
18. Find the longest string in a list of strings.
    ```
    Optional<String> res = words.stream().max(Collectors.comparingInt(String::length));
    ```
19. Given a list of string, remove the duplicates.
    ```
    words.stream().distinct().forEach(System.out::println); // Option1
    words.stream().collect(Collectors.toSet()); //Option 2
    ```
20. Print duplicate elements in a given list of integers.
    ```
    input.stream().collect(Collectors.groupingBy(Function::identity,Collectors.counting))
            .entrySet().stream()
            .filter(m -> m.getValue > 1).map( m -> m.getKey())
            .collect(Collectors.toSet()) ;
    ```
21. Customers whose lifetime spending exceeds a certain threshold.
    ```
    Map<Boolean,List<Customer>> result = customerList.stream().collect(Collectors.partitioningBy(customer -> customers.getTotalSpend() > 10000));
    ```
22. Counting the total number of orders placed by customers over a given period of time.
    ```
    long totalOrders = orderList.stream().collect(Collectors.counting()); // useful for generating daily,weekly or monthly sales report.
    ```
23. summazingInt
    ```
    IntSummaryStatics stats = numbers.stream().collect(Collectors.toSummarizingInt(Integer::intValue)); //getMin,getMax,etc.
    ```
24. Extract cutomer email address from list of orders, to create a list of email addresses for sending marketing or order status update.
    ```
    List<String> customerEmails = orderList.stream().collect(Collectors.mapping(order -> order.getCustomer.getEmail(), Collectors.toList()));
    ```
25. Group a list of string by length and count the number of strings in each group.
    ```
    Map<Integer,Long> result = stringList.stream().collect(Collectors.groupingBy(String::length, Collectors.counting));
    ```
26. Group orders by customer and then count how many orders each customer placed
    ```
    orderList.stream().collect(Collectors.groupingBy(Order::getCustomer, Collectors.counting()));
    ```
27. Collect customers in a set but then transform the set into the list of customer email addresses.
    ```
    customerList.stream().collect(Collectors.collectingAndThen(Collectors.toSet(), set -> set.stream().map(Customer::getEmail).toList));
    ```
28. collect the length of all strings in the list and sum them.
    ```
    words.stream().collect(Collectors.mapping(String::length,Collectors.summingInt(Integer::intValue)));
    ```
29. Collect all unique product names from a list of orders.
    ```
    orderList.stream().flatMap(order -> order.getProduct().stream()).collect(Collectors.mapping(Product::getName, Collectors.toSet()));
    ```
30. Create a map where each order id is associated with its total amount.
    ```
    orderList.stream().collect(Collectors.toMap(Order::getId, Order::getTotalAmount));
    ```
31. Find the sum of a list of integers.
    ```
    numbers.stream().collect(Collectors.reducing(0, Integer::sum));
    ```
32. Sum the total revenue from the list of orders
    ```
    orderList.stream().map(Order::getTotalAmount).collect(Collectors.reducing(0, Integer::sum));
    ```
33. Given List<Order> , Order has List<Product> , Product has category.
    - Generate a report the shows for each product category the total number of quantities sold in 24 hours but
      - only include orders where total values is more than 500$
      - product are not out of stock
      - he report must be sorted by total quantity in descending order.
        ```
        Map<String,Integer> result = orderList.stream().filter(order -> order.getOrderTime.isAfter(LocalDateTime.now().minusHours(24)))
                                        .filter(order -> order.getTotalValue > 500)
                                        .flatMap(order -> order.getProducts.stream())
                                        .filter(product -> product.isInStock())
                                        .collect(Collectors.groupingBy(Product.getCategory, Collectors.summingInt(Product::getQuantity)))
                                                .entrySet().stream().
                                                .sorted(Map.Entry.<String,Integer>comparingByValue(Comparator.reverseOrder()))
                                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,e2) -> e1, LinkedHashMap::new));
        ```
34. Your warehouse system tracks inventory level of products. During order processing, you need to flag orders where the ordered quantity exceeds the available stock.
    - You have
    - Map<String,Integer> productStock : productId -> available stock
    - List of order objects where each order has a list of product and each product has a product id and quantity
    - Identify which orders have issue with the stock availability
      ```
      List<String> problematicOrder = orderList.stream().flatMap(order -> order.getProducts.stream())
              .anyMatch(product -> {
              int availableStock = productStock.get(product.getId);
              return availableStock < product.getQuantity();
              }). map(Order::getId).toList();
      
      ```
