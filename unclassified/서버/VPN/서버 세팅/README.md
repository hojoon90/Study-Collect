# 리눅스 서버에 VPN 서버 세팅하기

기존에 Softether VPN 을 이용하여 VPN 세팅을 진행하였다. 윈도우를 그냥 바로 서버로 사용하여 VPN을 실행했는데, 생각보다 CPU 자원을 많이 사용할
뿐만 아니라, CPU 때문인진 모르겠지만 연결 후 시간이 지나면 자동으로 연결이 끊긴 후 다시 연결되지 않는 문제가 발생했다. 

대충 원인을 확인해보니 Softether VPN은 Virtual NAT 라는 가상의 네트워크 인터페이스를 이용해서 DHCP 서버까지 같이 제공을 해준다고 한다. 
이 방법은 사용자가 간단하게 VPN 세팅을 할 수 있지만, 아무래도 가상으로 DHCP서버와 허브까지 같이 동작하다보니 호스트 자원을 상당히 많이 사용하게 되어
속도가 많이 느려지게 된다고 한다. 그래서 보통은 로컬 브릿지 방식을 이용해서 세팅을 진행하는데, 이방법은 속도는 잘 나오지만 초반 세팅이 조금 복잡하다고 한다.

기왕 이렇게 된거 서버에 VPN을 설치하면서, 로컬 브릿지까지 같이 세팅하는 방법을 작성해볼까 한다. 일단 여기서는 설치 및 실행까지만 다뤄본다.

#### 참고 URL: <https://damoa-nawa.tistory.com/89>

### 세팅환경
세팅은 Rocky Linux 9 버전에서 진행한다. 기존 CentOS를 거의 동일할 뿐만 아니라, 요즘 이 리눅스버전을 사용하는 곳이 늘어나고 있어서 여기에 세팅을
진행해본다.

### 설치
 -- 작성할 내용
* 파일 다운로드
* 필요 라이브러리 다운로드
* 압축풀기
* 설치
* 실행

```shell
# Softehter VPN 서버 시작
[root@localhost vpnserver]# ./vpnserver start
The SoftEther VPN Server service has been started.

Let's get started by accessing to the following URL from your PC:

https://192.168.50.250:5555/
  or
https://192.168.50.250/

Note: IP address may vary. Specify your server's IP address.
A TLS certificate warning will appear because the server uses self signed certificate by default. That is natural. Continue with ignoring the TLS warning.
```

```shell
# 엔터엔터 눌러서 기본 세팅으로 설정
[root@localhost vpnserver]# ./vpncmd
vpncmd command - SoftEther VPN Command Line Management Utility
SoftEther VPN Command Line Management Utility (vpncmd command)
Version 4.38 Build 9760   (English)
Compiled 2021/08/17 22:32:49 by buildsan at crosswin
Copyright (c) SoftEther VPN Project. All Rights Reserved.

By using vpncmd program, the following can be achieved.

1. Management of VPN Server or VPN Bridge
2. Management of VPN Client
3. Use of VPN Tools (certificate creation and Network Traffic Speed Test Tool)

Select 1, 2 or 3: 1

Specify the host name or IP address of the computer that the destination VPN Server or VPN Bridge is operating on.
By specifying according to the format 'host name:port number', you can also specify the port number.
(When the port number is unspecified, 443 is used.)
If nothing is input and the Enter key is pressed, the connection will be made to the port number 8888 of localhost (this computer).
Hostname of IP Address of Destination:

If connecting to the server by Virtual Hub Admin Mode, please input the Virtual Hub name.
If connecting by server admin mode, please press Enter without inputting anything.
Specify Virtual Hub Name:
Connection has been established with VPN Server "localhost" (port 443).

You have administrator privileges for the entire VPN Server.
```

```shell
#서버 패스워드 세팅
VPN Server>ServerPasswordSet
ServerPasswordSet command - Set VPN Server Administrator Password
Please enter the password. To cancel press the Ctrl+D key.

Password: **********
Confirm input: **********


The command completed successfully.

VPN Server>
```

```shell
#SVPN 이라는 허브 생성
VPN Server>HubCreate SVPN
HubCreate command - Create New Virtual Hub
Please enter the password. To cancel press the Ctrl+D key.

Password: **********
Confirm input: **********


The command completed successfully.

VPN Server>
```

```shell
# SVPN 허브로 접속 및 SecureNat 사용 설정
VPN Server>Hub SVPN
Hub command - Select Virtual Hub to Manage
The Virtual Hub "SVPN" has been selected.
The command completed successfully.

VPN Server/SVPN>SecureNatEnable
SecureNatEnable command - Enable the Virtual NAT and DHCP Server Function (SecureNat Function)
The command completed successfully.

VPN Server/SVPN>
```



```shell
# VPN 접속 사용자 세팅
VPN Server/SVPN>UserCreate hjchoi
UserCreate command - Create User
Assigned Group Name:

User Full Name:

User Description:

The command completed successfully.

VPN Server/SVPN>UserPasswordSet hjchoi
UserPasswordSet command - Set Password Authentication for User Auth Type and Set Password
Please enter the password. To cancel press the Ctrl+D key.

Password: **********
Confirm input: **********


The command completed successfully.

VPN Server/SVPN>
```