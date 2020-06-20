# Steps to install and run elasticsearch locally on port 9200/9300

## 1. Download elasticsearch binary [version 7.2.0] 

``` bash
~$ pwd
/home/arpit

~$ wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.2.0-linux-x86_64.tar.gz
~$ tar -xvf elasticsearch-7.2.0-linux-x86_64.tar.gz
~$ mv elasticsearch-7.2.0 elasticsearch
~$ rm elasticsearch-7.2.0-linux-x86_64.tar.gz
~$ cd elasticsearch
```

## 2. Configure elasticsearch server

Create a data directory for elasticsearch data
``` bash
~/elasticsearch$ mkdir -p /home/arpit/elasticsearch/data 
```

Update config/elasticsearch.yml to change data directory
```.env
~/elasticsearch$ vi config/elasticsearch.yml
```

Update the following property
```.env
path.data: /home/arpit/elasticsearch/data
```

Verify config/elasticsearch.yml for above changes
``` bash
~/elasticsearch$ cat config/elasticsearch.yml | grep path.data
```

## 3. Start elasticsearch server
```bash
# Start elasticsearch server. Blocking call
~/elasticsearch$ bin/elasticsearch
```

## 4. Verify elasticsearch installation
```bash
~/elasticsearch$ curl -XGET localhost:9200
```