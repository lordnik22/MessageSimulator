#!/bin/sh

mvn package -f "./order-models/pom.xml"

yes | cp order-models/target/kafka-order-models-1.0.0-SNAPSHOT.jar order-creation-service/libs
yes | cp order-models/target/kafka-order-models-1.0.0-SNAPSHOT.jar order-export-marker-service/libs

mvn package -f "./order-creation-service/pom.xml"
mvn package -f "./order-export-marker-service/pom.xml"
