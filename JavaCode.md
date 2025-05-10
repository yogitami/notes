# Java Codes

1. Number is prime or not ?
   
```
public static void isPrime(int nunm) {
 if(num <=1 ) return false;
 for(int i=2 ; i <= Math.sqrt(num); i++){
    if(num % i == 0) return false;
  }
return true;
}
```
