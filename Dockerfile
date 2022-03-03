FROM openjdk:15
COPY ./src/ /tmp
WORKDIR /tmp
RUN javac HelloWorld.java
ENTRYPOINT ["java","HelloWorld"]
