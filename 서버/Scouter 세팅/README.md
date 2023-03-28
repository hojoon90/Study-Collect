# Scouter 이미지 생성

## Scouter Collector 서버 생성

Docker 이미지를 이용하여 Scouter를 실행 시켜야하는 작업이 있어서 아래와 같이 작업을 진행하였다.\
먼저 Scouter를 띄우기 위한 Dokcer image 파일을 먼저 만들어주었다.

Scouter Collector 최종 버전 Dockerfile
```bash
FROM dev.hjchoi.com:5000/centos.7.base:latest

USER root

RUN yum -y install unzip

USER app

# install and setup application
WORKDIR /app/install

# install java development kit
RUN curl -O https://download.java.net/java/GA/jdk14.0.1/664493ef4a6946b186ff29eb326336a2/7/GPL/openjdk-14.0.1_linux-x64_bin.tar.gz
RUN tar xvf openjdk-14.0.1_linux-x64_bin.tar.gz

RUN mkdir /app/jdk
RUN mv /app/install/jdk-14.0.1 /app/jdk/14.0.1

# install scouter agent
RUN wget https://github.com/scouter-project/scouter/releases/download/v2.15.0/scouter-all-2.15.0.tar.gz
RUN tar xvf scouter-all-2.15.0.tar.gz

# install scouter-paper
RUN wget https://github.com/scouter-contrib/scouter-paper/releases/download/2.6.4/scouter-paper-v2.6.4.zip
RUN unzip -o scouter-paper-v2.6.4.zip -d /app/install/scouter/server/extweb

USER root

RUN mv /app/install/scouter/server /home/scouter-server

# Java 환경 설정
ENV JAVA_HOME /app/jdk/14.0.1
ENV PATH $PATH:$JAVA_HOME/bin

RUN source /etc/profile

ADD scouter.conf /home/scouter-server/conf/scouter.conf
ADD startup.sh /home/scouter-server/startup.sh

RUN ls -lat /home/scouter-server;chmod 755 -R /home/scouter-server
RUN chown app:app /home/scouter-server/startup.sh

# 컨테이너 실행시 실행될 명령
CMD /bin/bash

# install and setup application
WORKDIR /home/scouter-server

ENTRYPOINT ["/home/scouter-server/startup.sh"]
```

위의 Docker image 생성시 사용되는 명령어들은 아래와 같다.

FROM: 이미지를 생성할 때 사용하는 기반 이미지\
USER: 서버 사용자 변경 (root, app)\
RUN: 명령어 실행\
WORKDIR: 작업을 진행할 디렉토리 (RUN, CMD, ENTRYPOINT의 명령이 실행될 디렉터리)\
ENV: 환경 변수 설정 (Java 등)\
ADD: 호스트에 있는 파일을 컨테이너 안에 추가해준다.\
ENTRYPOINT: 컨테이너 실행시 실행되는 스크립트 혹은 명령어

scouter.conf
```bash
server_id=SCCOUTER-COLLECTOR

#Activating Http Server
net_http_server_enabled=true
net_http_api_swagger_enabled=true

#Activating Scouter API
net_http_api_enabled=true
```

startup.sh
```bash
#!/usr/bin/env bash
export JAVA_OPT=${JAVA_OPT:--Xms1024m -Xmx1024m}
java $JAVA_OPT -classpath /home/scouter-server/scouter-server-boot.jar scouter.boot.Boot /home/scouter-server/lib
```

파일 세팅이 완료되었으면 docker build 실행.

