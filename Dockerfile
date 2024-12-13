FROM openjdk:23-jdk-oracle AS builder


WORKDIR /app

COPY mvnw .
COPY .mvn .mvn

COPY pom.xml .
COPY src src

RUN ./mvnw package -Dmaven.test.skip=true


FROM openjdk:23-jdk-oracle


WORKDIR /app

COPY --from=builder \ 
    /app/target/noticeboard-0.0.1-SNAPSHOT.jar app.jar

# RUN apt update && apt install -y curl 

ENV PORT=4000
EXPOSE ${PORT}

HEALTHCHECK --start-period=120s --interval=60s \
		CMD curl -s -f http://ingenious-wholeness-production.up.railway.app/status || exit 1

ENTRYPOINT java -jar app.jar