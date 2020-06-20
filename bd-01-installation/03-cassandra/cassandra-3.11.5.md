# Steps to install and run cassandra locally on port 9042

## 1. Download cassandra binary [version 3.11.5] 

``` bash
~$ pwd
/home/arpit

~$ wget http://apachemirror.wuchna.com/cassandra/3.11.5/apache-cassandra-3.11.5-bin.tar.gz
~$ tar -xvf apache-cassandra-3.11.5-bin.tar.gz
~$ mv apache-cassandra-3.11.5 cassandra
~$ rm apache-cassandra-3.11.5-bin.tar.gz
~$ cd cassandra
```

## 2. Configure cassandra server

Create a data directory for commit log and sstables
``` bash
~/cassandra$ mkdir -p /home/arpit/cassandra/data/commitlog
~/cassandra$ mkdir -p /home/arpit/cassandra/data/sstables
```

Update conf/cassandra.yaml to change commitlog and sstables directory
```.env
~/cassandra$ vi conf/cassandra.yaml
```

Update the following properties
```.env
data_file_directories:
     - /home/arpit/cassandra/data/sstables

commitlog_directory: /home/arpit/cassandra/data/commitlog
```

Verify conf/cassandra.yaml for above changes
``` bash
~/cassandra$ cat conf/cassandra.yaml | grep -A 1 -e commitlog_directory -e data_file_directories
```

## 3. Start cassandra server

Start cassandra server
```bash
# Start cassandra server, blocking call
~/cassandra$ bin/cassandra
```

## 4. Configure and start cqlsh
Open a new terminal to configure cqlsh. Install python, required for cqlsh
```bash
~$ sudo apt install -y python
~$ cd cassandra 
~/cassandra$ bin/cqlsh
```

## 5. Verify cassandra and cqlsh installation
```bash
~/cassandra$ bin/cqlsh

cqlsh> describe keyspaces;
cqlsh> CREATE KEYSPACE spark WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 1};
cqlsh> use spark;
cqlsh:spark> CREATE TABLE test(id int PRIMARY KEY, name text);
cqlsh:spark> describe tables;
cqlsh:spark> insert into test(id, name) values(1, 'arpit');
cqlsh:spark> select * from test;
cqlsh:spark> drop table test;
```