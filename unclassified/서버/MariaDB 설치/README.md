# MariaDB 설치 on Docker

이번엔 MariaDB를 Docker에 올려 사용하는 법에 대해 작성해본다. Docker로 인해 서버 내부에 직접 설치할 필요 없이 컨테이너 이미지만 올려
MariaDB를 사용할 수 있기 때문에 사용하기 굉장히 편해졌다고 볼 수 있다. 바로 설치방법으로 넘어가보자.

먼저 서버 내부에서 Docker를 이용해 MariaDB 를 다운로드 한다.
```shell
[root@localhost ~]# docker pull mariadb
Using default tag: latest
latest: Pulling from library/mariadb
2b55860d4c66: Pull complete
4bf944e49ffa: Pull complete
020ff2b6bb0b: Pull complete
977397ae9bc6: Pull complete
b361cf449d40: Pull complete
21d261950157: Pull complete
296a47dd9435: Pull complete
bbe841bf5cfe: Pull complete
758db05dd921: Pull complete
9c2c0a21c9e6: Pull complete
4bc311b9359a: Pull complete
Digest: sha256:05b53c3f7ebf1884f37fe9efd02da0b7faa0d03e86d724863f3591f963de632c
Status: Downloaded newer image for mariadb:latest
docker.io/library/mariadb:latest
[root@localhost ~]#
```
위 명령어를 이용하면 Docker 이미지중 가장 최신 이미지를 내려받게 된다.\
도커 이미지를 받았으면 실행 시켜보자
```shell
[root@localhost data]# docker run --name mariadb -d -p 3306:3306 -e MARIADB_ROOT_PASSWORD=root -v /data/mariadb:/var/lib/mariadb mariadb
bfeb597dd2cb373882bb90fb42b26de8019e037840c5ffe9e87a49e67d7ac29f
```
여기서 사용한 옵션은 다음과 같다.\
--name mariadb : 컨테이너의 이름을 'mariadb'로 지정\
-d(--detach) : 백그라운드 실행\
-p : 포트 설정 (호스트 포트:Docker내부포트)\
-e : 환경변수 (여기서는 mariadb root 비밀번호)\
-v : 볼륨 마운트 (호스트 볼륨:Docker내부볼륨, MariaDB와 같은 데이터베이스들은 컨테이너가 재기동 되면 데이터가 다 날아감. 그래서 호스트 서버의 볼륨에 데이터를 저장하도록 세팅을 해줌.)

정상적으로 실행되었다면 docker ps 를 이용해 실제로 DB가 실행되었는지 확인한다.
```shell
CONTAINER ID   IMAGE       COMMAND                  CREATED         STATUS         PORTS                                            NAMES
bfeb597dd2cb   mariadb     "docker-entrypoint.s…"   3 minutes ago   Up 3 minutes   0.0.0.0:3306->3306/tcp, :::3306->3306/tcp        mariadb
```

