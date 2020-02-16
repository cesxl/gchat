## gchat SpringBoot Demo

### 项目说明

1.创建 `MySQL` 数据库 `gchat`，导入 `db` 文件夹下的 `gchat.sql`.

2.项目启动入口：`com.demo.modules.Application`.

3.配置文件 application.yml

4.登录账户：`superadmin` 密码：`0`

5.`Solr` 搜索`Demo` 访问入口：`http://ip:port/gchat/search`

6.`Solr Admin` 账户：`solr` 密码：`SolrRocks`

### Docker

```
# 创建运行 MySQL 容器
docker run -di -v /usr/local/mysql/data/:/var/lib/mysql -v /usr/local/mysql/my.cnf:/etc/mysql/my.cnf -p 3306:3306 --name mysql -e MYSQL_ROOT_PASSWORD=root --restart=no mysql:latest

# copy 宿主机文件至 MySQL 容器内
docker cp /usr/local/gchat.sql gchat:/usr/local/gchat.sql

# 进入 mysql 容器
docker exec -it mysql bash

# 创建 gchat 容器运行
docker run -d --network=host \
    --name gchat \
    --env jdbc.type="mysql" \
    --env jdbc.username="root" \
    --env jdbc.password="root" \
    --env jdbc.driver="com.mysql.cj.jdbc.Driver" \
    --env jdbc.url="jdbc:mysql://127.0.0.1:3306/gchat?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai" \
    com.demo/gchat:1.0-SNAPSHOT

# 查看日志
docker logs gchat

# docker-compose 命令
docker-compose -f ./docker-compose.yml up -d
docker-compose down
docker-compose start/stop/restart
```