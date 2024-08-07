FROM openjdk:17-jdk-alpine
EXPOSE 1111
ADD target/category-service.jar category-service.jar
ENTRYPOINT ["java","-jar","/category-service.jar"]
