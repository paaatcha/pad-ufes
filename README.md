# Projeto de apoio dermatológico UFES (PAD-UFES)

Neste repositório está armazenado o sistema de obtenção de dados dermatológicos de pacientes atendendidos pelo PAD-UFES. O sistema foi implementado utilizando spring-boot, joinfaces e primefaces.

Como o sistema possui dados de pacientes, o seu uso é privado e exclusivo para os participantes do projeto.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

##### Java

Se ainda não tiver o repositório do OpenJDK 8:
```
sudo add-apt-repository ppa:openjdk-r/ppa
```
Atualize o APT com o comando:
```
sudo apt-get update
```
Agora use o comando para instalar:
```
sudo apt-get install openjdk-8-jdk
```
Se você tiver mais de uma versão do Java instalado, execute os comandos para definir o padrão:
```
sudo update-alternatives --config java
sudo update-alternatives --config javac
```
Finalmente confira a versão Java atual:
```
java -version
```

##### Apache

Para instalar execute o comando:
```
sudo apt-get install apache2
```

##### MySQL

Para instalar execute o comando:
```
sudo apt-get install mysql-server
```

##### PHP

Instale primeiramente as ferramentas necessárias:
```
sudo apt-get install python-software-properties
```
Adicione o repositório:
```
sudo add-apt-repository ppa:ondrej/php
```
Atualize os repositórios:
```
sudo apt-get update
```
Faça a instalação:
```
sudo apt-get install php7.0 php7.0-fpm
sudo apt-get install libapache2-mod-php7.0
sudo apt-get install php7.0-mysql -y
```
Para o Apache dar prioridade aos arquivos php, altere o seguinte arquivo para:
```
sudo nano /etc/apache2/mods-enabled/dir.conf

<IfModule mod_dir.c>
       DirectoryIndex index.php index.html index.cgi index.pl index.xhtml index.htm
</IfModule>
```
Verifique a instalação:
```
php --version
```

##### Maven

https://maven.apache.org/install.html

### Installing

Na pasta principal do projeto, execute:

```
mvn spring-boot:run
```

Crie o usuário PAD no banco de dados no endereço:
```
localhost/phpmyadmin
```
Crie um usuário no pad no seguinte endereço:
```
localhost:8080
```

## Built With
 spring-boot, joinfaces e primefaces.
* [Spring-boot](https://spring.io/projects/spring-boot)
* [Joinfaces](https://github.com/joinfaces/joinfaces)
* [Primefaces](https://www.primefaces.org/)
* [Maven](https://maven.apache.org/) - Dependency Management

## Contributing

## Versioning

## Authors

* **André** - *Initial work*

## License


## Acknowledgments

* Hat tip to anyone whose code was used
* Inspiration
* etc
