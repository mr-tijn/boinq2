FROM centos:centos7

MAINTAINER martijn <martijn@genohm.com>

RUN yum -y update && yum -y install java sysstat wget curl nc
RUN yum clean all

VOLUME /tmp
RUN mkdir -p /opt/boinq/files
COPY ../../../target/boinq-1.0.0-ALPHA.war /boinq.war
COPY run.sh /run.sh
COPY createBoinq.xml /createBoinq.xml
COPY createBoinqStatic.xml /createBoinqStatic.xml
COPY createTest.xml /createTest.xml
RUN chmod +x /run.sh
WORKDIR /
ENTRYPOINT ["/run.sh", "4g"]

EXPOSE 8080
