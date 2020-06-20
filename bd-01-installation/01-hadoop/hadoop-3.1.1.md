# Steps to install and run hadoop locally [version 2.8.5]
 

## 1. Setup password less ssh to localhost
``` bash
~$ pwd
/home/arpit

$ sudo apt install openssh-server openssh-client -y
$ ssh-keygen -t rsa
$ cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
```

## 2. Install JDK 8
```.env
~$ sudo apt install openjdk-8-jdk -y
~$ java -version (# To verify)
```

## 3. Download, install and configure hadoop [version 3.1.1]
```bash
~$ wget https://archive.apache.org/dist/hadoop/core/hadoop-3.1.1/hadoop-3.1.1.tar.gz
~$ tar -xvf hadoop-3.1.1.tar.gz
~$ mv hadoop-3.1.1/ hadoop
~$ rm hadoop-3.1.1.tar.gz
```

3.1 Configure environment variables. Add the following in ~/.bashrc
```.env
export HADOOP_HOME=/home/arpit/hadoop
export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop
export HADOOP_HDFS_HOME=$HADOOP_HOME
export HADOOP_INSTALL=$HADOOP_HOME
export HADOOP_MAPRED_HOME=$HADOOP_HOME
export HADOOP_COMMON_HOME=$HADOOP_HOME
export HADOOP_HDFS_HOME=$HADOOP_HOME
export YARN_HOME=$HADOOP_HOME
export HADOOP_COMMON_LIB_NATIVE_DIR=$HADOOP_HOME/lib/native
export PATH=$PATH:$HADOOP_HOME/sbin:$HADOOP_HOME/bin
export HADOOP_OPTS="-Djava.library.path=$HADOOP_HOME/lib/native"
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
```

3.2 Source .bashrc for these env variables
```bash
~$ source .bashrc
~$ echo $HADOOP_HOME #(To verify)
```

3.3 Update hadoop-env.sh (Comment JAVA_HOME and HADOOP_CONF_DIR variables and add these 2 lines)
```.env
~$ vi hadoop/etc/hadoop/hadoop-env.sh

export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
export HADOOP_CONF_DIR=${HADOOP_CONF_DIR:-"/home/arpit/hadoop/etc/hadoop"}
```

3.4 Replace entire core-site.xml file entirely with following content
```.env
~$ vi hadoop/etc/hadoop/core-site.xml

<?xml version="1.0" encoding="UTF-8"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<configuration>
    <property>
        <name>fs.default.name</name>
        <value>hdfs://localhost:9000</value>
    </property>
</configuration>
```

3.5 Create NN and DN data directory.
```bash
~$ mkdir -p /home/arpit/hadoop/data/nn
~$ mkdir -p /home/arpit/hadoop/data/dn
```

3.6 Replace entire hdfs-site.xml file entirely with following content
```.env
~$ vi hadoop/etc/hadoop/hdfs-site.xml

<?xml version="1.0" encoding="UTF-8"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<configuration>
    <property>
        <name>dfs.replication</name>
        <value>1</value>
    </property>
    <property>
        <name>dfs.permission</name>
        <value>false</value>
    </property>
	<property>
		<name>dfs.namenode.name.dir</name>
		<value>file:///home/arpit/hadoop/data/nn</value>
	</property>
	<property>
		<name>dfs.datanode.data.dir</name>
		<value>file:///home/arpit/hadoop/data/dn</value>
	</property>
</configuration>
```

3.7 Format name-node and hadoop daemons (dfs and yarn)
```bash
~$ sudo service ssh start # If on wsl

~$ hdfs namenode -format
~$ start-dfs.sh
~$ start-yarn.sh
~$ jps #(To verify)
```
Open hdfs file browser in chrome : http://localhost:50070/explorer.html#/

## 4. Sample hdfs commands to verify installation
```bash
# Create directory in hdfs and list it

~$ hdfs dfs -mkdir /data
~$ hdfs dfs -chmod -R 1777 /data
~$ echo "Welcome to hadoop" > /tmp/welcome.txt
~$ hdfs dfs -put /tmp/welcome.txt /data
~$ hdfs dfs -ls /data
```