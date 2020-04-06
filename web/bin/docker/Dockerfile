FROM maven:3-jdk-8-alpine
MAINTAINER "人间春荡荡 <人间春荡荡@126.com>"

WORKDIR /opt/gchat
ADD gchat.war .
RUN jar -xvf gchat.war

ENV TZ=Asia/Shanghai
ENV JAVA_OPTS "-Xms1024m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m"
EXPOSE 8080

ENTRYPOINT java -cp /opt/gchat $JAVA_OPTS org.springframework.boot.loader.WarLauncher

# docker run -d --network=host \
#    --name gchat \
#    --env jdbc.type="mysql" \
#    --env jdbc.username="root" \
#    --env jdbc.password="root" \
#    --env jdbc.driver="com.mysql.cj.jdbc.Driver" \
#    --env jdbc.url="jdbc:mysql://127.0.0.1:3306/gchat?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai" \
#    com.demo/gchat:1.0-SNAPSHOT

# mvn clean package docker:build -Dmaven.test.skip=true -U -Pdocker