# Bux Flow

1. [CQRS](#CQRS)
1. [KAFKA](#Kafka)
1. [BUX](#Bux)

---


<a name = 'Kafka' ></a>
## KAFKA : 




---


<a name = 'CQRS' ></a>
## CQRS :
Command Query Responsibility Segregation

CQRS is a concept in which the data model used for reading is separated from the data model that is used to update information.
Here, command is sent to change the state in the system & this state is changed by using Domain Events. Commands generate Domain Events.

- Commands change state, queries don't.
- **Command** describes what you want to happen.
- **Domain Events** describe what happened in the system. They are immutable and ordered in time. They are always about an entity (or aggregate in DDD) in the system.

    UI --> Command Service -(data validation and logic)--> Command Model --(updating data)-->  DB
    UI <-- Query Service <--(Creating presentation for User)-- Query Model <--(reading data)-- DB


|  Process flow   |
| --- |
| <img width="100%" src="/Users/yogitamittal/Downloads/Flow.drawio.png"> |
|  |


*Query Model*
: model that is used to read data from database

*Command Model*
: update data from database

**Query Service**
: Service that reads data from database to be presented to users

1. Use domain events to build up an internal model
1. Can combine multiple domain events.
1. Query Model can be stored in any medium : in-memory, elastic search, actor system.
1. Eventual consistency.
1. **Must be able to rebuild it's model at all times.**

*Command Service*
: 
1. Service that receives data from users and performs update in the database
1. It is the main place where business logic is implemented. It’s purpose is to receive a command, perform required validation and execute actions needed for that command. (UseCase). Result of command will be an event in *-DomainEvents topic .i.e. for every state change a domain event is emitted.
1. We can also use command service to replay domain events to (re)build internal state.
1. One command service always treats one entity.
1. It can manage multiple instances of the same entity.

##### Benefits

1. Improved Security because update operation can be validated separately.
1. Improved Scalability because query and command services can be scaled according to needs.
1. Improved Performance because different database instances (master/replica) can be used to read and to write.

##### Disadvantages

1. Code complexity
2. Data Consistency


---

<a name = 'Bux' ></a>
### Bux Implementation

##### Kafka

1. Used to store all domain evenets, command & command response.
1. Kafka is used as a messaging and a storage layer, we can achieve atomic transaction.
1. It is based on commit log.
1. Processes which are called as producers writes key-value messages to a disk as commit logs called "topics".
1. These topics are are ordered and can be partitioned.
1. Partitions can be distributed across different instances for redundancy and resiliency while retaining both high-throughput and scalability.
1. Exactly once semantic in the context of Kafka stream processing mean's that we can guaranty all messages written to a particular topic by producers are going to be read only once by each consumer listening to that topic. (this is helpful when managing financial transcations : debit/credit )
1. Kafka manages this by using idempotency while writing messages and atomicity while reading and writing messages.
1. In Kafka the concept of transactions enforce exactly-once processing by enabling atomic writes to multiple Kafka topics and partitions.


##### Command & Command Response, Domain Events, State, State Repository
:  
1. Commands are data objects used to trigger an operation execution. It is used to change the state of an entity (Registered inside command registry).
1. Command and command response are syncronous process
1. Dommain events are emitted asynchronously when a command is executed, these are the data objects that result in application state changes and are stored in the event store.
1. State is a POJO objects that holds application states for each entity (State Repository).

##### UseCase and State Mutators
: 
1. In use case definition we handle and validate commands and emit domain events.
1. StateMutator handles the domain event to apply required changes to application states.
1. The state that is returned by State Mutator is then persisted using State repository.
1. There can be two cases : Domain Driven useCase and Command Driven useCase


##### Command Service
: 
1. Keep the state of the entity they are operating on and validate commands based on it.
1. Command services are the main place where we implement our business logic, it is important for them to be strongly consistent we achieve this in the framework by making use of Kafka's exactly once semantic and transaction API.
1. **Strongly Consistent :**  The framework grantees strong consistency for each command until state change, by using Kafka's only once semantic and transnational API, an event is going to be emitted and processed in the same transaction that the command opened so if any thing goes wrong at any point all of the relate messages, command and domain event and state will be rolled back.

##### State
: 
1. Data Objects
1. States are changed by handling domain events and persisted using implementations of StateRepository interface.
1. TopicBackedStateRepository, in this implementation we use a compacted Kafka topic to keep track of the latest state of each entity (In compacted Kafka topics only the latest version of a message with the same key is kept and the old messages with the same key is remove from the end of the log periodically). topic backed implementation uses Kafka as storage so it also provides partitioning and hence scalability to services that use this implementation.
1. InMemoryStateRepository this implementation keeps all of the states in memory, this implementation is also scalable but it requires the replay of all events at the start of the service and it is more recommended for services with less domain event load.

##### Aggregators
: Part of query service, is used to read state from kafka event and store it in DB.

#### Note :  UseCase, State Mutators, StateRepository and Domain Event Processor needs to be configured as a spring bean (@Component).

##### Event Sourcing

Application state is updated from sequence of events and each event represents changes in data. This allows to see history of the changes and order in which those changes were made. It also allows to restore the state of data at any given moment.

##### Domain Events
:
1. DomainEvents are the messages that we persist in our event store.
1. These events are stored as logs in a Kafka topics.

##### Domain Event Processor
: This component is usually used in query services to process domain events. using this processor we can build a state and store it in repository to be queried by clients later (aggregate on projection and persist in repository).


- The related command is written to Kafka and is read by the use case which handles it, use case writes the event to Kafka, which in turn is read by state mutator and results in the change of state.


---
#### Behind the scene

1. Start kafka transaction.
1. For each consumer record :
    1. Deseralize the command/domain event.
    1. Handle command/domain event (usecase).
    1. Serialize the domain event.
    1. Write the domain event to domain event topic.
    1. update internal state (state mutators/creators).
    1. Serialize the state.
    1. Write state to state repository.
    1. Async a command response would already have been sent
1. Commit the kafka transaction.

---

#### Deep Understanding of Flow Using Kafka topics.

1. Commad Service client uses a kafka client to write to a kafka topic and then waits for the response from another topic (Command Service).
1. Each command service has its own Kafka topic for commands.
1. In the command service, a processing thread is used to pull commands from Kafka topics. The command and payload both are deseralized and if there is a usecase to process the command, it is passed to that to be processed.
1. First in the use case we validate the commands with latest state of the entity.
1. After validation each command can result in one or multiple domain events that is written to a separate topic.
1. Each command service has a separate kafka topic for its events (Domain Events).
1. The use case handle method returns the response that is written to the command response topic, each client has it's own command response topic.
1. CompletionStage that was returned from CommanServiceClient send method in step one and was waiting asynchronously for the response is completed after the response is read.

- One thing to keep in mind is in usecase the event is emitted to a kafka topic and that event is read again from the kafka topic in the state mutator, where the actual changes to the state will happen. Other services can also read the event from the kafka topic at that time but it is not guranteed that the data will be consistent since the actual state is changed in the mutator.

Here are some of the processors in the framework :

* UseCaseProcessor: This is the processor that reads the commands from their topics and if there is a use case registered for that commands it will call that use case handle method.
* KafkaDomainEventListener: This class read's domain events from their topics and calls the corresponding DomainEventProcessor process method.

Since we only use one processing thread for all processors we can guaranty that all of the commands and events in a certain topic are processed in the same order that they are written

|  Kafka flow   |
| --- |
| <img width="100%" src="/Users/yogitamittal/Downloads/Kafka.drawio.png"> |
|  |

---

#### What I worked on

##### Broker Backoffice

It is the consumer.
For Producer events, it contacts the BAC.

- Processor : has an aggregator
- Aggregator : aggregator is used to do operations in database based on the events consumed from kafka


##### Broker Accounts Command

It has the producer events.

- Commands & Command Response : Received from Kafka
- Use case :
    - Create an event and send it to the Consumer to consume 
- Mutator : 
    - It is responsible for changing the state so that updated state can be sent to kafka. Based on some event
    - They are immutable.
    - It is called from inside the 


##### Domain Event Regsitry

Registry to hold the definitions.



##### How the flow went

(Writing)
Controller : payload which has updated detials of the user. 
It sends a command to submit a KYC update proposal request. 

It goes to BAC.
Then in BAC we have a usecase to handle that command.
In use case : we create an event like in my case : BrokerAccountKycUpdateProposedEventV5 that has the details we wish to update (from the command), then we send this event to the consumer to accept the event (it updates the state of the broker account) & after this we create a PersonalDetailsChangedEventV3 event. 
And then again send the event to the consumer to accept.

(Reading)
Controller 

Before it in the background we have a processor that consumes the event generated by Kafka.
And in the processor there is an aggregator which saves the data in the database (state in the db)

---


