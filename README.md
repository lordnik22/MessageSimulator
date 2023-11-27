# MessageSimulator
We try to learn hybride, message-orientierte and reactive and the effects of it 


## Running the application locally

Open sample01 with eclipse or intellij. See pom.xml for java
requirements. Make a maven reload.


Steps
1. docker-compose up
2. Start sample-application within intellij


## Documentation

https://drive.google.com/drive/folders/1E3qmszlbwIsQAW5mp99LMlFbxcFYdoH-

## In beginning there was chaos
```text
||||
||||
||||
\||/
 \/
```

- zookeeper = coordination service for distributed applications
- KRaft =  Apache Kafka Raft (KRaft) is the consensus protocol that was introduced in KIP-500 to remove Apache Kafka’s dependency on ZooKeeper for metadata management. 

https://cwiki.apache.org/confluence/display/KAFKA/Compatibility+Matrix

https://docs.spring.io/spring-kafka/reference/quick-tour.html

https://docs.spring.io/spring-kafka/reference/kafka/connecting.html

https://docs.confluent.io/home/overview.html

https://docs.spring.io/spring-kafka/api/org/springframework/kafka/core/package-summary.html

https://kafka.apache.org/quickstart
```bash
docker-compose up -d
docker pull rabbitmq
docker pull apache/activemq-artemis

```

## Microservices for the Win
### Tracing of Messages

https://microservices.io/patterns/observability/distributed-tracing.html

https://zipkin.io/

### Saga pattern for transaction spanning multiple services


#### Chereography
https://docs.axoniq.io/reference-guide/v/3.1/part-ii-domain-logic/sagas

#### Orchestration

https://camel.apache.org/components/4.0.x/eips/saga-eip.html
## Compare API specs/protocols

https://docs.spring.io/spring-boot/docs/current/reference/html/messaging.html#messaging.amqp

- https://www.oracle.com/java/technologies/java-message-service.html

Java Message Service (JMS) vs Advanced Message Queuing Protocol (AMQP)

## Compare Message Broker

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


### What about RabbitMQ?

## Apache Arrow

https://www.dremio.com/open-source/apache-arrow/

## Kafka
docker pull bitnami/kafka



https://kafka.apache.org/
https://kafka.apache.org/32/javadoc/index.html
https://hub.docker.com/r/wurstmeister/kafka/


## Message oriented programming with PHP

Framework welches entsprechende Strukturen anbietet:
- https://github.com/ecotoneframework/ecotone

## Kafka ui?

  kafka-ui:
    container_name: kafka-ui
    image: provectuslabs/kafka-ui:latest
    ports:
      - 8080:29093
    environment:
      DYNAMIC_CONFIG_ENABLED: 'true'
    volumes:
      - ~/kui/config.yml:/etc/kafkaui/dynamic_config.yaml
