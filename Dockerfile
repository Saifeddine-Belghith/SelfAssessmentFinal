FROM openjdk:17-jdk
MAINTAINER altersis
COPY personalTarget/SelfAssessment-0.0.1-SNAPSHOT.jar /SelfAssessment.jar
ENTRYPOINT ["java","-jar","/SelfAssessment.jar"]
