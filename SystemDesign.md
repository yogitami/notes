# System Design


### Componenets

1. Databases
   - RDBMS : relational databases represent and store data in table and rows. You can perform join operations using SQL across different tables
   - NoSQL : These databases are grouped in 4 categories : key-value stores, graph stores, column stores, document stores.
     - Non-relational databases might be a right choice if :
       - Your application requires super low latency.
       - Your data is unstructured.
       - You only need to serialize and deserialize data (JSON,XML,YAML,etc.)
       - You need yo store a massive amount of data.

2. Scaling
   - Vertical :
     - Vertical scaling has a hard limit. It is impossible to add unlimited CPU and memory to a single server.
     - Vertical scaling doesn't have failover and redundancy. If one server goes down, the website/app does down with it completely.
   - Horizontal

3. Load Balancer


### Scale from zero to millions of users

#### Basics
1. Users access websites through domain names.
2. DNS (Domain Name System) is a paid service provided by 3rd party and not hosted by our servers.
3. IP address is returned to the browser or mobile app.
4. Once IP address is obtained, HTTP requests are sent directly to your web server
5. The web server returns HTML pages or Json response for rendering.
