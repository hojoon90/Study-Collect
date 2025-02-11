# Rocky Linux Docker 설치

Rocky Linux 설치 후 개발에 필요한 MariaDB, Jenkins와 실제 제공할 프로젝트 서비스 제공을 위해 Docker 설치를 진행하려 한다. CentOS와 거의 유사하지만
일부 부분에서 다르기 때문에 한번 정리를 해보려 한다.

#### 참고 URL
* <https://docs.docker.com/engine/install/fedora/#set-up-the-repository>
* <https://svrforum.com/os/366485>

### Docker 설치

먼저 dnf-plugins-core 패키지(DNF 레포지토리 관리 명령 제공)를 설치 하고 레포지토리를 설정한다.

```shell
[root@localhost ~]# dnf -y install dnf-plugins-core
Last metadata expiration check: 2:34:01 ago on Sat 24 Sep 2022 07:51:03 PM KST.
Package dnf-plugins-core-4.0.24-4.el9_0.noarch is already installed.
Dependencies resolved.
Nothing to do.
Complete!
[root@localhost ~]#
[root@localhost ~]# dnf config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
Adding repo from: https://download.docker.com/linux/centos/docker-ce.repo
```

도커 설치에 필요한 파일 3개를 다운받는다.
```shell
[root@localhost ~]# dnf install docker-ce docker-ce-cli containerd.io
Docker CE Stable - x86_64                                                                   1.6 kB/s | 8.7 kB     00:05
Dependencies resolved.
============================================================================================================================
 Package                               Architecture       Version                        Repository                    Size
============================================================================================================================
Installing:
 containerd.io                         x86_64             1.6.8-3.1.el9                  docker-ce-stable              32 M
 docker-ce                             x86_64             3:20.10.18-3.el9               docker-ce-stable              20 M
 docker-ce-cli                         x86_64             1:20.10.18-3.el9               docker-ce-stable              29 M
Installing dependencies:
 container-selinux                     noarch             3:2.188.0-1.el9_0              appstream                     47 k
 docker-ce-rootless-extras             x86_64             20.10.18-3.el9                 docker-ce-stable             3.7 M
 fuse-common                           x86_64             3.10.2-5.el9.0.1               baseos                       8.1 k
 fuse-overlayfs                        x86_64             1.9-1.el9_0                    appstream                     71 k
 fuse3                                 x86_64             3.10.2-5.el9.0.1               appstream                     53 k
 fuse3-libs                            x86_64             3.10.2-5.el9.0.1               appstream                     91 k
 libslirp                              x86_64             4.4.0-7.el9                    appstream                     68 k
 slirp4netns                           x86_64             1.2.0-2.el9_0                  appstream                     46 k
Installing weak dependencies:
 docker-scan-plugin                    x86_64             0.17.0-3.el9                   docker-ce-stable             3.6 M

Transaction Summary
============================================================================================================================
Install  12 Packages
...
Installed:
  container-selinux-3:2.188.0-1.el9_0.noarch                        containerd.io-1.6.8-3.1.el9.x86_64
  docker-ce-3:20.10.18-3.el9.x86_64                                 docker-ce-cli-1:20.10.18-3.el9.x86_64
  docker-ce-rootless-extras-20.10.18-3.el9.x86_64                   docker-scan-plugin-0.17.0-3.el9.x86_64
  fuse-common-3.10.2-5.el9.0.1.x86_64                               fuse-overlayfs-1.9-1.el9_0.x86_64
  fuse3-3.10.2-5.el9.0.1.x86_64                                     fuse3-libs-3.10.2-5.el9.0.1.x86_64
  libslirp-4.4.0-7.el9.x86_64                                       slirp4netns-1.2.0-2.el9_0.x86_64

Complete!
```

설치가 완료됐으면 docker 를 실행해준다.
```shell
[root@localhost ~]# systemctl start docker
```

