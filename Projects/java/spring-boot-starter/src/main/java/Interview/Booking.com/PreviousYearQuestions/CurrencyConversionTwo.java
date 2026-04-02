package org.example.booking;

/*
    Question

    You are given a list of currency conversion rates.
    Each rate is represented as:
        [fromCurrency, toCurrency, rate]
    which means:
        1 fromCurrency = rate * toCurrency
    Example:
        ["USD", "JPY", 110]
        ["USD", "AUD", 1.45]
        ["JPY", "GBP", 0.007]

    You are also given a query:
        [fromCurrency, toCurrency]

    Your task is to compute the conversion rate between the two currencies.

    You may need to go through intermediate currencies.

    Example:

        Rates:
        ["USD","JPY",110]
        ["USD","AUD",1.45]
        ["JPY","GBP",0.007]

        Query:
        ["GBP","AUD"]

    Output:
        1.89

    Explanation:

        GBP → JPY → USD → AUD

        GBP → JPY = 1 / 0.007
        JPY → USD = 1 / 110
        USD → AUD = 1.45

        Multiply the rates along the path.

    If conversion is not possible, return -1.
    */

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CurrencyConversionTwo {

    public static double dfs(String source, String target, Set<String> visited, double product, Map<String,Map<String,Double>> map){
        double res = -1.0;
        visited.add(source);
        Map<String,Double> currentMap = map.get(source);
        if(currentMap.containsKey(target)) {
            res = product * currentMap.get(target);
        }else {
            for(Map.Entry<String,Double> entry : currentMap.entrySet()){
                String nextNode = entry.getKey();
                if(visited.contains(nextNode)) continue;
                res = dfs(nextNode,target,visited, product*entry.getValue(),map);
                if(res != -1.0) break;
            }
        }
        return res;
    }
    public static double computeCurrencyConversion( List<String[]> rates,List<String> queries){

        // construct a hashmap for fast lookup of currencies.

        Map<String,Map<String,Double>> map = new HashMap<>();
        for(int i=0 ;i<rates.size();i++){
            String[] element = rates.get(i);

            String dividend = element[0];
            String divisor = element[1];
            double quo = Double.parseDouble(element[2]);

            map.putIfAbsent(dividend, new HashMap<>());
            map.putIfAbsent(divisor, new HashMap<>());

            map.get(dividend).put(divisor,quo);
            map.get(divisor).put(dividend, 1/quo);
        }

        String source = queries.get(0);
        String target = queries.get(1);

        Set<String> visited = new HashSet<>();

       if( !map.containsKey(source) || !map.containsKey(target)) return -1.0;
       else if(source.equals(target)) return 1.0;
       else {
        return dfs(source,target,visited,1,map);
       }
    }

    public static void main(String args[]){
        List<String[]> rates = List.of(
                new String[]{"USD", "JPY", "110"}, // 1 USD = 110 * JPY
                new String[]{"USD", "AUD", "1.45"},
                new String[]{"JPY", "GBP", "0.007"}
        );

        String from = "GBP";
        String to = "AUD";

        List<String> queries = List.of(from, to);

       double exchangeRateCost = computeCurrencyConversion(rates, queries);
       System.out.println(exchangeRateCost);

    }
}
