FROM maven:latest

LABEL authors="mikaelnyman"

WORKDIR /app

COPY pom.xml /app/

COPY . /app/

RUN apt-get update && apt-get install -y libx11-6 libxext6 \
    libxrender1 libxtst6 libxi6 libgtk-3-0 mesa-utils wget unzip \
    && rm -rf /var/lib/apt/lists/*

RUN mkdir -p /javafx-sdk \
    && wget -O javafx.zip https://download2.gluonhq.com/openjfx/21/openjfx-21_linux-x64_bin-sdk.zip \
    && unzip javafx.zip -d /javafx-sdk \
    && mv /javafx-sdk/javafx-sdk-21/lib /javafx-sdk/lib \
    && rm -rf /javafx-sdk/javafx-sdk-21 javafx.zip

COPY target/OTP1_inclass1_assignment_MikaelNyman-1.0-SNAPSHOT.jar app.jar

ENV DISPLAY=host.docker.internal:0.0

RUN mvn package

CMD [
    "java",
    "--module-path", "/javafx-sdk/lib",
    "-add-modules", "javafx.controls, javafx.xml",
    "-Dprism.order=sw",
    "-Dprism.verbose=true",
    "-jar", "app.jar"
]