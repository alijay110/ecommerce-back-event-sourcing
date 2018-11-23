#!/bin/bash

echo "deleting existing kafka"
docker kill local_kafka
echo "starting new kafka"
docker run -d --name local_kafka -e SAMPLEDATA=0 -e ADV_HOST=127.0.0.1 -e EULA="https://dl.lenses.stream/d/?id=b7569a80-8417-4311-8ee3-5ec7ed18a845" --rm -p 3030:3030 -p 9092:9092 -p 2181:2181 -p 8088:8088 -p 9081:9081 -p 9082:9082 -p 9084:9084 -p 9085:9085  landoop/kafka-lenses-dev
echo "sleeping 45 secs"
sleep 45
for topic in "${@}"; do
    echo "Creating ${topic}"
 	docker exec local_kafka bin/bash -c " kafka-topics --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --create --topic ${topic}"
	echo "$topic created."
done
start http://localhost:3030




#$ ./restart-kafka.sh orderComponentEvent order brand card queryBrand queryCardsByBrand joinedCardWithOrder buyerOrder sellerOrder
