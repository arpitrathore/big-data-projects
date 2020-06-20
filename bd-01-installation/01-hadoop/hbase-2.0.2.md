# Steps to install and run hbase locally in pseudo distributed mode [version 2.0.2]
 
Prerequisite : Hadoop installed locally [steps](hadoop-3.1.1.md)
## 1. Download hive binary [version 2.0.2]
``` bash
~$ pwd
/home/arpit

~$ wget https://archive.apache.org/dist/hbase/2.0.2/hbase-2.0.2-bin.tar.gz
~$ tar -xvf hbase-2.0.2-bin.tar.gz
~$ mv hbase-2.0.2 hbase/
~$ rm hbase-2.0.2-bin.tar.gz
``` 

## 2. Set hive environment variables in .bashrc file and source it
```.env
~$ vi .bashrc

export HBASE_HOME=/home/arpit/hbase
export PATH=$PATH:$HBASE_HOME/bin

# Source .bashrc and run which command to verify
$ source .bashrc
$ which hbase
/home/arpit/hbase/bin/hbase
```

## 3. Update hbase-env.sh (Update the value of JAVA_HOME)
```.env
~$ vi hbase/conf/hbase-env.sh

export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
```

## 4. Create hbase zookeeper data dir
``` bash
~$ mkdir -p /home/arpit/hbase/data/zookeeper
```

## 5. Replace entire hbase-site.xml file entirely with following content
```.env
~$ vi hbase/conf/hbase-site.xml

<?xml version="1.0" encoding="UTF-8"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<configuration>
   <property>
      <name>hbase.zookeeper.property.dataDir</name>
      <value>/home/arpit/hbase/data/zookeeper</value>
   </property>

   <property>
      <name>hbase.zookeeper.quorum</name>
      <value>localhost</value>
   </property>

   <property>
      <name>hbase.zookeeper.quorum.port</name>
      <value>3181</value>
   </property>   

   <property>
      <name>hbase.rootdir</name>
      <value>hdfs://localhost:9000/hbase</value>
   </property>
	
   <property>
     <name>hbase.cluster.distributed</name>
     <value>true</value>
   </property>

    <property>
      <name>hbase.unsafe.stream.capability.enforce</name>
      <value>false</value>
    </property>
</configuration>
```

## 6. Update your hosts file with localhost and your hostname
```bash
# Windows/WSL : C:\Windows\System32\drivers\etc\hosts
# Linux : /etc/hosts

127.0.0.1	localhost
127.0.0.1	myhostname
```

## 7. Verify hdfs is running
``` bash
$ jps
1009 Jps
371 NameNode
566 DataNode
863 SecondaryNameNode
```

## 8. Start Hbase
```bash
~$ start-hbase.sh 

# Verify hbase by running jps command
~$ jps
1971 Jps
371 NameNode
566 DataNode
863 SecondaryNameNode
1399 HQuorumPeer
1575 HRegionServer

# Verify hbase by checking hbase data in hdfs
$ hdfs dfs -ls /hbase
Found 12 items
drwxr-xr-x   - arpit supergroup          0 2020-04-05 03:35 /hbase/.hbck
drwxr-xr-x   - arpit supergroup          0 2020-04-05 03:35 /hbase/.tmp
drwxr-xr-x   - arpit supergroup          0 2020-04-05 03:35 /hbase/MasterProcWALs
drwxr-xr-x   - arpit supergroup          0 2020-04-05 03:35 /hbase/WALs
drwxr-xr-x   - arpit supergroup          0 2020-04-05 03:35 /hbase/archive
drwxr-xr-x   - arpit supergroup          0 2020-04-05 03:35 /hbase/corrupt
drwxr-xr-x   - arpit supergroup          0 2020-04-05 03:35 /hbase/data
-rw-r--r--   1 arpit supergroup         42 2020-04-05 03:35 /hbase/hbase.id
-rw-r--r--   1 arpit supergroup          7 2020-04-05 03:35 /hbase/hbase.version
drwxr-xr-x   - arpit supergroup          0 2020-04-05 03:35 /hbase/mobdir
drwxr-xr-x   - arpit supergroup          0 2020-04-05 03:35 /hbase/oldWALs
drwx--x--x   - arpit supergroup          0 2020-04-05 03:35 /hbase/staging
```
## 9. Start Hbase shell
```bash
$ hbase shell


```



