# Steps to install and run hive locally [version 3.1.0]
 
Prerequisite : Haoop installed locally [steps](hadoop-3.1.1.md)
## 1. Download hive binary [version 3.1.0]
``` bash
~$ pwd
/home/arpit

~$ wget https://archive.apache.org/dist/hive/hive-3.1.0/apache-hive-3.1.0-bin.tar.gz
~$ tar -xvf apache-hive-3.1.0-bin.tar.gz
~$ mv apache-hive-3.1.0-bin hive/
~$ rm apache-hive-3.1.0-bin.tar.gz
``` 

## 2. # Remove slf4j jar due to conflict  
``` bash
$ mv hive/lib/log4j-slf4j-impl-2.10.0.jar hive/lib/log4j-slf4j-impl-2.10.0.jar.bak
```

## 3. Set hive environment variables in .bashrc file and source it
```.env
~$ vi .bashrc

export HIVE_HOME=/home/arpit/hive
export PATH=$PATH:$HIVE_HOME/bin

~$ hive --version #(To verify)
```

## 4. Create following directories with permission in HDFS 
``` bash
~$ hdfs dfs -mkdir -p /user/hive/warehouse
~$ hdfs dfs -mkdir -p /tmp
~$ hdfs dfs -chmod g+w /user/hive/warehouse
~$ hdfs dfs -chmod g+w /tmp
~$ hdfs dfs -chmod -R 1777 /user/hive/warehouse
```

## 5. Create hive-env.sh with following environment variables
```.env
~$ vi hive/conf/hive-env.sh

export HADOOP_HOME=/home/arpit/hadoop
export HADOOP_HEAPSIZE=512
export HIVE_CONF_DIR=/home/arpit/hive/conf
```

## 6. Create hive-site.xml with following content
```.env
~$ vi hive/conf/hive-site.xml

<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<configuration>
    <property>
        <name>javax.jdo.option.ConnectionURL</name>
        <value>jdbc:derby:;databaseName=/home/arpit/hive/metastore_db;create=true</value>
        <description>JDBC connect string for a JDBC metastore.</description>
    </property>
    <property>
        <name>hive.metastore.warehouse.dir</name>
        <value>/user/hive/warehouse</value>
        <description>location of default database for the warehouse</description>
    </property>
    <property>
        <name>hive.metastore.uris</name>
        <value>thrift://localhost:9083</value>
        <description>Thrift URI for the remote metastore.</description>
    </property>
    <property>
        <name>javax.jdo.option.ConnectionDriverName</name>
        <value>org.apache.derby.jdbc.EmbeddedDriver</value>
        <description>Driver class name for a JDBC metastore</description>
    </property>
    <property>
        <name>javax.jdo.PersistenceManagerFactoryClass</name>
        <value>org.datanucleus.api.jdo.JDOPersistenceManagerFactory</value>
        <description>class implementing the jdo persistence</description>
    </property>
    <property>
        <name>hive.server2.enable.doAs</name>
        <value>false</value>
    </property>
</configuration>
```

## 7. Create a database schema for Hive to work with using schematool
``` bash
~$ schematool -initSchema -dbType derby
```


## 8. Start the Hive Metastore server with the following command (Blocking call)
``` bash
~$ hive --service metastore
```

## 9. Sample hive commands to verify installation. Start hive in new shell
```bash
~$ hive

hive> create database testdb;
hive> show databases;
hive> use testdb;
hive> create table employee(id int, name string);
hive> show tables in testdb;
hive> insert into employee values(1, 'Arpit');
hive> select * from employee;
```

## 10. [OPTIONAL] Configure postgresql instead of derby db

**10.1 Install postgresql**
```bash
~$ sudo apt-get install postgresql
```

**10.2 Setup password for user postgres (password- postgres)**
```bash
~$ sudo passwd postgres
```

**10.3 Start postgres service**
```bash
$ sudo service postgresql start
```

**10.4 Download postgres jdbc jar and move it to hive lib dir**
```bash
~$ sudo apt-get install libpostgresql-jdbc-java
~$ cp /usr/share/java/postgresql-jdbc4.jar $HIVE_HOME/lib

~$ ls -l $HIVE_HOME/lib | grep postgre #(To verify)
```

**10.5 Connect to postgresql, create the Metastore database and user accounts.**
```bash
~$ sudo -u postgres psql

postgres=# CREATE USER hiveuser WITH PASSWORD 'mypassword';
postgres=# CREATE DATABASE metastore;

# Exit from postgres terminal using Cntl + d and run following command from bash to verify
~$ psql -h localhost -U hiveuser -d metastore

```

**10.6 Update hive-site.xml**
```.env
~$ vi hive/conf/hive-site.xml

<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<configuration>
    <property>
        <name>javax.jdo.option.ConnectionURL</name>
        <value>jdbc:postgresql://localhost/metastore</value>
        <description>JDBC connect string for a JDBC metastore.</description>
    </property>
    <property>
        <name>hive.metastore.warehouse.dir</name>
        <value>/user/hive/warehouse</value>
        <description>location of default database for the warehouse</description>
    </property>
    <property>
        <name>hive.metastore.uris</name>
        <value>thrift://localhost:9083</value>
        <description>Thrift URI for the remote metastore.</description>
    </property>
    <property>
        <name>javax.jdo.option.ConnectionDriverName</name>
        <value>org.postgresql.Driver</value>
        <description>Driver class name for a JDBC metastore</description>
    </property>
    <property>
        <name>javax.jdo.option.ConnectionUserName</name>
        <value>hiveuser</value>
    </property>
    <property>
        <name>javax.jdo.option.ConnectionPassword</name>
        <value>mypassword</value>
    </property>
    <property>
        <name>javax.jdo.PersistenceManagerFactoryClass</name>
        <value>org.datanucleus.api.jdo.JDOPersistenceManagerFactory</value>
        <description>class implementing the jdo persistence</description>
    </property>
    <property>
        <name>hive.server2.enable.doAs</name>
        <value>false</value>
    </property>
</configuration>
```

**10.7 Run schemaTool to create the initial DB structure.**
```bash
~$ schematool -dbType postgres -initSchema
```
