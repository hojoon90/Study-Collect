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
위 처럼 npm 설치 시 node 버전은 기본적으로 12버전이 설치됨. (12.22.1 or 12.22.2)\
Nuxt 3 는 14, 16, 18, 19버전을 지원한다. 
> On the server side, Nuxt 3 supports Node.js 14, 16, 18, and 19 at the moment. ...\
> (https://nuxt.com/v3#the-browser-and-nodejs-support)

버전 변경이 필요하므로 n 을 설치하여 버전을 변경해준다.

