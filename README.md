```
 ___________ _____   _____ _           _ _                       
/  ___| ___ \  ___| /  __ \ |         | | |                      
\ `--.| |_/ / |__   | /  \/ |__   __ _| | | ___ _ __   __ _  ___ 
 `--. \    /|  __|  | |   | '_ \ / _` | | |/ _ \ '_ \ / _` |/ _ \
/\__/ / |\ \| |___  | \__/\ | | | (_| | | |  __/ | | | (_| |  __/
\____/\_| \_\____/   \____/_| |_|\__,_|_|_|\___|_| |_|\__, |\___|
                                                       __/ |     
                                                      |___/      
```

## Assumptions

I assume you have
1. `docker` running on your box if you want to build and push part 1 of the exercise 
2. `helm` and `kube config` pointing to a vanilla k8s if you want to deploy part 2 of this exercise 
3. (optional) `make` and `kubectl`
4. you might want to change the docker repo I use in my Makefile (`IMAGE=hub.docker.com/layerzerolabs/...`)
5. if you want to expose it, ingress needs some more parametrization depending on ingress controller you will use (f.e. nginx ingress controller, alb ingress controller etc.)

## What is the structure of this repo

1. this is a `Spring Boot` application initialized via https://start.spring.io/ 
2. it is using `Java 21` (the latest LTS), built with `maven`
3. there is a single RESTFul microservice implemented with `JAX-RS` (run by `Jersey`)
4. source code can be found under `/src/main/java/`
5. application properties that the app is using can be found under `/src/main/resources/`
6. unit tests can be found under `/src/test/java/`
7. helm chart can be found under `/deploy/`
8. helm chart was initialized with `helm create` so there is some redundant stuff on the chart
9. there is a `Makefile` that I usually use for my local apps
10. there is a two stage `Dockerfile` that builds (jdk) and runs (jre) the app

## Howto build
```shell
% make build

