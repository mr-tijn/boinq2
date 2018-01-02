FROM centos:centos7

MAINTAINER martijn <martijn@genohm.com>

RUN yum -y update && yum -y install java sysstat wget curl nc
RUN yum clean all

VOLUME /tmp
RUN mkdir -p /opt/boinq/files

#COPY boinq.war /boinq.war
ARG WAR_FILE
ADD target/${WAR_FILE} /boinq.war
COPY docker/boinq/build/run.sh /run.sh
COPY docker/boinq/build/createBoinq.xml /createBoinq.xml
COPY docker/boinq/build/createBoinqStatic.xml /createBoinqStatic.xml
RUN chmod +x /run.sh
WORKDIR /
ENTRYPOINT ["/run.sh", "4g"]

EXPOSE 8080