설치가 제대로 됐는지 확인하기 위해 아래 명령어를 입력한다.
```shell
[root@localhost ~]# docker run hello-world
Unable to find image 'hello-world:latest' locally
latest: Pulling from library/hello-world
2db29710123e: Pull complete
Digest: sha256:62af9efd515a25f84961b70f973a798d2eca956b1b2b026d0a4a63a3b0b6a3f2
Status: Downloaded newer image for hello-world:latest

Hello from Docker!
This message shows that your installation appears to be working correctly.

To generate this message, Docker took the following steps:
 1. The Docker client contacted the Docker daemon.
 2. The Docker daemon pulled the "hello-world" image from the Docker Hub.
    (amd64)
 3. The Docker daemon created a new container from that image which runs the
    executable that produces the output you are currently reading.
 4. The Docker daemon streamed that output to the Docker client, which sent it
    to your terminal.

To try something more ambitious, you can run an Ubuntu container with:
 $ docker run -it ubuntu bash

Share images, automate workflows, and more with a free Docker ID:
 https://hub.docker.com/

For more examples and ideas, visit:
 https://docs.docker.com/get-started/

[root@localhost ~]#
```
위와 같이 메세지가 나오면 정상 설치된 것이다.

재부팅시 자동 실행되기 위해 아래 명령어를 입력해준다.
```shell
[root@localhost ~]# systemctl enable docker
Created symlink /etc/systemd/system/multi-user.target.wants/docker.service → /usr/lib/systemd/system/docker.service.
```

여기까지 진행했으면 docker 설치가 완료된 것이다.


### Portainer 설치
Portainer는 도커 관리를 위한 GUI툴로서, 명령어로 모든걸 처리해야 하는 도커 특성상 굉장히 유용한 툴이다. 도커 이미지 관리뿐만 아니라 이미지 내부에 접속해
여러가지를 확인할 수 있는 오픈소스 툴이다.

난 볼륨매칭용 데이터 디렉토리를 만든 후 Portainer 처리를 진행하였다.
명령어를 입력하면 알아서 이미지 다운로드 후 실행을 진행한다.
```shell
[root@localhost ~]# docker run -d -p 8000:8000 -p 9443:9443 --name portainer --restart=always -v /var/run/docker.sock:/var/run/docker.sock -v portainer_data:/data portainer/portainer-ce:latest
Unable to find image 'portainer/portainer-ce:latest' locally
latest: Pulling from portainer/portainer-ce
772227786281: Pull complete
96fd13befc87: Pull complete
884dbe464e55: Pull complete
f60f1fd54bd7: Pull complete
Digest: sha256:5466af30b8eaf3f75edd3c74703d1c9973f0963acd6ef164913ea6f195d640c2
Status: Downloaded newer image for portainer/portainer-ce:latest
2127e40a40e92b24677ec32693785819e516a1520937556d5e1fb92123de320d
```
-- 25.2.11 수정
포트가 8000에서 9000으로 변경
```shell
[root@localhost ~]# docker run -d -p 9000:9000 -p 9443:9443 --name portainer --restart=always -v /var/run/docker.sock:/var/run/docker.sock -v portainer_data:/data portainer/portainer-ce:latest
Unable to find image 'portainer/portainer-ce:latest' locally
latest: Pulling from portainer/portainer-ce
772227786281: Pull complete
96fd13befc87: Pull complete
884dbe464e55: Pull complete
f60f1fd54bd7: Pull complete
Digest: sha256:5466af30b8eaf3f75edd3c74703d1c9973f0963acd6ef164913ea6f195d640c2
Status: Downloaded newer image for portainer/portainer-ce:latest
2127e40a40e92b24677ec32693785819e516a1520937556d5e1fb92123de320d
```


실행 후 https://{접근 IP}:9443 을 웹브라우저에 입력하면 아래와 같이 초기 생성화면이 나타난다.
![portainer_main.png](images/portainer_main.png)

암호 세팅 후 접속하면 아래와 같은 화면이 나타난다.
Get Started를 눌러준다.
![dashboard.png](images/dashboard.png)

그럼 다음과 같이 자신의 로컬페이지 홈 화면으로 이동하게 된다. 아래 local 을 눌러주자.
![getstart.png](images/getstart.png)

그럼 local의 서버 상태와 이미지 등의 여러 정보들이 나타나게 된다. 여기서 이미지 접근 혹은 새롭게 생성등을 할 수 있다.
![info.png](images/info.png)
