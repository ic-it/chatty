#FROM maven:3.8.6-jdk-8 as maven

#RUN apt-get update

## Add oracle java 8 repository
#RUN echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections
#RUN apt-get -y install software-properties-common
#RUN add-apt-repository -y ppa:webupd8team/java
#RUN apt-get update
#RUN apt-get install -y default-jdk maven wget
#RUN rm -rf /var/lib/apt/lists/*
#RUN rm -rf /var/cache/oracle-jdk8-installer
#ENV JAVA_HOME /usr/lib/jvm/java-8-oracle

#WORKDIR /usr/src/app
#COPY ./src /usr/src/app/src
#COPY ./pom.xml /usr/src/app/pom.xml
#RUN mvn package
#
#RUN ls -l /usr/src/app
#
#COPY target/chatty.war /usr/src/app
#
#COPY target/chatty.war /usr/local/tomee/webapps/
#
#EXPOSE 8080




FROM maven:3.8.6-jdk-8 as maven

WORKDIR /usr/src/app
COPY ./src /usr/src/app/src
COPY ./pom.xml /usr/src/app/pom.xml
RUN mvn package

WORKDIR /user/local/tomcat

COPY ./tomcat /usr/local/tomcat

RUN mkdir /usr/local/tomcat/webapps/

RUN cp -R /usr/src/app/target/chatty /usr/local/tomcat/webapps/ROOT
RUN cp /usr/src/app/target/chatty.war /usr/local/tomcat/webapps/ROOT.war


RUN ls -l /usr/local/tomcat/


EXPOSE 8080

CMD ["sh", "/usr/local/tomcat/bin/catalina.sh", "run"]