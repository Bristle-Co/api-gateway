FROM openjdk:17
MAINTAINER AndersonHsieh
RUN mkdir app
WORKDIR /app
COPY target/api-gateway-0.0.1.jar app/api-gateway-0.0.1.jar
ENTRYPOINT ["java","-jar","app/api-gateway-0.0.1.jar"]
EXPOSE 8088