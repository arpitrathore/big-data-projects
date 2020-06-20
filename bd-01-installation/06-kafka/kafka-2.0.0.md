# Steps to install and run kafka/zookeeper locally on port 9092/2181

## 1. Download kafka binary [version 2.0.0] 

``` bash
~$ pwd
/home/arpit

~$ wget https://archive.apache.org/dist/kafka/2.0.0/kafka_2.12-2.0.0.tgz
~$ tar -xvf kafka_2.12-2.0.0.tgz
~$ mv kafka_2.12-2.0.0 kafka
~$ rm kafka_2.12-2.0.0.tgz
~$ cd kafka
```

## 2. Configure kafka/zookeeper

Create a data directory for kafka and zookeeper
``` bash
~/kafka$ mkdir -p /home/arpit/kafka/data/zookeeper
~/kafka$ mkdir -p /home/arpit/kafka/data/kafka
```

Update config/zookeeper.properties to change zookeeper data directory
```.env
~/kafka$ vi config/zookeeper.properties

# Update the following property
dataDir=/home/arpit/kafka/data/zookeeper

# Verify config/zookeeper.properties for above changes
~/kafka$ cat config/zookeeper.properties | grep dataDir
```


Update config/server.properties to change kafka data directory
```.env
~/kafka$ vi config/server.properties

# Update the following property
log.dirs=/home/arpit/kafka/data/kafka

# Verify config/server.properties for above changes
~/kafka$ cat config/server.properties | grep log.dirs
```

## 3. Start zookeeper server on port 2181
```bash
# Start zookeeper server. Blocking call
~/kafka$ bin/zookeeper-server-start.sh config/zookeeper.properties
```

## 4. Start kafka server on port 9092
Open a new terminal
```bash
~$ cd kafka
# Start kafka server. Blocking call
~/kafka$ bin/kafka-server-start.sh config/server.properties
```

## 4. Verify kafka installation by creating and viewing new topic
Open a new terminal
```bash
~$ cd kafka

# Create new topic
~/kafka$ bin/kafka-topics.sh --zookeeper localhost:2181 --topic test-topic --create --partitions 3 --replication-factor 1

# List topics
~/kafka$ bin/kafka-topics.sh --zookeeper localhost:2181 --list
```