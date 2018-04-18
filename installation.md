# OpenRecordz installation from scratch

## Java 8
```
sudo add-apt-repository ppa:webupd8team/java
sudo apt-get update
sudo apt-get install oracle-java8-installer
```

## Maven
```
sudo apt-get install maven
```

## Git
```
sudo apt-get install git
```

## MongoDB
```
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 0C49F3730359A14518585931BC711F9BA15703C6

echo "deb [ arch=amd64,arm64 ] http://repo.mongodb.org/apt/ubuntu xenial/mongodb-org/3.4 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-3.4.list

sudo apt-get update

sudo apt-get install -y mongodb-org

sudo service mongod start
```

Implementare auto boot 

# MySQL

Install mysql or use a cloud service (AWS RDS)

## MySQL client

```
sudo apt-get install mysql-client
```


# Apache2 

Install Apache2 and mod_proxy:

```
sudo apt-get install apache2
sudo apt-get install libapache2-mod-jk
sudo a2enmod proxy
sudo a2enmod proxy_http
sudo a2enmod proxy_ajp

```

Create a virtualhost file named openrecordz.conf under /etc/apache2/sites-available

```
<VirtualHost *:80>
        ServerAdmin info@openrecordz.com
        ServerName api.openrecordz.com
        ServerAlias *.api.openrecordz.com

        ProxyRequests Off
        <Proxy *>
                Order Allow,Deny
                Allow from all
        </Proxy>

        ProxyPass / ajp://localhost:9099/
        ProxyPassReverse /  ajp://localhost:9099/

</VirtualHost>
```

Enable the site

```
sudo a2ensite openrecord.conf 
```



# Openrecordz data dir configuration

```
cd /var/lib
sudo mkdir openrecordz
sudo chown ubuntu:ubuntu openrecordz
cd openrecordz
mkdir scripts
mkdir files
```

ATTENTION:

REMEMBER TO CHANGE YOUR OPENRECORDZ ADMIN PASSWORD


# Set a Custom Domain (custom DNS)

* Create a CNAME DNS entry in your DNS control panel like this:
dati.comune.NOMECOMUNE.it -> CNAME ->   NOMECOMUNE.openrecordz.com


```

curl -v -X POST -u '<ADMIN_USERNAME>:<ADMIN_PASSWORD>' -H 'Content-Type: text/plain;charset=UTF-8' -d '{"json":{"code":"tenants.mapping.<CUSTOM_DOMAIN_ENTRY>","message":"<TENANT>"}}' http://<TENANT>.api.openrecordz.com/service/v1/cdata/_message_source/

```

Example:

```
curl -v -X POST -u '<ADMIN_USERNAME>:<ADMIN_PASSWORD>' -H 'Content-Type: text/plain;charset=UTF-8' -d '{"json":{"code":"tenants.mapping.comuneditest.frontiere21.com","message":"comuneditest"}}' http://comuneditest.api.openrecordz.com/service/v1/cdata/_message_source/
```
## GET a tenant config
```
curl -v -X GET -u '<ADMIN_USERNAME>:<ADMIN_PASSWORD>' -H "Content-Type: application/json" http://comuneditest.api.openrecordz.com/service/v1/cdata/_message_source/
```







