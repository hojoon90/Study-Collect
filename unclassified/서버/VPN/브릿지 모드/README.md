# Softether VPN 브릿지 모드 세팅

이 항목을 읽기 전에 서버 세팅을 먼저 읽고 오는 것을 추천함.

* 브릿지 모드 세팅 이미지 추가(관리툴)


```shell
[root@localhost ~]# ifconfig
enp0s3: flags=4419<UP,BROADCAST,RUNNING,PROMISC,MULTICAST>  mtu 1500
        inet 192.168.50.250  netmask 255.255.255.0  broadcast 192.168.50.255
        inet6 fe80::a00:27ff:fe0f:5bea  prefixlen 64  scopeid 0x20<link>
        ether 08:00:27:0f:5b:ea  txqueuelen 1000  (Ethernet)
        RX packets 1753265  bytes 354563460 (338.1 MiB)
        RX errors 0  dropped 90  overruns 0  frame 0
        TX packets 1730131  bytes 303076445 (289.0 MiB)
        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0

lo: flags=73<UP,LOOPBACK,RUNNING>  mtu 65536
        inet 127.0.0.1  netmask 255.0.0.0
        inet6 ::1  prefixlen 128  scopeid 0x10<host>
        loop  txqueuelen 1000  (Local Loopback)
        RX packets 68  bytes 31027 (30.2 KiB)
        RX errors 0  dropped 0  overruns 0  frame 0
        TX packets 68  bytes 31027 (30.2 KiB)
        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0

tap_soft: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
        inet6 fe80::5c1e:f4ff:fe99:80cb  prefixlen 64  scopeid 0x20<link>
        ether 5e:1e:f4:99:80:cb  txqueuelen 1000  (Ethernet)
        RX packets 26  bytes 1224 (1.1 KiB)
        RX errors 0  dropped 0  overruns 0  frame 0
        TX packets 14  bytes 1148 (1.1 KiB)
        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0
```

```shell
[root@localhost ~]# dnf install dnsmasq
```


```shell
[root@localhost ~]# vi /etc/dnsmasq.conf

# 맨 아래에 다음 내용 추가
interface=tap_soft
dhcp-range=tap_soft,192.168.7.50,192.168.7.60,12h
dhcp-option=tap_soft,3,192.168.7.1
```


```shell
[root@localhost ~]# vi /etc/init.d/vpnserver

#아래 내용 추가
#!/bin/sh
### BEGIN INIT INFO
# Provides:          vpnserver
# Required-Start:    $remote_fs $syslog
# Required-Stop:     $remote_fs $syslog
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: Start daemon at boot time
# Description:       Enable Softether by daemon.
### END INIT INFO

DAEMON=/svc/vpnserver/vpnserver
LOCK=/var/lock/subsys/vpnserver
TAP_ADDR=192.168.7.1
test -x $DAEMON || exit 0
case "$1" in
start)
$DAEMON start
touch $LOCK
sleep 1
/sbin/ifconfig tap_soft $TAP_ADDR
;;
stop)
$DAEMON stop
rm $LOCK
;;
restart)
$DAEMON stop
sleep 3
$DAEMON start
sleep 1
/sbin/ifconfig tap_soft $TAP_ADDR
;;
*)
echo "Usage: $0 {start|stop|restart}"
exit 1
esac
exit 0
```


```shell
[root@localhost ~]# vi /etc/sysctl.conf

# 아래 내용 추가
net.ipv4.ip_forward=1
```

```shell
[root@localhost ~]# sysctl --system
```

```shell
[root@localhost ~]# iptables -t nat -A POSTROUTING -s 192.168.7.0/24 -j SNAT --to-source 192.168.50.250
```

https://jamong1014.tistory.com/47
https://blog.kerus.net/690/setup-softether-vpn-local-bridge/