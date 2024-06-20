# Eirbone : une application d’échange de gros Fichiers en Pair à Pair (P2P)

 
Dans ce répertoire se trouve 2 répertoires, le premier est le répertoire **peer** qui a été généré avec *Maven* pour faciliter la compilation des fichiers Java et un répertoire **tracker**. Pour pouvoir utiliser les différentes parties de l'application, les instructions sont les suivantes :

Si maven n'est pas installé sur votre ordinateur, vous pouvez l'installer facilement sur ubuntu en suivant le tutoriel sur ce lien : [Installer Maven](https://doc.ubuntu-fr.org/maven). Sur ce lien vous pourrez cliquer sur le mot maven qui l'installera automatiquement. Sinon, si vous souhaitez l'installer manuellement, vous pouvez suivre ces étapes ( Source : https://serverspace.io/fr/support/help/how-to-install-maven-on-ubuntu/): 
 ```
cd /tmp; wget https://dlcdn.apache.org/maven/maven-3/3.9.2/binaries/apache-maven-3.9.2-bin.tar.gz
```
Puis, il faut faire 
```
tar xf apache-maven-3.9.2-bin.tar.gz
```

```
mv apache-maven-3.9.2 /opt/maven
```
Ensuite, il faut définir le propriétaire correcte : 
```
chown -R root:root /opt/maven
```
Puis, il faut faire 
```
cat <<EOF >>&nbsp;/etc/profile.d/mymavenvars.sh
export JAVA_HOME=/usr/lib/jvm/default-java
export M2_HOME=/opt/maven
export MAVEN_HOME=/opt/maven
export PATH=${M2_HOME}/bin:${PATH}
EOF
```
 
```
ln -s /opt/maven/bin/mvn /usr/bin/mvn
```

```
chmod 755 /etc/profile.d/mymavenvars.sh
```
Ensuite, il faut faire 
```
source /etc/profile.d/mymavenvars.sh
```
pour ne pas avoir a redémarrer la machine.
Avec la commande 
```
mvn --version
```
on peut vérifier que *Maven* a bien été installé


Pour lancer le tracker dans un terminal, il faut se rendre dans le répertoire **tracker** puis il est possible de compiler les différents fichiers et de générer l'exécutable *tracker* en faisant la commande : 

```
make tracker
```
Nous pouvons également faire la commande 
 ```
 make test
 ``` 
si nous souhaitons compiler les tests et générer l'exécutable *test*. Ensuite, pour lancer le tracker sur le **port 3000** par exemple, il faut faire : 
```
./install/tracker port 3000
```
Cette valeur n'a pas été choisie au hasard car c'est celle que nous avons renseigné aux pairs.
La configuration au travers du **config.ini** est aussi disponible, il faut executer le même programme mais sans arguements.
Une fois le tracker lancé, il faut lancer un nouveau terminal et se rendre dans le répertoire **peer** puis faire la commande : 
```
mvn compile
```
pour compiler les fichiers *Java*. Ensuite, pour lancer le premier pair, il faut se rendre dans le répertoire **classes** qui se trouve dans **target**. En partant du répertoire **peer**, la commande est : 
```
cd target/classes
```
Ensuite, pour démarrer le premier peer, il faut faire :
```
java org.peer.Main
``` 
Pour démarrer un second pair, il faut redémarrer un terminal et suivre les étapes précédentes puis faire : 
```
java org.peer.mainClass
```
Pour démarrer les tests, il faut se rendre dans le répertoire **peer** puis faire la commande :
```
mvn test
```