```bash
# docker 빌드
[root@server1 scouter]# docker build -t scouter.new.collector:v1 -f /app/scouter/Dockerfile /app/scouter
......
Step 17/25 : ENV PATH $PATH:$JAVA_HOME/bin
 ---> Running in d8febf5948ff
Removing intermediate container d8febf5948ff
 ---> d140dd2a6cdf
Step 18/25 : RUN source /etc/profile
 ---> Running in 954ff9b9a594
Removing intermediate container 954ff9b9a594
 ---> a74ac4f1488d
Step 19/25 : ADD scouter.conf /home/scouter-server/conf/scouter.conf
 ---> 007df1d14ade
Step 20/25 : ADD startup.sh /home/scouter-server/startup.sh
 ---> 5c109289e5e8
Step 21/25 : RUN ls -lat /home/scouter-server;chmod 755 -R /home/scouter-server
 ---> Running in 64359ce9d90d
total 76
drwxr-xr-x 1 app  app    24 Jan  5 14:44 .
drwxr-xr-x 1 app  app    26 Jan  5 14:44 conf
drwxr-xr-x 1 root root   28 Jan  5 14:44 ..
drwxr-xr-x 6 app  app   163 Jan  5 14:44 extweb
drwxr-xr-x 2 app  app  4096 Jan  5 14:43 lib
drwxr-xr-x 2 app  app   205 Jan  5 14:43 plugin
-rw-r--r-- 1 root root  184 Jan  5 14:13 startup.sh
-rwxr-xr-x 1 app  app   119 Oct  1 13:57 env.sh
-rwxr-xr-x 1 app  app   150 Oct  1 13:57 sample1.startcon.sh
-rwxr-xr-x 1 app  app   304 Oct  1 13:57 sample1.startup.sh
-rwxr-xr-x 1 app  app   489 Oct  1 13:57 sample2.readlink.sh
-rwxr-xr-x 1 app  app   348 Oct  1 13:57 sample2.startup.sh
-rwxr-xr-x 1 app  app    74 Oct  1 13:57 sample2.stop.sh
-rwxr-xr-x 1 app  app  1294 Oct  1 13:57 sample3.startup.sh
-rw-r--r-- 1 app  app  4576 Oct  1 13:57 scouter-server-boot.jar
-rw-r--r-- 1 app  app    84 Oct  1 13:57 startcon.bat
-rwxr-xr-x 1 app  app    96 Oct  1 13:57 startcon.sh
-rw-r--r-- 1 app  app    76 Oct  1 13:57 startup.bat
-rw-r--r-- 1 app  app    75 Oct  1 13:57 startup_512m.bat
-rwxr-xr-x 1 app  app   145 Oct  1 13:57 startup_512m.sh
-rw-r--r-- 1 app  app    13 Oct  1 13:57 stop.bat
-rwxr-xr-x 1 app  app    16 Oct  1 13:57 stop.sh
Removing intermediate container 64359ce9d90d
 ---> a727f1843744
Step 22/25 : RUN chown app:app /home/scouter-server/startup.sh
 ---> Running in 979b33354642
Removing intermediate container 979b33354642
 ---> 96d17c220978
Step 23/25 : CMD /bin/bash
 ---> Running in ae5a3d739201
Removing intermediate container ae5a3d739201
 ---> a8eab677d324
Step 24/25 : WORKDIR /home/scouter-server
 ---> Running in d469481e1c65
Removing intermediate container d469481e1c65
 ---> 53533ee79d1d
Step 25/25 : ENTRYPOINT ["/home/scouter-server/startup.sh"]
 ---> Running in 20b4955085e4
Removing intermediate container 20b4955085e4
 ---> 7221a400a573
Successfully built 7221a400a573
Successfully tagged scouter.new.collector:v1

# 스카우터 태그 걸어주기 (원격repo에 올리기 위해 주소를 입력해준다)
[root@server1 scouter]# docker tag scouter.new.collector:v1 dev.hjchoi.com:5000/scouter_collect

# 스카우터 Push
[root@server1 scouter]# docker push dev.hjchoi.com:5000/scouter_collect
Using default tag: latest
The push refers to repository [dev.hjchoi.com:5000/scouter_collect]
547354b99216: Pushed
12ba11098fbc: Pushed
9e38cf6d5709: Pushed
bb320a515ae3: Pushed
93c295bfca14: Pushed
5e2d26c2989e: Pushed
59f6800c2f4d: Pushed
78dc769fb7f8: Pushed
9de6398ddf80: Pushed
c94f7d130b68: Mounted from inclination.api
02abab379664: Mounted from inclination.api
e47c408dca64: Mounted from inclination.api
942ed04341d9: Mounted from inclination.api
164c75b97a29: Mounted from inclination.api
3b766b5f5419: Mounted from inclination.api
0a17e78456a4: Mounted from inclination.api
c8e0c8598800: Mounted from inclination.api
73ca1a428e16: Mounted from inclination.api
4716c77a7a05: Mounted from inclination.api
f690b4d858ba: Mounted from inclination.api
9dd914824497: Mounted from inclination.api
174f56854903: Mounted from inclination.api
latest: digest: sha256:c49d24f5515deda2b8e9283e9fee8dd310b0f719108da883a21ebe00c00ea965 size: 4940

# Push 후 원하는 서버에서 이미지 pull 한 후 실행
[root@server2 ~]# docker pull dev.hjchoi.com:5000/scouter_collect

# 빌드된 이미지 실행 명령어
[root@server2 ~]# docker run -it --name scouter_collector -p 6180:6180 -p 6100:6100 -p 6100:6100/udp -d scouter.collector:v1
- 포트의 경우 Dockerfile에 넣었었지만 호스트와 포트 연결이 되지 않았으며, run 명령어 실행 시 -P 옵션을 주면 되지만 그럴 경우 호스트의 랜덤포트를 선택해서 뺌.

```

