FROM openjdk:11-jdk

VOLUME /tmp

ARG JAR_FILE=./build/libs/batch-1.0.jar

COPY ${JAR_FILE} app.jar

# 운영
ENTRYPOINT ["nohup","java","-jar","-Dspring.profiles.active=prod","app.jar","2>&1","&"]
