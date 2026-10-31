FROM openjdk : 21-oracle
2 VOLUME /tmp
3 COPY target/ *. jar app.jar
4 ENTRYPOINT ["java","-jar", "/app.jar"]