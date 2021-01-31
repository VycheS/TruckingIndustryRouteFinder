FROM maven:3.6-openjdk-11

# COPY src /home/app/src
# COPY pom.xml /home/app
# WORKDIR /home/app
# RUN mvn clean package

# оставляем пустой, для того чтобы адекватно работал mvnw
ENV MAVEN_CONFIG=""

EXPOSE 8080 80 443

# эта строка нужна для того чтобы при ошибках контейнер не завершался. Только для разработки
ENTRYPOINT ["tail", "-f", "/dev/null"]

# CMD ["mvn"]