package org.example.booking;

/*
    Given a list of hotelId, parentHotelId and a score retrieve the top k root parentHotelIds with highest scores:
    [{0, 1, 10}, {1, 2, 20}, {3, 4, 10}, {7, 8, 5}] K = 2
    Result: [[2, 30], [4,10]]
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class HotelIdsWithScore {

    public static String dfs(String id, Map<String,String> parentMap, Map<String,String> cacheMap) {
        if(cacheMap.containsKey(id)) return cacheMap.get(id);

        if(!parentMap.containsKey(id)) return id;

        String parent = dfs(parentMap.get(id),parentMap,cacheMap);
        cacheMap.put(id, parent);

        return parent;
    }

    public static List<List<String>> getTopKParentHotelIds(List<List<String>> records, int k) {
        List<List<String>> result = new ArrayList<>();
        Map<String,String> parentMap = new HashMap<>();
        Map<String,Integer> scorMap = new HashMap<>();
        Map<String,String> cacheMap = new HashMap<>();

        //  compute the adjacency map
        for(int i=0;i<records.size();i++) {
           List<String> record = records.get(i);
           parentMap.put(record.get(0), record.get(1));
        }

        // find the root using dfs for every record
        for(int i=0;i<records.size();i++){
            List<String> record = records.get(i);
            String parent = dfs(record.get(0), parentMap, cacheMap);
            scorMap.put(parent, scorMap.getOrDefault(parent,0) + Integer.parseInt(record.get(2)));
        }

        // find top k elements with highest score
        // min heap
//        PriorityQueue<Map.Entry<String,Integer>> priorityQueue = new PriorityQueue<>(
//                (a,b) -> Integer.compare(a.getValue(),b.getValue())
//        );
//
//        for(Map.Entry<String,Integer> entry : scorMap.entrySet()){
//            if(priorityQueue.size() < k){
//                priorityQueue.offer(entry);
//            }
//            else if(priorityQueue.peek().getValue() < entry.getValue()){
//                priorityQueue.poll();
//                priorityQueue.offer(entry);
//            }
//        }
//
//        while (!priorityQueue.isEmpty()){
//            Map.Entry<String,Integer> entry = priorityQueue.poll();
//            result.add(List.of(entry.getKey(),entry.getValue().toString()));
//        }
//
//        result.sort((a,b) -> Integer.compare(Integer.parseInt(b.get(1)),Integer.parseInt(a.get(1))));


        // max heap would be a better option here
        PriorityQueue<Map.Entry<String,Integer>> priorityQueue = new PriorityQueue<>(
                (a,b) -> Integer.compare(b.getValue(),a.getValue())
        );

        for(Map.Entry<String,Integer> entry : scorMap.entrySet()){
            priorityQueue.offer(entry);
        }

        while (!priorityQueue.isEmpty() && result.size() < k){
            Map.Entry<String,Integer> entry = priorityQueue.poll();
            result.add(List.of(entry.getKey(),entry.getValue().toString()));
        }

        return result;
    }

    public static void main(String args[]){
        List<List<String>> input = List.of(
                List.of("0", "1", "10"),
                List.of("1", "2", "20"),
                List.of("3", "4", "10"),
                List.of("7", "8", "5")
        );
        int k = 2;
        List<List<String>> result = getTopKParentHotelIds(input, k);
        System.out.println(result);
    }
}