정상적으로 실행되었다면 해당 컨테이너 안으로 들어가서 인코딩 수정을 해주어야 한다. Docker에서 받아오는 MariaDB는 기본적으로 클라이언트 부분 인코딩이 latin1
로 설정되어 있는데, 이부분을 변경해주어야 한다. 컨테이너에 접속해보자.
```shell
[root@localhost data]# docker exec -it mariadb /bin/bash
root@bfeb597dd2cb:/#
root@bfeb597dd2cb:/# mysql -u root -p
Enter password:
Welcome to the MariaDB monitor.  Commands end with ; or \g.
Your MariaDB connection id is 3
Server version: 10.9.3-MariaDB-1:10.9.3+maria~ubu2204 mariadb.org binary distribution

Copyright (c) 2000, 2018, Oracle, MariaDB Corporation Ab and others.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

MariaDB [(none)]>
```
접속 후 mariadb 까지 접속하면 위와 같이 mariadb cli 가 나타나게 된다. 여기서 status를 입력해보자
```shell
MariaDB [(none)]> status
--------------
mysql  Ver 15.1 Distrib 10.9.3-MariaDB, for debian-linux-gnu (x86_64) using  EditLine wrapper

Connection id:		3
Current database:
Current user:		root@localhost
SSL:			Not in use
Current pager:		stdout
Using outfile:		''
Using delimiter:	;
Server:			MariaDB
Server version:		10.9.3-MariaDB-1:10.9.3+maria~ubu2204 mariadb.org binary distribution
Protocol version:	10
Connection:		Localhost via UNIX socket
Server characterset:	utf8mb4
Db     characterset:	utf8mb4
Client characterset:	latin1
Conn.  characterset:	latin1
UNIX socket:		/run/mysqld/mysqld.sock
Uptime:			5 min 4 sec

Threads: 1  Questions: 4  Slow queries: 0  Opens: 17  Open tables: 10  Queries per second avg: 0.013
--------------

MariaDB [(none)]>
```
클라이언트 쪽 세팅 수정을 위해 /etc/mysql/my.conf 를 수정한다. 그전에 파일 수정을 위해 이미지 내부에 vim을 설치한다. 설치를 위해 OS를 확인해준다.
```shell
root@bfeb597dd2cb:/# cat /etc/*-release
DISTRIB_ID=Ubuntu
DISTRIB_RELEASE=22.04
DISTRIB_CODENAME=jammy
DISTRIB_DESCRIPTION="Ubuntu 22.04.1 LTS"
PRETTY_NAME="Ubuntu 22.04.1 LTS"
NAME="Ubuntu"
VERSION_ID="22.04"
VERSION="22.04.1 LTS (Jammy Jellyfish)"
VERSION_CODENAME=jammy
ID=ubuntu
ID_LIKE=debian
HOME_URL="https://www.ubuntu.com/"
SUPPORT_URL="https://help.ubuntu.com/"
BUG_REPORT_URL="https://bugs.launchpad.net/ubuntu/"
PRIVACY_POLICY_URL="https://www.ubuntu.com/legal/terms-and-policies/privacy-policy"
UBUNTU_CODENAME=jammy
```
우분투 이므로 apt-get을 이용하여 다운 받으면 된다.
```shell
# apt-get update
root@bfeb597dd2cb:/# apt-get update

#vim download
root@bfeb597dd2cb:/# apt-get install vim
```

설치 후 파일을 열면 아래와 같이 나오게 된다.
```shell
# The MariaDB configuration file
#
# The MariaDB/MySQL tools read configuration files in the following order:
# 0. "/etc/mysql/my.cnf" symlinks to this file, reason why all the rest is read.
# 1. "/etc/mysql/mariadb.cnf" (this file) to set global defaults,
# 2. "/etc/mysql/conf.d/*.cnf" to set global options.
# 3. "/etc/mysql/mariadb.conf.d/*.cnf" to set MariaDB-only options.
# 4. "~/.my.cnf" to set user-specific options.
#
# If the same option is defined multiple times, the last one will apply.
#
# One can use all long options that the program supports.
# Run program with --help to get a list of available options and with
# --print-defaults to see which it would actually understand and use.
#
# If you are new to MariaDB, check out https://mariadb.com/kb/en/basic-mariadb-articles/

#
# This group is read both by the client and the server
# use it for options that affect everything
#
[client-server]
# Port or socket location where to connect
# port = 3306
socket = /run/mysqld/mysqld.sock

# Import all .cnf files from configuration directory
[mariadbd]
skip-host-cache
skip-name-resolve

!includedir /etc/mysql/mariadb.conf.d/
!includedir /etc/mysql/conf.d/
```

맨 아래 부분에 다음 내용을 추가한다.
```shell
[client]
default-character-set = utf8mb4

[mysql]
default-character-set = utf8mb4

[mysqld]
character-set-client-handshake = FALSE
character-set-server = utf8mb4
collation-server = utf8mb4_unicode_ci
```
추가해주었으면 설정 적용을 위해 컨테이너를 재시작해준다.
```shell
root@bfeb597dd2cb:/# exit
exit
[root@localhost data]# docker restart mariadb
mariadb
[root@localhost data]#
```

그 후 확인해보면 정상적으로 변경된 것을 볼 수 있다.
```shell
MariaDB [(none)]> status
--------------
mysql  Ver 15.1 Distrib 10.9.3-MariaDB, for debian-linux-gnu (x86_64) using  EditLine wrapper

Connection id:		4
Current database:
Current user:		root@localhost
SSL:			Not in use
Current pager:		stdout
Using outfile:		''
Using delimiter:	;
Server:			MariaDB
Server version:		10.9.3-MariaDB-1:10.9.3+maria~ubu2204 mariadb.org binary distribution
Protocol version:	10
Connection:		Localhost via UNIX socket
Server characterset:	utf8mb4
Db     characterset:	utf8mb4
Client characterset:	utf8mb4
Conn.  characterset:	utf8mb4
UNIX socket:		/run/mysqld/mysqld.sock
Uptime:			59 sec

Threads: 1  Questions: 4  Slow queries: 0  Opens: 17  Open tables: 10  Queries per second avg: 0.067
--------------

MariaDB [(none)]>
```