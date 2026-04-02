package org.example.booking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/*
    given historical user travel data as (user_id, city) visits. Design a class that:
    Takes history_visits: List[UserVisit] and k in its constructor
    Exposes recommend_city(user_id)
    The goal is to recommend up to k cities to a user based on other users’ behavior.

    Do not recommend cities already visited by the user
    Rank cities by recommendation score (descending)
    If scores tie, return cities in lexicographic order
 */

class UserVisit {
    int userId;
    String city;

    public UserVisit(int userId, String city) {
        this.userId = userId;
        this.city = city;
    }
}
public class RecommendCities {

    private List<UserVisit> historyVisits = new ArrayList<>();
    private int recommendations;
    private Map<Integer, Set<String>> userToCityMap = new HashMap<>();
    private Map<String, Set<Integer>> cityToUserMap = new HashMap<>();

    public RecommendCities(List<UserVisit> history_visits, int k) {
        this.historyVisits = history_visits;
        this.recommendations = k;

        for(UserVisit userVisit : historyVisits){
            // userid, city
            userToCityMap.putIfAbsent(userVisit.userId, new HashSet<>());
            userToCityMap.get(userVisit.userId).add(userVisit.city);

            cityToUserMap.computeIfAbsent(userVisit.city, ignore -> new HashSet<>()).add(userVisit.userId);
        }

    }

    public List<String> topKRecommendationsForUser(int userId){

        // for user find the cities visited by him
        Set<String> citiesVisited = userToCityMap.get(userId);

        if(citiesVisited == null || citiesVisited.isEmpty()) return Collections.emptyList();

        // find the other users who visited the same city and make a Map of similarity score based on user id
        Map<Integer, Integer> userSimilarityScoreMap = new HashMap<>();

        for(Map.Entry<String, Set<Integer>> entry : cityToUserMap.entrySet()){
           String  city = entry.getKey();
           Set<Integer> users = entry.getValue();
           if(citiesVisited.contains(city)){
               for(int user : users)
                   if(user != userId)
                     userSimilarityScoreMap.merge(user,1,Integer::sum);
           }
        }

        // on the basis of the similarityMap make a map which has the cities not visited by the user with its city score

        Map<String,Integer> cityWithScore =  new HashMap<>();

        for(Map.Entry<Integer,Integer> entry : userSimilarityScoreMap.entrySet()){
            int otherUserId = entry.getKey();;
            int score = entry.getValue();
            Set<String> cities = userToCityMap.get(otherUserId);
            for(String city : cities){
                if(citiesVisited.contains(city)) continue;
                cityWithScore.merge(city, score, Integer::sum);
            }
        }

        // score desc, if scores same them alphabetic order
        // min heap
        PriorityQueue<Map.Entry<String,Integer>> priorityQueue = new PriorityQueue<>(
        (a,b) -> {
            if(a.getValue() == b.getValue()){
                return a.getKey().compareTo(b.getKey());
            }
            return Integer.compare(a.getValue(),b.getValue());
        }
        );

        List<String> result = new ArrayList<>();

        for(Map.Entry<String,Integer> entry : cityWithScore.entrySet()){
            priorityQueue.offer(entry);
            if(priorityQueue.size() > recommendations) priorityQueue.poll();
        }

        while (!priorityQueue.isEmpty()){
            result.add(priorityQueue.poll().getKey());
        }

        Collections.reverse(result);
        return result;
    }

    public static void main(String args[]){

        List<UserVisit> historyVisits = Arrays.asList(
         new UserVisit(1, "Paris"),
         new UserVisit(1, "Rome"),

         new UserVisit(2, "Paris"),
         new UserVisit(2, "Rome"),
         new UserVisit(2, "Berlin"),

         new UserVisit(3, "Paris"),
         new UserVisit(3, "Madrid"),

         new UserVisit(4, "Tokyo"),
         new UserVisit(4, "Seoul")
        );

       int k = 2;
        RecommendCities  recommendCities = new RecommendCities(historyVisits, k);
        recommendCities.topKRecommendationsForUser(3).forEach(System.out::println);
    }
}
