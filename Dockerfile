FROM maven:latest

LABEL authors="mikaelnyman"

WORKDIR /app

COPY pom.xml /app/

COPY . /app/

RUN mvn package

CMD ["java", "-jar", "target/OTP1_inclass1_assignment_MikaelNyman-1.0-SNAPSHOT.jar"]