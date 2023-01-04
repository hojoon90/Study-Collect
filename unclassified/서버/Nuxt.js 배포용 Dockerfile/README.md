# Nuxt.js 배포용 Dockerfile 

### 목표
Nuxt.js 프로젝트를 Docker image로 생성하여 Docker에 배포

### 확인사항
- Nuxt.js의 버전은 3 (Nuxt3)
- Nuxt.js SSR 형식의 배포를 위한 베이스 이미지 설정.
- 추후 nginx 가 추가될 수 있으므로 이에 대한 적용 방안도 함께 고려
- 띄워둔 Nuxt.js 가 모종의 이유로 비정상 종료될 시 컨테이너가 종료되지 않도록 처리
- 패키지 매니저는 yarn을 사용한다.

### 테스트용 Nuxt.js 프로젝트 생성
Dockerfile 생성 시 사용할 Nuxt.js 프로젝트를 하나 생성해준다.
```shell
yarn create nuxt-app <project-name>
```

### Dockerfile 생성
Docker 이미지 생성시 필요한 Dockerfile을 먼저 만들어준다.
```shell
#Dockerfile 생성
touch Dockerfile

or

vi Dockerfile
```
Nuxt.js는 기본적으로 node가 실행될 수 있는 환경에서 실행된다. 따라서 정적 사이트일 때와 SSR 일때의 이미지 처리가 다르다.\
여기서는 SSR일때의 처리를 진행한다. (-static 페이지에 대한 처리는 아래 참고 URL에서 확인.)\
추후 nginx도 적용될 수 있으므로 편하게 nginx 이미지를 베이스로 Dockerfile을 생성한다.
```shell
FROM nginx
# node 설치를 위한 npm 설치
RUN apt-get update && apt-get install npm -y
# yarn, n 설치
RUN npm i -g yarn && npm i -g n
# node 버전 변경 및 확인.
RUN n 16.19.0
RUN node -v

#추후 nginx 사용시 필요한 설정.
#COPY ./default.conf /etc/nginx/conf.d/default.conf

WORKDIR /app

#추후 이미지 빌드 시 cache 사용을 위해 패키지 선 설치
COPY ./package*.json .
# 모듈 설치
RUN yarn install

# 소스 복사
COPY . .

ENV HOST 0.0.0.0
EXPOSE 3000

# 빌드
RUN yarn run build

# 컨테이너 실행.
CMD ["sh", "-c", "yarn start && service nginx start"]
```
위 처럼 npm 설치 시 node 버전은 기본적으로 12버전이 설치됨. (12.22.1 or 12.22.2)\
Nuxt 3 는 14, 16, 18, 19버전을 지원한다. 
> On the server side, Nuxt 3 supports Node.js 14, 16, 18, and 19 at the moment. ...\
> (https://nuxt.com/v3#the-browser-and-nodejs-support)

버전 변경이 필요하므로 n 을 설치하여 버전을 변경해준다.

Docker file build
```shell
docker build docker build -t <tag name> -f <Dockerfile 경로>
```

빌드한 이미지 실행
```shell
docker run -it -d --name <실행할 이름> -p 3000:3000 <tag name>
```

### 비정상 종료에 대한 테스트

특정 페이지를 호출하면 Array에 빈 객체를 push하여 메모리 누수로 앱이 강제로 종료되도록 코드 추가
```javascript
<template>
  <div v-model="this.memoryLeak()"></div>
  <h1>test</h1>
</template>

<script>
export default {
  name: "testPage",
  data: () => ({
    title: "My App",
    description: "My App Description",
    memory : [],
    i: 0,
  }),
  methods:{
    memoryLeak(){
      while(true){
        this.i = this.i+1;
        this.memory.push({});
        if(this.i % 10000000 == 0){
          console.log("process "+this.i+" completed.");
        }
      }
    },
  }
};
</script>

<style lang="scss">
</style>
```

페이지 실행 시 메모리 누수로 인해 앱이 강제 종료되는 현상 확인.

```shell
ℹ Listening on: http://172.17.0.3:3000/
process 10000000 completed.
<--- Last few GCs --->
[18:0x4d70bd0]    48535 ms: Scavenge (reduce) 917.7 (932.8) -> 917.7 (933.5) MB, 6.5 / 0.0 ms  (average mu = 0.306, current mu = 0.219) allocation failure 
[18:0x4d70bd0]    48720 ms: Mark-sweep (reduce) 918.5 (933.5) -> 918.4 (934.5) MB, 65.4 / 0.0 ms  (+ 703.5 ms in 99 steps since start of marking, biggest step 43.6 ms, walltime since start of marking 915 ms) (average mu = 0.311, current mu = 0.316) alloca
<--- JS stacktrace --->
FATAL ERROR: Reached heap limit Allocation failed - JavaScript heap out of memory
 1: 0xb08e80 node::Abort() [node /app/node_modules/nuxt/bin/nuxt.js]
 2: 0xa1b70e  [node /app/node_modules/nuxt/bin/nuxt.js]
 3: 0xce1890 v8::Utils::ReportOOMFailure(v8::internal::Isolate*, char const*, bool) [node /app/node_modules/nuxt/bin/nuxt.js]
 4: 0xce1c37 v8::internal::V8::FatalProcessOutOfMemory(v8::internal::Isolate*, char const*, bool) [node /app/node_modules/nuxt/bin/nuxt.js]
 5: 0xe992a5  [node /app/node_modules/nuxt/bin/nuxt.js]
 6: 0xea8f6d v8::internal::Heap::CollectGarbage(v8::internal::AllocationSpace, v8::internal::GarbageCollectionReason, v8::GCCallbackFlags) [node /app/node_modules/nuxt/bin/nuxt.js]
 7: 0xeabc6e v8::internal::Heap::AllocateRawWithRetryOrFailSlowPath(int, v8::internal::AllocationType, v8::internal::AllocationOrigin, v8::internal::AllocationAlignment) [node /app/node_modules/nuxt/bin/nuxt.js]
 8: 0xe6d1aa v8::internal::Factory::NewFillerObject(int, bool, v8::internal::AllocationType, v8::internal::AllocationOrigin) [node /app/node_modules/nuxt/bin/nuxt.js]
 9: 0x11e5f96 v8::internal::Runtime_AllocateInYoungGeneration(int, unsigned long*, v8::internal::Isolate*) [node /app/node_modules/nuxt/bin/nuxt.js]
10: 0x15d9c19  [node /app/node_modules/nuxt/bin/nuxt.js]
```

### PM2 설치
PM2는 간단히 이야기해서 로드 밸런서가 내장 된 Node.js 애플리케이션의 프로덕션 프로세스 관리자.\
Nuxt에 PM2 적용방법은 아래 URL 참고\
https://nuxtjs.org/deployments/pm2/

root 디렉토리에 ecosystem.config.js 파일 생성 후 아래와 같이 입력
```shell
module.exports = {
  apps: [
    {
      name: 'NuxtAppName',
      script: 'yarn',
      exec_mode: 'fork',
      instances: 1, // Or 'max'
      autorestart: true,
      script: './node_modules/nuxt/bin/nuxt.js',
      args: 'start'
    }
  ]
}
```
instance는 1개, fork 모드로 실행하도록 설정.

Dockerfile에 의존성 및 pm2 실행을 위한 세팅 추가
```shell
FROM nginx
# node 설치를 위한 npm 설치
RUN apt-get update && apt-get install npm -y
# yarn, n, pm2 설치
RUN npm i -g yarn && npm i -g n && npm i -g pm2
# node 버전 변경 및 확인.
RUN n 16.19.0
RUN node -v

#추후 nginx 사용시 필요한 설정.
#COPY ./default.conf /etc/nginx/conf.d/default.conf

WORKDIR /app

#추후 이미지 빌드 시 cache 사용을 위해 패키지 선 설치
COPY ./package*.json .
# 모듈 설치
RUN yarn install

# 소스 복사
COPY . .

ENV HOST 0.0.0.0
EXPOSE 3000

# 빌드
RUN yarn run build

# 컨테이너 실행. pm2의 경우 foreground 실행을 위해 pm2-runtime으로 실행.
CMD ["sh", "-c", "pm2-runtime start ecosystem.config.js && service nginx start"]
```
Dockerfile 다시 빌드 후 run

동일한 테스트 재진행시 정상적으로 node가 restart하는 것을 확인할 수 있음.
```shell
ℹ Listening on: http://172.17.0.3:3000/
process 10000000 completed.
<--- Last few GCs --->
[18:0x4d70bd0]    48535 ms: Scavenge (reduce) 917.7 (932.8) -> 917.7 (933.5) MB, 6.5 / 0.0 ms  (average mu = 0.306, current mu = 0.219) allocation failure 
[18:0x4d70bd0]    48720 ms: Mark-sweep (reduce) 918.5 (933.5) -> 918.4 (934.5) MB, 65.4 / 0.0 ms  (+ 703.5 ms in 99 steps since start of marking, biggest step 43.6 ms, walltime since start of marking 915 ms) (average mu = 0.311, current mu = 0.316) alloca
<--- JS stacktrace --->
FATAL ERROR: Reached heap limit Allocation failed - JavaScript heap out of memory
 1: 0xb08e80 node::Abort() [node /app/node_modules/nuxt/bin/nuxt.js]
 2: 0xa1b70e  [node /app/node_modules/nuxt/bin/nuxt.js]
 3: 0xce1890 v8::Utils::ReportOOMFailure(v8::internal::Isolate*, char const*, bool) [node /app/node_modules/nuxt/bin/nuxt.js]
 4: 0xce1c37 v8::internal::V8::FatalProcessOutOfMemory(v8::internal::Isolate*, char const*, bool) [node /app/node_modules/nuxt/bin/nuxt.js]
 5: 0xe992a5  [node /app/node_modules/nuxt/bin/nuxt.js]
 6: 0xea8f6d v8::internal::Heap::CollectGarbage(v8::internal::AllocationSpace, v8::internal::GarbageCollectionReason, v8::GCCallbackFlags) [node /app/node_modules/nuxt/bin/nuxt.js]
 7: 0xeabc6e v8::internal::Heap::AllocateRawWithRetryOrFailSlowPath(int, v8::internal::AllocationType, v8::internal::AllocationOrigin, v8::internal::AllocationAlignment) [node /app/node_modules/nuxt/bin/nuxt.js]
 8: 0xe6d1aa v8::internal::Factory::NewFillerObject(int, bool, v8::internal::AllocationType, v8::internal::AllocationOrigin) [node /app/node_modules/nuxt/bin/nuxt.js]
 9: 0x11e5f96 v8::internal::Runtime_AllocateInYoungGeneration(int, unsigned long*, v8::internal::Isolate*) [node /app/node_modules/nuxt/bin/nuxt.js]
10: 0x15d9c19  [node /app/node_modules/nuxt/bin/nuxt.js]

2023-01-03T08:51:26: PM2 log: App [NuxtAppName:0] exited with code [0] via signal [SIGABRT]
2023-01-03T08:51:26: PM2 log: App [NuxtAppName:0] starting in -fork mode-
2023-01-03T08:51:26: PM2 log: App [NuxtAppName:0] online
ℹ Listening on: http://172.17.0.3:3000/
```


