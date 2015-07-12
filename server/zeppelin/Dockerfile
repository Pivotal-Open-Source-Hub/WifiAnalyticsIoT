FROM java:8u45-jdk

ENV MAVEN_VERSION 3.3.1
RUN apt-get update && apt-get -y install curl git nodejs npm

RUN curl -sSL http://archive.apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz | tar xzf - -C /usr/share \
    && mv /usr/share/apache-maven-$MAVEN_VERSION /usr/share/maven \
    && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

RUN  git clone https://github.com/apache/incubator-zeppelin
      
RUN cd incubator-zeppelin && mvn clean package -Pspark-1.3 -Dhadoop.version=2.2.0 -Phadoop-2.2 -DskipTests && \
    apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/* && rm -rf ~/.m2/repository/
