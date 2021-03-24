# Skilland-backend

To run the project, you need to clone the repository. Next, start the database service by running command "docker-compose up db"
in the terminal of the main project directory.
Next, start the DemoApplication project itself.

check API at http://localhost:8080/swagger-ui/index.html

All new functionality will be through the replenishment of the endpoints that can be seen on the swagger. To do this, periodically
request "git pull".

Communication client-service takes place via HTTP and Websockets. Websockets are only used during gameplay to exchange the current game state between
all the participants. Testing websockets is done using the URI http://localhost:8080/(basic functionality has been added so far). Testing HTTP requests
to http://localhost:8080/swagger-ui/index.html