docker build . -t hub.docker.com/layerzerolabs/digital-currency-exchange-app\:latest
[+] Building 48.9s (13/13) FINISHED                                                                                                                                                                         
 => [internal] load build definition from Dockerfile                                                                                                                                                   0.0s
 => => transferring dockerfile: 37B                                                                                                                                                                    0.0s
 => [internal] load .dockerignore                                                                                                                                                                      0.0s
 => => transferring context: 2B                                                                                                                                                                        0.0s
 => [internal] load metadata for docker.io/library/eclipse-temurin:21-jre                                                                                                                              2.0s
 => [internal] load metadata for docker.io/library/eclipse-temurin:21-jdk                                                                                                                              2.0s
 => [internal] load build context                                                                                                                                                                      0.0s
 => => transferring context: 29.48kB                                                                                                                                                                   0.0s
 => CACHED [stage-1 1/2] FROM docker.io/library/eclipse-temurin:21-jre@sha256:5c1753e7f18ca3fe50e71ff8df1f1211be00aa9bc9b63fa71f556048e2df4f4c                                                         0.0s
 => [build 1/5] FROM docker.io/library/eclipse-temurin:21-jdk@sha256:2e387a63a9086232a53fb668f78bcda1f233118f234326fcb88b0bb2a968ec39                                                                  0.0s
 => CACHED [build 2/5] RUN mkdir -p /usr/app                                                                                                                                                           0.0s
 => CACHED [build 3/5] WORKDIR /usr/app                                                                                                                                                                0.0s
 => [build 4/5] ADD . /usr/app                                                                                                                                                                         0.0s
 => [build 5/5] RUN ./mvnw -f /usr/app/pom.xml clean package                                                                                                                                          46.2s
 => [stage-1 2/2] COPY --from=build /usr/app/target/*.jar /app/runner.jar                                                                                                                              0.1s 
 => exporting to image                                                                                                                                                                                 0.1s 
 => => exporting layers                                                                                                                                                                                0.1s 
 => => writing image sha256:58ef8932a4ad6e1b376e4c15918e6d8d39f90fa76b09c68b09a50e63794cce00                                                                                                           0.0s 
 => => naming to hub.docker.com/layerzerolabs/digital-currency-exchange-app:latest                                                                                                                     0.0s 
                                                                                                                                                                                                            
% docker images
REPOSITORY                                                   TAG                  IMAGE ID       CREATED          SIZE
hub.docker.com/layerzerolabs/digital-currency-exchange-app   latest               58ef8932a4ad   26 seconds ago   306MB
```

## Howto run locally
```shell
% make spin-local

docker build . -t hub.docker.com/layerzerolabs/digital-currency-exchange-app\:latest
...
docker rm $(docker stop $(docker ps -a -q --filter ancestor=hub.docker.com/layerzerolabs/digital-currency-exchange-app\:latest --format="{{.ID}}")) || true                                                 

docker run -p 8080\:8080 hub.docker.com/layerzerolabs/digital-currency-exchange-app\:latest
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

 :: Spring Boot ::                (v3.3.0)

2024-06-02T19:59:46.059Z  INFO 7 --- [DigitalCurrencyExchange] [           main] l.n.D.DigitalCurrencyExchangeApplication : Starting DigitalCurrencyExchangeApplication v0.0.1-SNAPSHOT using Java 21.0.3 with PID 7 (/app/runner.jar started by root in /)
2024-06-02T19:59:46.063Z  INFO 7 --- [DigitalCurrencyExchange] [           main] l.n.D.DigitalCurrencyExchangeApplication : No active profile set, falling back to 1 default profile: "default"
2024-06-02T19:59:47.415Z  INFO 7 --- [DigitalCurrencyExchange] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8080 (http)
2024-06-02T19:59:47.435Z  INFO 7 --- [DigitalCurrencyExchange] [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2024-06-02T19:59:47.435Z  INFO 7 --- [DigitalCurrencyExchange] [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.24]
2024-06-02T19:59:47.475Z  INFO 7 --- [DigitalCurrencyExchange] [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2024-06-02T19:59:47.477Z  INFO 7 --- [DigitalCurrencyExchange] [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 1305 ms
2024-06-02T19:59:47.838Z  INFO 7 --- [DigitalCurrencyExchange] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path '/'
2024-06-02T19:59:47.876Z  INFO 7 --- [DigitalCurrencyExchange] [           main] l.n.D.DigitalCurrencyExchangeApplication : Started DigitalCurrencyExchangeApplication in 2.371 seconds (process running for 3.155)
2024-06-02T20:08:03.141Z  INFO 7 --- [DigitalCurrencyExchange] [nio-8080-exec-5] l.n.D.service.Endpoint                   : GET called
2024-06-02T20:08:04.358Z  INFO 7 --- [DigitalCurrencyExchange] [nio-8080-exec-5] l.n.D.service.Endpoint                   : GET returning data

% curl http://localhost:8080/api/exchange
{
    "Meta Data": {
        "1. Information": "Daily Prices and Volumes for Digital Currency",
        "2. Digital Currency Code": "BTC",
        "3. Digital Currency Name": "Bitcoin",
        "4. Market Code": "EUR",
        "5. Market Name": "Euro",
        "6. Last Refreshed": "2024-06-02 00:00:00",
        "7. Time Zone": "UTC"
    },
    "Time Series (Digital Currency Daily)": {
        "2024-06-02": {
            "1. open": "62445.22000000",
            "2. high": "62571.94000000",
            "3. low": "62409.93000000",
            "4. close": "62557.06000000",
            "5. volume": "1.26747903"
        },
...
```

## Howto push
```shell
% docker login hub.docker.com
Username: ldunal
Password: 

% make push
docker build . -t hub.docker.com/layerzerolabs/digital-currency-exchange-app\:latest
...
docker image push hub.docker.com/layerzerolabs/digital-currency-exchange-app\:latest                                                                                                                        
The push refers to repository [hub.docker.com/layerzerolabs/digital-currency-exchange-app]
b16991865eb9: Preparing 
263b05d874da: Preparing 
e179e4c07349: Preparing 
2c2e968f93dc: Preparing 
e7026e1c8e7b: Preparing 
629ca62fb7c7: Waiting 
...
```

## Howto deploy
```shell
% make deploy
...
cd ./deploy/dce && helm install dce . --set image.repository=hub.docker.com/layerzerolabs/digital-currency-exchange-app\:latest
NAME: dce
LAST DEPLOYED: Sun Jun  2 22:20:44 2024
NAMESPACE: ldunal
STATUS: deployed
REVISION: 1
TEST SUITE: None
NOTES:
1. Get the application URL by running these commands:
  http://chart-example.local/

% helm ls
NAME	NAMESPACE	REVISION	UPDATED                              	STATUS  	CHART    	APP VERSION
dce 	ldunal   	1       	2024-06-02 22:20:44.382833 +0200 CEST	deployed	dce-0.1.0	0.1.0     

% kubectl get all
NAME                       READY   STATUS    RESTARTS   AGE
pod/dce-6ff9b888cc-slkns   1/1     Running   0          13s

NAME          TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)    AGE
service/dce   ClusterIP   10.43.177.167   <none>        8080/TCP   13s

NAME                  READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/dce   1/1     1            1           14s

NAME                             DESIRED   CURRENT   READY   AGE
replicaset.apps/dce-6ff9b888cc   1         1         1       14s

% helm delete dce
release "dce" uninstalled
```


## What can be improved
I was asked to timebox to 2h. If I had more time I would probably build in few more features.
1. I would map the JSON responses from alphavantage service to https://github.com/projectlombok/lombok DTOs. Then I would do some manipulations f.e. I would implement query parameters on my service to select the timeframe and subset of data to return. Now I am returning everything I get from alphavantage as this fulfils the requirement (`At the very minimum it should display open and close values of BTC for the past 2 days.`)
2. I would introduce https://www.sonarsource.com/products/sonarqube/ via maven, during the docker build phase, to do my source code quality inspection.
3. I would introduce `docker scan` to run Snyk tests against images to find vulnerabilities and learn how to fix them
4. I would integrate https://swagger.io/tools/swagger-ui/ via maven to provide fancy GUI for my service
5. I would probably put it on github 
6. I would setup a github actions workflow to build and push the docker image do docker hub automatically.
7. I would setup a github workflow to deploy the image to some K8S.
8. I would introduce maven cache to speed up the build (f.e. `RUN --mount=type=cache,target=/root/.m2 ./mvnw -f $HOME/pom.xml clean package`)