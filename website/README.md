## Website
### Prerequisites (for Ubuntu)
Install required software
```
sudo apt-get install -y apache2 php libapache2-mod-php
```
Copy required files
```
sudo cp giganticgranite.com.conf /etc/apache2/sites-available/giganticgranite.com.conf
cd /var/www
sudo ln -s /location/to/repo/website/giganticgranite.com giganticgranite.com
```
Modify permissions
```
sudo chown www-data:www-data website/giganticgranite.com/uploads
```
Enable site
```
sudo a2ensite giganticgranite.com.conf
sudo service apache2 reload
```
Modify hosts
```
sudo vim /etc/hosts
127.0.0.1 giganticgranite.com
```
You can find website on ```giganticgranite.com```
