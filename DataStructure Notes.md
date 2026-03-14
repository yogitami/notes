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
