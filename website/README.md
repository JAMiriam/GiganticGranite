## Website
### Prerequisites (for Ubuntu)
Install required software
```
sudo apt-get install -y apache2 php libapache2-mod-php curl imagemagick php-imagick composer
```
Install php dependencies via composer
```
cd /path/to/repo/website/giganticgranite.com/public_html
composer install
```
Set permissions
```
sudo chmod 755 *
```
Copy required files
```
sudo cp giganticgranite.com.conf /etc/apache2/sites-available/giganticgranite.com.conf
sudo ln -s /path/to/repo/website/giganticgranite.com /var/www/giganticgranite.com
```
Enable site
```
sudo a2enmod ssl
sudo a2ensite giganticgranite.com.conf
sudo service apache2 reload
```
Modify hosts
```
sudo vim /etc/hosts
127.0.0.1 giganticgranite.com
```
You can find website on ```https://giganticgranite.com```
