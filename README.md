# ftp2mqtt

This microservice acts as a bridge between IP cameras (with FTP support
for motion detected events) and an MQTT broker.

It should run on a private network only, it is not recommend to expose it
to the internet.


## How to build

You need the Java Development Kit (JDK) 10 installed on your system.

1. Clone this repository:

   ```
   git clone https://github.com/1element/ftp2mqtt.git .
   ```

2. Run Gradle build:

   ```
   ./gradlew build
   ```

You will end up with an artifact (`ftp2mqtt-<VERSION>.jar`) in
the `build/libs/` directory.

You can start the application by executing:

```
java -jar ftp2mqtt-<VERSION>.jar
```

If you want to use Docker there is a Gradle task to build a Docker image:

```
./gradlew build docker
```

This can also be done manually by passing the artifact as an argument
to the Docker build command:

```
docker build -t ftp2mqtt --build-arg JAR_FILE=build/libs/ftp2mqtt-<VERSION>.jar .
```


## Configuration

If you start the application Spring Boot expects a configuration file
`application.properties` in the same directory where your JAR file is
located. If you use Docker there is a volume `/config` for this.

Example configuration [application.properties](https://github.com/1element/ftp2mqtt/blob/master/src/main/resources/application.properties):

```
# FTP server
ftp.port=2121
ftp.password=secret

# MQTT
mqtt.broker-connection=ssl://localhost:1883
mqtt.username=username
mqtt.password=password
mqtt.topic-prefix=ipcamera/
```

You can specify the port and the password for the built-in FTP server.
Connections are accepted for any username. The FTP username will be
prefixed with the configured `mqtt.topic-prefix` and used as the
MQTT topic to publish to.

So if your IP camera uploads motion detected images to the FTP server
using the username "backyard", the image will be published to the MQTT
brokers topic `ipcamera/backyard` (assuming "ipcamera/" is configured as
the prefix).

You also need to configure your MQTT broker connection (host and port)
as well as the username and password.


## Contributions

This application was written for my own purpose, therefore it may lack in
features and configuration abilities. In case it might be useful for
someone I decided to share it. However, pull requests are welcome.


## License

This project is licensed under the terms of the GNU Affero General Public License
as published by the Free Software Foundation, either version 3 of the License,
or (at your option) any later version.

For more information, see [LICENSE.md](https://github.com/1element/ftp2mqtt/blob/master/LICENSE.md).
