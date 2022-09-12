this service is used to store data of all the placed orders by our customers
# Deployment

### Build into docker image
1. cd to $PROJECT_ROOT
2. cd to ./configuration and do git pull to make sure configuration submodule is at newest commit
3. `mvn -Dcustomer-detail-service=localhost -DDB_PASSWORD=Microservice@39909204 -DDB_USERNAME=microservice -DHOST=localhost -Dorder-service=localhost -Dproduction-ticket-service=localhost -Duser-service=localhost clean package`

   maven package execute tests and we want it to be able to load application context, thus the HOST environment variable is needed
   when running mvn package, there will be a .jar under $PROJECT_ROOT/target/production-ticket-service-<version>.jar. The docker file then uses this location to copy into the image
4. `docker build -t production-ticket-service:<version> .`

# Running this service locally
There are some preconfigured environment variables that need to be fulfilled and they are listed below

### running in Intellij from source code
edit run configurations and copy-paste below code block to add environment variables

    DB_PASSWORD=Microservice@39909204;DB_USERNAME=microservice;HOST=localhost;CUSTOMER_DETAIL_SERVICE_ADDRESS=localhost;ORDER_SERVICE_ADDRESS=localhost;PRODUCTION_TICKET_SERVICE_ADDRESS=localhost;USER_SERVICE_ADDRESS=localhost

### running in terminal from packaged .jar
Once a jar is packaged, assuming the name of the .jar file is `production-ticket-service-0.0.1.jar` we can go to its directory and run

      java -jar order-service-0.0.1.jar -Dcustomer-detail-service=localhost -DDB_PASSWORD=Microservice@39909204 -DDB_USERNAME=microservice -DHOST=localhost -Dorder-service=localhost -Dproduction-ticket-service=localhost -Duser-service=localhost

### running with docker from image
      docker pull andersonhsieh0330/order-service:<version>

assuming the image repository is production-ticket-service and tag(<version>) is 0.0.1

      docker run -p 8087:8087 -p 9097:9097 -ti --env customer-detail-service=localhost --env DB_PASSWORD=Microservice@39909204 --env DB_USERNAME=microservice --env HOST=host.docker.internal --env order-service=localhost --env production-ticket-service=localhost --env user-service=localhost andersonhsieh0330/production-ticket-service:0.0.1