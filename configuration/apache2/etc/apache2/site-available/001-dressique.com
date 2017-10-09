<VirtualHost *:80>
        ServerAdmin info@ciaotrip.it
        ServerName ciaotrip.it
#	JkMount  /* ajp13_worker
#        ServerAlias *.dressique.com
        ProxyPass / ajp://localhost:8009/
	ProxyPassReverse /  ajp://localhost:8009/

#	DocumentRoot /var/www
#        <Directory />
#                Options FollowSymLinks
#                AllowOverride None
#        </Directory>
#       <Directory /var/www/>
#               Options Indexes FollowSymLinks MultiViews
#                AllowOverride None
#                Order allow,deny
#                allow from all
#        </Directory>

#RewriteEngine On
#RewriteOptions Inherit 
#RewriteRule ^/default/near /default/signin
#RewriteLog "/var/log/apache2/rewrite-shop.log" 

RewriteEngine on
RewriteCond %{HTTP_HOST}  ^ciaotrip.it$       [NC]
RewriteRule ^(.*)         http://www.ciaotrip.it$1  [R=301]

#ciaotrip.com
RewriteCond %{HTTP_HOST} ^ciaotrip.com$ [NC,OR] 
RewriteCond %{HTTP_HOST} ^www.ciaotrip.com$ [NC]
RewriteRule ^(.*)$ 	 http://www.ciaotrip.it$1 [L,R=301]

#fa redirect per candido.frontiere21.it
#RewriteCond %{HTTP_HOST}   ^candido.frontiere21.it [NC]
#RewriteRule ^/(.*)         http://candido1859.frontiere21.it/$1 [L,R=301]

#fa redirect per candido.dressique.com
#RewriteCond %{HTTP_HOST}   ^candido.dressique.com [NC]
#RewriteRule ^/(.*)         http://candido1859.dressique.com/$1 [L,R=301]


#RewriteCond %{HTTP_HOST} !^www\. [NC]
#RewriteRule ^(.*)$ http://www.%{HTTP_HOST}%{REQUEST_URI} [R=301,L]

</VirtualHost>
