package org.example.booking;


/*
data set :
{

	176 : 	[
				{
					"price" : 120,
					"features" : ["breakfast", "refundable"],
					"availbility" : 5
				}
			],
	177 : 	[
				{
					"price" : 130,
					"features" : ["breakfast"],
					"availbility" : 1
				},
				{
					"price" : 140,
					"features" : ["breakfast", "refundable", "wifi"],
					"availbility" : 3
				}
			],
	178 : 	[
				{
					"price" : 130,
					"features" : ["breakfast"],
					"availbility" : 2
				},
				{
					"price" : 140,
					"features" : ["breakfast", "refundable", "wifi"],
					"availbility" : 10
				}
			]
}


Input :
{
	"checkin" : 176,
	"checkout" : 178,
	"features" : ["breakfast"]
	"rooms"    : 1

}

Output :

[
	{
		"price" : 250,
		"features" : ["breakfast"],
		"availbility" : 1
	},
	{
		"price" : 260,
		"features" : ["breakfast", "refundable"],
		"availbility" : 3
	}
]
 */


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

 class Room {
    private int price;
    Set<String> features;
    private int availability;

     public Room(int price, Set<String> features, int availability) {
         this.price = price;
         this.availability = availability;
         this.features = features;
     }

     public int getPrice() {
         return price;
     }

     public void setPrice(int price) {
         this.price = price;
     }

     public Set<String> getFeatures() {
         return features;
     }

     public void setFeatures(Set<String> features) {
         this.features = features;
     }

     public int getAvailability() {
         return availability;
     }

     public void setAvailability(int availability) {
         this.availability = availability;
     }

     @Override
     public String toString() {
         return "Room{" +
                 "price=" + price +
                 ", features=" + features +
                 ", availability=" + availability +
                 '}';
     }
 }

public class HotelBooking {

     public static List<Room> getHotels(int rooms, int checkin, int checkout, Set<String> requiredFeatures, Map<Integer, List<Room>> hotelsAvailabilities){
         List<Room> avaiableRooms = new ArrayList<>();
         dfs(rooms,checkin,checkout,requiredFeatures,hotelsAvailabilities,0,null,Integer.MAX_VALUE,avaiableRooms);
        return avaiableRooms;
     }

     private static void dfs(int requiredRooms,
                            int checkin,
                            int checkout,
                            Set<String> requiredFeatures,
                            Map<Integer, List<Room>> hotelsAvailabilities,
                            int currentPrice,
                            Set<String> currentFeatures,
                            int currentAvailability,
                            List<Room> result){

         if(checkin > checkout) return;

         if(checkin == checkout){
             result.add(new Room(currentPrice,currentFeatures,currentAvailability));
             return;
         }

         List<Room> availableRoomsForTheDay = hotelsAvailabilities.get(checkin);
         if(availableRoomsForTheDay == null) return;

         for(Room room: availableRoomsForTheDay){

             if(room.getAvailability() < requiredRooms) continue;

             // Current day's option must contain all required features
             if(!room.getFeatures().containsAll(requiredFeatures)) continue;

             Set<String> nextFeature;
             if(currentFeatures == null) {
                 // first day
                 nextFeature = new HashSet<>(room.features);
             }
             else {
                 nextFeature = new HashSet<>(currentFeatures);
                 nextFeature.retainAll(room.features);
             }


             int newPrice = currentPrice + room.getPrice();
             // Minimum availability across all chosen days
             int minAvailableRooms = Math.min(room.getAvailability(), currentAvailability);

             dfs(requiredRooms,checkin+1,checkout,requiredFeatures,hotelsAvailabilities,newPrice,nextFeature,minAvailableRooms,result);
         }

     }


    public static  void main(String[] args) {
        Map<Integer, List<Room>> hotelsAvailabilities = new HashMap<>();

        hotelsAvailabilities.put(176, Arrays.asList(
                new Room(120, Set.of("breakfast", "refundable"), 11),
                new Room(200, Set.of("breakfast", "refundable", "wifi"), 18)
        ));

        hotelsAvailabilities.put(177, Arrays.asList(
                new Room(130, Set.of("breakfast"), 1),
                new Room(140, Set.of("breakfast", "refundable", "wifi"), 8)
        ));

        hotelsAvailabilities.put(178, Arrays.asList(
                new Room(130, Set.of("breakfast"), 2),
                new Room(140, Set.of("breakfast", "refundable", "wifi"), 10)
        ));

        Set<String> required = new HashSet<>(Arrays.asList("breakfast"));

        List<Room> answer = getHotels(3, 176, 178, required, hotelsAvailabilities);

        for (Room hotel : answer) {
            System.out.println(hotel);
        }
    }
}
