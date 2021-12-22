FROM openjdk:11.0.11-jdk
USER root
WORKDIR /blog
VOLUME ["/blog"]
ADD personal-blog-multi-module-web-0.0.1-SNAPSHOT.jar blog.jar
EXPOSE 8989
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo "Asia/Shanghai" > /etc/timezone
ENTRYPOINT ["java","-jar","blog.jar"]