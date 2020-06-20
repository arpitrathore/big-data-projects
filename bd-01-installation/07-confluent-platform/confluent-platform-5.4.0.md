# Steps to install and run confluent-platform locally

## 1. Download the confluent-platform binary [version 5.4.0]
Documentation: https://docs.confluent.io/current/installation/installing_cp/zip-tar.html
<br>

``` bash
~$ pwd
/home/arpit

~$ wget http://packages.confluent.io/archive/5.4/confluent-5.4.0-2.12.tar.gz
~$ tar -xvf confluent-5.4.0-2.12.tar.gz
~$ rm confluent-5.4.0-2.12.tar.gz
~$ mv confluent-5.4.0/ confluent
~$ cd confluent
```

## 2. Configure kafka/zookeeper

Create a data directory for kafka and zookeeper
``` bash
~/kafka$ mkdir -p /home/arpit/confluent/data/zookeeper
~/kafka$ mkdir -p /home/arpit/confluent/data/kafka
```

Update etc/kafka/zookeeper.properties to change zookeeper data directory
```.env
~/confluent$ vi etc/kafka/zookeeper.properties

# Update the following property
dataDir=/home/arpit/confluent/data/zookeeper

# Verify etc/kafka/zookeeper.properties for above changes
~/confluent$ cat etc/kafka/zookeeper.properties | grep dataDir
```


Update etc/kafka/server.properties to change kafka data directory
```.env
~/confluent$ vi etc/kafka/server.properties

# Update the following property
log.dirs=/home/arpit/confluent/data/kafka

# Verify etc/kafka/server.properties for above changes
~/confluent$ cat etc/kafka/server.properties | grep log.dirs
```

## 3. Start zookeeper server on port 2181
```bash
# Start zookeeper server. Blocking call
~/confluent$ bin/zookeeper-server-start etc/kafka/zookeeper.properties
```

## 4. Start kafka server on port 9092
Open a new terminal
```bash
~$ cd confluent

# Start kafka server. Blocking call
~/confluent$ bin/kafka-server-start etc/kafka/server.properties
```

## 5. Start schema registry on port 8081
Open a new terminal
```bash
~$ cd confluent

# Start schema registry server. Blocking call
~/confluent$ bin/schema-registry-start etc/schema-registry/schema-registry.properties
```

## 6. Verify kafka installation by creating and viewing new topic
Open a new terminal
```bash
~$ cd confluent

# Create new topic
~/confluent$ bin/kafka-topics --zookeeper localhost:2181 --topic test-topic --create --partitions 3 --replication-factor 1

# List topics
~/confluent$ bin/kafka-topics --zookeeper localhost:2181 --list
```

## 7. Verify schema-registry installation by visiting following url
```bash
~/confluent$ curl -XGET http://localhost:8081
```