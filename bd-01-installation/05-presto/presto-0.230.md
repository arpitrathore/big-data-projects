# Steps to install and run presto locally

## 1. Download the presto binary [version 0.230]
Documentation: https://prestodb.io/docs/0.230/installation/deployment.html
<br>

``` bash
~$ pwd
/home/arpit

~$ wget https://repo1.maven.org/maven2/com/facebook/presto/presto-server/0.230/presto-server-0.230.tar.gz
~$ $ tar -xvf presto-server-0.230.tar.gz
~$ rm presto-server-0.230.tar.gz
~$ mv presto-server-0.230/ presto
~$ cd presto
```

## 2. Configure presto
2.1 Create etc and data directory
```bash
~/presto$ mkdir etc
~/presto$ mkdir data
```

2.2 Create etc/node.properties with following content
```.env
~/presto$ vi etc/node.properties

node.environment=local
node.id=ffffffff-ffff-ffff-ffff-ffffffffffff
node.data-dir=/home/arpit/presto/data
```

2.3 Create etc/jvm.config with following content
```.env
~/presto$ vi etc/jvm.config

-server
-Xmx16G
-XX:+UseG1GC
-XX:G1HeapRegionSize=32M
-XX:+UseGCOverheadLimit
-XX:+ExplicitGCInvokesConcurrent
-XX:+HeapDumpOnOutOfMemoryError
-XX:+ExitOnOutOfMemoryError
```

2.4 Create etc/config.properties with following content
```.env
~/presto$ vi etc/config.properties

coordinator=true
node-scheduler.include-coordinator=true
http-server.http.port=8080
query.max-memory=5GB
query.max-memory-per-node=1GB
query.max-total-memory-per-node=2GB
discovery-server.enabled=true
discovery.uri=http://localhost:8080
```

2.5 Create etc/catalog/cassandra.properties for cassandra connector
<br>
Install cassandra server locally if not installed already : [steps](../03-cassandra/cassandra-3.11.5.md)
```.env
~/presto$ mkdir -p etc/catalog
~/presto$ vi etc/catalog/cassandra.properties

connector.name=cassandra
cassandra.contact-points=localhost
cassandra.native-protocol-port=9042
```

## 2. Start presto server on port 8080
```bash
# Blocking call
~/presto$ bin/launcher run

# Verfivication log line-
2020-03-04T07:03:55.037Z        INFO    main    com.facebook.presto.server.PrestoServer ======== SERVER STARTED ========
```

## 3. Download and configure presto-cli
Start new terminal
```bash
~$ wget https://repo1.maven.org/maven2/com/facebook/presto/presto-cli/0.230/presto-cli-0.230-executable.jar
~$ mv presto-cli-0.230-executable.jar presto-cli
~$ chmod +x presto-cli
~$ sudo mv presto-cli /usr/local/bin/

# Verify
~$ which presto-cli
```

## 4. Start presto-cli
```bash
~$ presto-cli --server localhost:8080
```

## 5. Verify presto installation with cassandra catalog
Install cassandra server locally if not installed already : [steps](../03-cassandra/cassandra-3.11.5.md)
     
```bash
presto> show catalogs;
presto> show schemas in cassandra;
presto> show tables in cassandra.spark;
presto> select * from cassandra.spark.test;
```

## 5. Verify presto installation with hive meta-store catalog
Install hive locally if not installed already : [steps](../01-hadoop/hive-2.3.5.md)

5.1 Create etc/catalog/hive.properties for hive connector
```..env
~/presto$ mkdir -p etc/catalog
~/presto$ vi etc/catalog/hive.properties

connector.name=hive-hadoop2
hive.metastore.uri=thrift://localhost:9083
```
5.2 Run step 2 to start presto server and 4 to start presto-cli

5.3 Run following commands to view data from hive table-
```bash
presto> show catalogs;
presto> show schemas in hive;
presto> show tables in hive.testdb;
presto> select * from hive.testdb.employee;
```