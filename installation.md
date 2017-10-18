# OpenRecordz installation from scratch

## Java 8
$ sudo add-apt-repository ppa:webupd8team/java
$ sudo apt-get update
$ sudo apt-get install oracle-java8-installer


## Maven
$ sudo apt-get install maven

## Git
$ sudo apt-get install git


## MongoDB
$ sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 0C49F3730359A14518585931BC711F9BA15703C6

$ echo "deb [ arch=amd64,arm64 ] http://repo.mongodb.org/apt/ubuntu xenial/mongodb-org/3.4 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-3.4.list

$ sudo apt-get update

$ sudo apt-get install -y mongodb-org

$ sudo service mongod start

Implementare auto boot 

# MySQL - usare servizi di amazon

# Apache - usare servizio di amazon 