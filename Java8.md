# Java 8

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
20. Duplicate elements in a given list of integers.
    ```
    input.stream().collect(Collectors.groupingBy(Function::identity,Collectors.counting))
            .entrySet().stream()
            .filter(m -> m.getValue > 1).map( m -> m.getKey())
            .collect(Collectors.toSet()) ;
    ```
