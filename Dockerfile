FROM maven:3.8.6-jdk-8 as maven

WORKDIR /usr/src/app
COPY ./src /usr/src/app/src
COPY ./pom.xml /usr/src/app/pom.xml
RUN mvn package

WORKDIR /user/local/tomcat


RUN mkdir /usr/local/downloads
RUN ls -l /usr/local/downloads/
RUN wget https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.64/bin/apache-tomcat-9.0.64.zip -O /usr/local/downloads/tomcat.zip
RUN unzip /usr/local/downloads/tomcat.zip -d /usr/local/downloads/
RUN mv /usr/local/downloads/apache-tomcat* /usr/local/tomcat

RUN rm -rf /usr/local/tomcat/webapps/*

RUN cp -R /usr/src/app/target/chatty /usr/local/tomcat/webapps/ROOT
RUN cp /usr/src/app/target/chatty.war /usr/local/tomcat/webapps/ROOT.war


RUN ls -l /usr/local/tomcat/


EXPOSE 8080

CMD ["sh", "/usr/local/tomcat/bin/catalina.sh", "run"]
