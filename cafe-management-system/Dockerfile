#1) คอมไพล์โค้ด
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY src ./src
RUN javac src/com/cafe/model/*.java src/com/cafe/server/*.java -d bin

#2) รันเว็บ
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/bin ./bin
COPY web ./web

EXPOSE 8080
CMD ["java", "-cp", "bin", "com.cafe.server.ApiServer"]
