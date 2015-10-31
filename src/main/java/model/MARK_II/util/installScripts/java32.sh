#!/bin/bash

if [ ! -d "/opt/java" ]; then
	# Makes a java directory such that all users of this linux system will use java 1.8.0_45
	mkdir /opt/java && cd /opt/java
else
	cd /opt/java
fi

if [ ! -f "jdk-8u45-linux-x64.tar.gz" ]; then
	# Downloading java 1.8.0_45 from the Oracle website
	sudo wget --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" "http://download.oracle.com/otn-pub/java/jdk/8u45-b14/jdk-8u45-linux-i586.tar.gz"
fi

# Extracting the 32 bit java 8 tarball
sudo tar -zxvf jdk-8u45-linux-i586.tar.gz

# moving to the java 8 directory
cd jdk1.8.0_45/


# making the java runnable a java 8 runnable and setting java 8 runnable's priority a 100 which happens to be higher than all your other java runnables
sudo update-alternatives --install /usr/bin/java java /opt/java/jdk1.8.0_45/bin/java 100

# To check if the priority has been set right for java 8
# Hit enter (return key) if the priorities look right
sudo update-alternatives --config java


# making the javac runnable a java 8's javac runnable and setting java 8's javac runnable's priority a 100 which happens to be higher than all your other javac runnables
sudo update-alternatives --install /usr/bin/javac javac /opt/java/jdk1.8.0_45/bin/javac 100
# To check if the priority has been set right for java 8
# Hit enter (return key) if the priorities look right
sudo update-alternatives --config javac


# making the jar a java 8's jar and setting java 8's jar's priority a 100 which happens to be higher than all your other jar
sudo update-alternatives --install /usr/bin/jar jar /opt/java/jdk1.8.0_45/bin/jar 100
# To check if the priority has been set right for java 8
# Hit enter (return key) if the priorities look right
sudo update-alternatives --config jar

# Adding the new JAVA_HOME, JRE_HOME and PATH to the bashrc file (file that is run every time you open a terminal)
cd ~
echo "export JAVA_HOME=/opt/java/jdk1.8.0_45/" >> ./.bashrc
echo "export JRE_HOME=/opt/java/jdk1.8.0._45/jre" >> ./.bashrc
echo "export PATH=$PATH:/opt/java/jdk1.8.0_45/bin:/opt/java/jdk1.8.0_45/jre/bin" >> ./.bashrc

# Checking java version
java -version
