package org.example.booking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

record Transaction(String type,String currency,double amount){}

record Rate(int currency, double feePerUnit){}

public class CurrencyConversion {

    public static double[] processTransactions(Map<String, Map<String, Double>> feeMap, List<Transaction> transactions){

        // Step 1 : get all the currencies
        Set<String> currencies = new HashSet<>(feeMap.keySet());

        for(Map<String,Double> entry : feeMap.values()){
            currencies.addAll(entry.keySet());
        }

        for(Transaction transaction : transactions){
            currencies.add(transaction.currency());
        }

        // convert to list
        List<String> currencyList = new ArrayList<>(currencies);

        // Step 2 : create a map of index
        Map<String,Integer> currencyIndexMap = new HashMap<>();
        for(int i=0;i<currencyList.size();i++){
            currencyIndexMap.put(currencyList.get(i),i);
        }

        // Floyd Warshal
        double[][] dis = buildMinFeeMatrix(currencyList,currencyIndexMap,feeMap);

        // Greedy algorithm (Select using Dijkstra's algorithm)
        List<List<Rate>> sortedSources = buildSortedSources(currencyList, dis);

        // Process transactions in order
        double[] balance = new double[currencyList.size()];
        double[] answer = new double[transactions.size()];
        for(int i=0;i<transactions.size();i++) {
            Transaction txn = transactions.get(i);
            String txnType = txn.type();
            String txnCurrency = txn.currency();
            double txnAmount = txn.amount();
            int currencyIndex = currencyIndexMap.get(txnCurrency);

            if (txnType.equals("PAYIN")) {
                balance[currencyIndex] = balance[currencyIndex] + txnAmount;
                answer[i] = 0;
            } else if (txnType.equals("PAYOUT")) {
                answer[i] = processPayout(txnAmount, currencyIndex, balance, sortedSources);
            }
        }
        return answer;
    }

    public static double  processPayout(double txnAmount, int currencyIndex, double[] balance, List<List<Rate>> sortedSources){
        double totalFee = 0.0;
        double remainingAmount = txnAmount;

        double direct = Math.min(balance[currencyIndex],remainingAmount);
        remainingAmount = remainingAmount - direct;
        balance[currencyIndex] = balance[currencyIndex] - direct;

        if(remainingAmount == 0.0) return 0.0;

        // 2. Check if enough reachable balance exists
        double totalBalance = 0.0;
        for(Rate rate : sortedSources.get(currencyIndex)){
            totalBalance = totalBalance + balance[rate.currency()];
            if (totalBalance >= remainingAmount) {
                break;
            }
        }

        if(totalBalance < remainingAmount) return -1.0;

        // 3. Consume cheapest effective sources first
        for(Rate rate: sortedSources.get(currencyIndex)){
            if(remainingAmount == 0.0) break;

            double available = balance[rate.currency()];
            if(available <= 0.0) continue;

            double take = Math.min(remainingAmount, available);
            balance[rate.currency()] = balance[rate.currency()] - take;
            totalFee += take * rate.feePerUnit();
            remainingAmount = remainingAmount - take;
        }
        return  totalFee;
    }

    private static List<List<Rate>> buildSortedSources(List<String> currencyList, double[][] dis){
        List<List<Rate>> sortedSourcesByTarget = new ArrayList<>();
        int n = currencyList.size();

        for(int target=0;target<n;target++){
            List<Rate> list = new ArrayList<>();
            for(int source=0;source<n;source++){
                if(source == target) continue;
                if(!Double.isInfinite(dis[source][target])){
                    list.add(new Rate(source, dis[source][target]));
                }
            }
            list.sort(Comparator.comparingDouble(Rate::feePerUnit));
            sortedSourcesByTarget.add(list);
        }
        return sortedSourcesByTarget;
    }

    public static double[][] buildMinFeeMatrix(List<String> currencyList, Map<String,Integer> currencyIndexMap, Map<String, Map<String, Double>> feeMap){
        int n = currencyList.size();
        double[][] dis = new double[n][n];
        double INF = Double.POSITIVE_INFINITY;

        // Create a matrix

        for(int i=0;i<n;i++){
            Arrays.fill(dis[i],INF);
            dis[i][i] = 0.0;
        }

        for(Map.Entry<String,Map<String, Double>> entry : feeMap.entrySet()){
            String fromCurrency = entry.getKey();
            for(Map.Entry<String, Double> toCurrencies : entry.getValue().entrySet()){
                String toCurrency = toCurrencies.getKey();
                double fee = toCurrencies.getValue();
                dis[currencyIndexMap.get(fromCurrency)][currencyIndexMap.get(toCurrency)] = Math.min(fee,dis[currencyIndexMap.get(fromCurrency)][currencyIndexMap.get(toCurrency)]);
            }
        }

        // Floyd-Warshall: minimum fee per unit between all currency pairs
        // Rearrange the matrix
        for(int k=0 ;k<n;k++){
            for(int i=0;i<n;i++){
                if(dis[i][k] == INF) continue;
                for(int j=0;j<n;j++){
                    if(dis[k][j] == INF) continue;
                    dis[i][j] = Math.min(dis[i][j], dis[i][k]+dis[k][j]);
                }
            }
        }
        return dis;
    }

    public static void main(String args[]){
        Map<String, Map<String, Double>> feeMap = new HashMap<>();

        feeMap.put("AAA", Map.of("BBB", 1.2, "CCC", 1.5));
        feeMap.put("BBB", Map.of("AAA", 1.1, "CCC", 1.4));
        feeMap.put("CCC", Map.of("AAA", 1.3, "BBB", 1.25));

        List<Transaction> transactions = List.of(
                new Transaction("PAYIN", "AAA", 100),
                new Transaction("PAYIN", "BBB", 50),
                new Transaction("PAYOUT", "CCC", 60),
                new Transaction("PAYOUT", "AAA", 30)
        );

        double[] result = processTransactions(feeMap, transactions);

        System.out.println(Arrays.toString(result));
    }
}
