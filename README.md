## Temperature converter

### Technologies & tools used
- Java 21
- Junit
- JaCoCo
- JavaFX
- MariaDB
- Jenkins
- Docker

### Design approach & implementation
The application GUI is defined in the Main class.

Unit tests are located in "src/test/java/", tests cover only the temperature conversion logic.

A DAO class is used for interactions with the database, so the rest of the application code is not directly responsible for managing database connections or creating SQL statements. Temperature records are created every time a conversion is done, with the result being stored in the database. The database schema has one table: "temperature_record" which contains the id, input temperature, result and conversion used.

#### Jenkins pipeline
1. Checkout repo
2. Build with "mvn clean install"
3. Test with "mvn test"
4. Generate JaCoCo report
5. Publish JaCoCo report
6. Build Docker image
7. Push Docker image to Docker Hub

### How to run

#### Local

First uncomment the localhost URL in DatabaseConnection class and comment the docker URL, then run:

`mvn clean package`

`mvn javafx:run`

#### Docker

`mvn clean package`

`docker build -t mikaelnyman/tempconverter .`

`docker run --rm -e DISPLAY=host.docker.internal:0.0 mikaelnyman/tempconverter`