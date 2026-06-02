### Sort the entries in hashmap (freqMap)
- Create an arrayList
  > List<Map.Entry<Integer,Integer>> entries = new ArrayList<>(freqMap.entrySet());
    entries.sort( (a,b) -> b.getValue() - a.getValue()); // by descending order
- First k elements
  > int[] result = new int[k];
    for(int i = 0; i < k; i++){
        result[i] = entries.get(i).getKey();
    }


### LinkedHashMap

  - Maintain insertion order
  - Access order LinkedHashMap
      - LinkedHashMap<Integer, String> map = new LinkedHashMap<>(16, .75f, true); --> It maintains the order in which elements were accessed.
        If the boolean value is true then if an element is accessed using(get) then it would be added to the back of the linked list (as a new entry)(Useful for LRU cache prolems)
      - LinkedHashMap also provides a mechanism for maintaining a fixed number of mappings and to keep dropping off the oldest entries in case a new one needs to be added.
      - The removeEldestEntry method may be overridden to enforce this policy for removing stale mappings automatically.
        
        ```
        public class MyLinkedHashMap<K, V> extends LinkedHashMap<K, V> {

            private static final int MAX_ENTRIES = 5;
        
            public MyLinkedHashMap(
              int initialCapacity, float loadFactor, boolean accessOrder) {
                super(initialCapacity, loadFactor, accessOrder);
            }
        
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > MAX_ENTRIES;
            }
      
        }
        ```

### Floyd Warshall Algorithm

1. Given a matrix dist[][] of size n x n, where dist[i][j] represents the weight of the edge from node i to node j.
1. If there is no direct edge, dist[i][j] is set to INF (a large value i.e., 108).
1. The diagonal entries dist[i][i] are 0, since the distance from a node to itself is zero.

### Convert from array to List
1. if we have int[] array and we want to convert into List<Integer> : the way to do is
   
   > Arrays.stream(nums).boxed().collect(Collectors.toList());
