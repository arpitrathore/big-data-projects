# Steps to install latest version of minio and s3cmd
 

## 1. Download and start minio server on port 9005

``` bash
~$ pwd
/home/arpit

~$ mkdir -p minio && cd minio/
~/minio$ wget https://dl.min.io/server/minio/release/linux-amd64/minio
~/minio$ mkdir -p data-dir
~/minio$ vi start-minio.sh
```

Create a file start-minio.sh with following content-
```.env
export MINIO_ACCESS_KEY=myaccesskey9005
export MINIO_SECRET_KEY=mysecretkey9005
./minio server --address ":9005" data-dir
```

Make this file executable and start minio server
```bash
~/minio$ chmod +x start-minio.sh

# Following command is blocking call
~/minio$ ./start-minio.sh
```

## 2. Install and configure s3cmd (s3 compatible client)
Open a new terminal to install and configure s3cmd
```bash
~$ sudo apt install -y s3cmd
~$ s3cmd --configure
```
Change only following settings, keep remaining settings as default-
```.env
Access Key: myaccesskey9005
Secret Key: mysecretkey9005
S3 Endpoint [s3.amazonaws.com]: localhost:9005
DNS-style bucket+hostname:port template for accessing a bucket [%(bucket)s.s3.amazonaws.com]: localhost:9005
Use HTTPS protocol [Yes]: No
```

Save the settings
```bash
Test access with supplied credentials? [Y/n] y
Save settings? [y/N] y
```
## 3. Sample s3 commands to verify installation
```bash
~$ s3cmd mb s3://test-bucket
~$ s3cmd ls s3://
~$ echo "hello, this is a test file" >> test.txt
~$ s3cmd put test.txt s3://test-bucket
~$ s3cmd ls -H s3://test-bucket
~$ rm test.txt

# Optional
~$ s3cmd rb --recursive s3://test-bucket
```