docker 명령어들은 아래와 같다.

docker ps: 현재 실행중인 컨테이너 확인\
docker stop [컨테이너 ID]: 실행중인 컨테이너 종료\
docker rm [컨테이너 ID]: 컨테이너 삭제\
docker images: 현재 docker에 있는 이미지 확인\
docker rmi [이미지 ID]: 이미지 삭제\
docker build [옵션]: docker 컨테이너 이미지 빌드 (생성)

- 옵션: [https://docs.docker.com/engine/reference/commandline/build/#options](https://docs.docker.com/engine/reference/commandline/build/#options)

docker run [옵션] 이미지명:태그명 : docker 컨테이너 이미지 실행

- 옵션: [https://docs.docker.com/engine/reference/commandline/run/#options](https://docs.docker.com/engine/reference/commandline/run/#options)

docker run -it -v /app/docker/scouter/scouter-paper:/home/scouter-server/extweb --name scouter_collector -p 6180:6180 -p 6100:6100 -p 6100:6100/udp -d scouter.collector:v1

-v 옵션: [호스트 경로]:[컨테이너 경로]

docker tag [원래 이름] [변경 이름]: docker 태그 걸어주기 (사설 repo에 배포시에도 사용)\
docker push [이름]: docker 이미지 push (이름에 사설 repo URL 이 있으면 해당 서버에 올라감.)\
docker pull [이름]: docker 이미지 pull (이름에 사설 repo URL 이 있으면 해당 서버에서 받음.)

각 프로젝트 별로 scouter 연동을 위해 Dockerfile 수정
```bash
FROM dev.hjchoi.com:5000/java.app.env14:latest

USER app
RUN mkdir /pgms/service
ADD ./service.jar /pgms/service/

# Java 환경 설정
ENV JAVA_HOME /app/jdk/14.0.1
ENV PATH $PATH:$JAVA_HOME/bin

# scouter 환경 설정
ENV SCOUTER_DIR /app/scouter/2.15.0
ENV SCOUTER_NAME service-application (각 시스템에 맞는 이름 설정)
ENV JAVA_AGENT /app/scouter/2.15.0/agent.java/scouter.agent.jar
ENV SCOUTER_CONF /app/scouter/2.15.0/agent.java/conf/scouter.conf

USER root

RUN yum install -y fontconfig libfreetype6

# 스카우터 설정 (IP, 포트 등)
RUN echo 'net_collector_ip=192.168.100.10' >> /app/scouter/2.15.0/agent.host/conf/scouter.conf
RUN echo 'net_collector_udp_port=6100' >> /app/scouter/2.15.0/agent.host/conf/scouter.conf
RUN echo 'net_collector_tcp_port=6100' >> /app/scouter/2.15.0/agent.host/conf/scouter.conf

RUN echo 'trace_http_client_ip_header_key=X-Forwarded-For' >> /app/scouter/2.15.0/agent.java/conf/scouter.conf
RUN echo 'profile_http_parameter_enabled=true' >> /app/scouter/2.15.0/agent.java/conf/scouter.conf
RUN echo 'profile_http_header_enabled=true' >> /app/scouter/2.15.0/agent.java/conf/scouter.conf
RUN echo 'xlog_lower_bound_time_ms=10' >> /app/scouter/2.15.0/agent.java/conf/scouter.conf

# 스카우터 연동을 위한 실행 명령어 추가
CMD ["sh", "-c", "source /etc/profile && cd /app/scouter/2.15.0/agent.host && ./host.sh && java -javaagent:$JAVA_AGENT -Dscouter.config=$SCOUTER_CONF -Dobj_name=$SCOUTER_NAME -Dfile.encoding=UTF-8 -jar -Dserver.port=8080 /pgms/service/service.application.jar"]
```

![Untitled.png](images/scouter_paper.png)

[https://github.com/scouter-contrib/scouter-docker-image/tree/master/scouter-server](https://github.com/scouter-contrib/scouter-docker-image/tree/master/scouter-server)

[https://5equal0.tistory.com/entry/Docker-Registry-사설-원격-레지스트리-만들기](https://5equal0.tistory.com/entry/Docker-Registry-%EC%82%AC%EC%84%A4-%EC%9B%90%EA%B2%A9-%EB%A0%88%EC%A7%80%EC%8A%A4%ED%8A%B8%EB%A6%AC-%EB%A7%8C%EB%93%A4%EA%B8%B0)