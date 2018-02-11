FROM centos:centos7

MAINTAINER martijn <martijn@genohm.com>

RUN yum -y update && yum -y install java sysstat wget
RUN yum clean all

RUN mkdir /mnt/graphs
VOLUME /mnt/graphs
WORKDIR /opt/fuseki/apache-jena-fuseki-3.6.0

RUN mkdir -p /opt/fuseki
ENV PATH /opt/fuseki/apache-jena-fuseki-3.6.0:$PATH
ENV FUSEKI_BASE /opt/fuseki/apache-jena-fuseki-3.6.0

RUN cd /opt/fuseki/ && wget http://www-eu.apache.org/dist/jena/binaries/apache-jena-fuseki-3.6.0.tar.gz && tar zxf apache-jena-fuseki*.tar.gz

RUN mkdir /opt/fuseki/apache-jena-fuseki-3.6.0/configuration
ADD boinq.ttl /opt/fuseki/apache-jena-fuseki-3.6.0/configuration
ADD static.ttl /opt/fuseki/apache-jena-fuseki-3.6.0/configuration
ADD test.ttl /opt/fuseki/apache-jena-fuseki-3.6.0/configuration
ADD fuseki-server /opt/fuseki/apache-jena-fuseki-3.6.0
ADD shiro.ini /opt/fuseki/apache-jena-fuseki-3.6.0

RUN chmod -R 755 /opt/fuseki/

ENTRYPOINT ["fuseki-server", "4g"]

EXPOSE 3030
