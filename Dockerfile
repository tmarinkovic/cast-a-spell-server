FROM java:openjdk-8-jre-alpine

# add directly the war
ADD build/libs/cast-a-spell-server-0.1.0.jar app.jar

RUN sh -c 'touch /app.jar'
VOLUME /tmp
EXPOSE 8080
CMD exec java -jar /app.jar