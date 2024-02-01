# MessageSimulator
This is a learning project for message oriented programming.
It purpose is education and the goal is a prototype that processes orders of an e-commerce store.

## Running the application

### Prerequisites
To run this prototype following software needs to be installed:
- Maven
- Java
- Docker

### Usage
1. Run ```mvn clean package && docker compose up -d```
2. Start `http://localhost:8081/start`
4. Check the topics and messages via kafka-ui `http://localhost:8080`
3. Increase the message generation `http://localhost:8081/interval/200`
4. Stop the madness with `http://localhost:8081/stop`

#### Debugging
If the services don't appear with ```docker container ls``` or http://localhost:8081/start displays an HTTP-Error.
Retry ```docker compose up -d```
## Technical Documentation
### Compare Message Broker

Apache ActiveMQ is a great workhorse full of features and nice stuff.
It's not the fastest MQ software around but fast enough for most use
cases. Among features are flexible clustring, fail-over, integrations
with different application servers, security etc.

Apache Apollo is an attempt to write a new core for ActiveMQ to cope
with a large amount of clients and messages. It does not have all nice
and convenient feature of ActiveMQ but scales a lot better. Apache
Apollo is a really fast MQ implementation when you give it a large
multi-core server and thousands of concurrent connections. It has a
nice, simple UI, but is not a "one-size-fits-all" solution.

It seems that there is an attempt ongoing to merge a number of
ActiveMQ features with HornetQ under the name ActiveMQ Artemis.
HornetQ has JMS2.0 support, so my humble guess is that it's likely to
appear in ActiveMQ 6.x.

Kafka is a different beast. It's a very simple message broker intended
to scale persistent publish subscribe (topics) as fast as possible
over multiple servers. For small-medium sized deployments, Kafka is
probably not the best option. It also has it's way to do things to
achieve the high throughput, so you have to trade a lot in terms of
flexibility to get high distributed throughput. If you are new to the
area of MQ and brokers, I guess Kafka is overkill. On the other hand -
if you have a decent sized server cluster and wonder how to push as
many messages as possible through it - give Kafka a spin!

### Messaging Protocols and API
- AMQP = Advanced Message Queuing Protocol, application layer protocol,  binary based
- STOMP / TTMP = Streaming Text Oriented Messaging Protocol, text-based protocol
- MQTT = Message Queuing Telemetry Transport, network protocol
- JMS = Jackarta Messaging, Java API to support communication models: point-to-point and publish-and-subscribe.
- WebSockets = HTTP-Connection can be converted to WebSocket, making the communication bi-directional, application layer protocol

### Kafka
- zookeeper = coordination service for distributed applications
- KRaft =  Apache Kafka Raft (KRaft) is the consensus protocol that was introduced in KIP-500 to remove Apache Kafka’s dependency on ZooKeeper for metadata management.
- Apache Kafka = is a distributed event store and stream-processing platform.

We decided to use Kafka in our prototype because it has a spring integration and a well documented API.

Links:
- [Kafka Website] (https://kafka.apache.org/)
- [Spring Integration] (https://docs.spring.io/spring-kafka/reference/quick-tour.html)
- [Java API] (https://kafka.apache.org/32/javadoc/index.html)
- [Spring API] (https://docs.spring.io/spring-kafka/api/org/springframework/kafka/core/package-summary.html)
- [Compatibilty Matrix] (https://cwiki.apache.org/confluence/display/KAFKA/Compatibility+Matrix)

### Coordinated Transactions with the SAGA-Pattern
- ACID transactions = Atomicity, consistency, isolation, durability
- BASE transactions = Basic Availability, Soft state, Eventual consistency

SAGA are used to realize BASE Transactions between multiple services by defining a compensating action, which restores the state in case of an error.

Links:
- [Pattern: Saga] (https://microservices.io/patterns/data/saga.html)
- [Example using Choreography] (https://docs.axoniq.io/reference-guide/v/3.1/part-ii-domain-logic/sagas)
- [Example using Orchestration] (https://camel.apache.org/components/4.0.x/eips/saga-eip.html)

### Tracing of Messages

Enrich messages with a trace id by which log messages can be queried and analysed.

Links:
- [Pattern: Distributed Tracing] (https://microservices.io/patterns/observability/distributed-tracing.html)
- [Tracing System: Zipkin] (https://zipkin.io/)

### Message oriented programming with PHP

Because the current e-commerce is written in PHP, a thought was to use a PHP-messaging-framework.

Links:
- [Ecotone] (https://github.com/ecotoneframework/ecotone)

# Business Documentation
## Message Orientierte Logistik

Im Folgenden soll der Ablauf und die einzelnen Stationen der Logistik der haar-shop.ch aufgelistet werden. Die einzelnen Komponenten werden vereinfacht abgebildet.

### Bestellungen generieren
Ein Generator erzeugt konstant Bestellungen.
Eine Bestellung besteht aus folgenden Eigenschaften:
- Bestellnummer
- Kunde
    - Kundennummer
    - PLZ
    - Geschlecht
    - Kundenstatus
        - Neukunde oder Bestandeskunde
- Positionen
    - Produktnummer
    - Anzahl
- Preis
- Bezahlmethode
    - Kreditkarte, Rechnung oder Twint
- Versandart
    - A-Post oder B-Post
- Teillieferung

Eine generierte Bestellung hat den Status **importiert**.

### Bestellungen prüfen
Importierte Bestellungen werden dann auf Sonderheiten geprüft.

#### Export
Bestellungen aus einem bestimmten Nummernkreis sollen in einer separaten Tabelle erfasst und für den weiteren Versand gesperrt werden.
> Hintergrund: haar-shop.ch liefert grundsätzlich nur in die Schweiz. Jedoch gibt es in der Schweiz mit der Gemeinde Samnaun ein Zollausschlussgebiet. Bestellungen aus Samnaun müssen also als Exporte behandelt werden und entsprechende Dokumente beigelegt werden.

#### Betrugsversuch
Es kommt immer wieder vor, dass sich Kunden als Betrüger entpuppen. Die folgenden Kriterien deuten auf Betrug hin und die Bestellungen werden dann entsprechend markiert und gegen Signatur versendet.
- Kundentyp: Neukunde
- Bezahlmethode: Rechnung
- Preis: >= CHF 100

### Kommissionslisten erstellen
Die Produkte aus Bestellungen, welche komplett kommissionierbar sind, sollen in Kommissionslisten erfasst werden.

### Kommissionierung simulieren
Die Abarbeitung der Kommissionierungslisten soll simuliert werden. Wurden alle Bestellpositionen einer sich auf einer Kommissionierungsliste befindenden Bestellung kommissioniert, gilt die Bestellung als **verpackungsbereit**.

### Teillieferungen aktivieren
haar-shop.ch ist bestrebt, alle Bestellungen so schnell wie möglich zum/zur Kunden/Kundin zu bringen. Gleichzeitig soll eine Bestellung möglichst komplett ausgeliefert werden. Sollte für eine Bestellung nicht alle Bestellpositionen verfügbar sein, wird die Verarbeitung so lange wie möglich herausgezögert, in der Hoffnung, dass die fehlenden Produkte im Verlauf des Tages noch angeliefert werden. Ab 17:00 wird auf allen pendenten Bestellungen die Teillieferung aktiviert.

### Lagerbestände führen
Normalerweise werden die Lagerbestände von dem Logistiksystem geführt.
