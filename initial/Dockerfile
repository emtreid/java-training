FROM openjdk:8-jdk-alpine
WORKDIR /Docker
CMD ["gradle", "clean"]
CMD ["gradle", "build"]
COPY ./build/libs/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/Docker/app.jar"]