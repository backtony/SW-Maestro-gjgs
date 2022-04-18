
# 💎 왜 이 기술을 사용했는가? <a name = "why"></a>

## API 문서화

<div align="center">
 <img src="/images/refac-docs.PNG" alt="refac-docs">
</div>

Jira로 일정관리를 하고 있었기에 프로젝트 초기에는 Jira Confluence를 사용하여 API 문서화를 진행했습니다.  
프로젝트 초기 단계가 지나 작성해야 하는 API들이 많아지면서 일일이 Confluence에 작성하고 확인하기가 번거로워졌기에 코드상으로 해결 가능한 Swagger를 적용하여 문서화를 진행했습니다.  
이후 프로젝트의 중후반 단계가 되었을 때, 정말 많은 API들을 만들게 되었는데 이 과정에서 Swagger의 단점이 명확하게 보이기 시작했습니다.
1. 문서화 작업을 위한 Swagger 애노테이션으로 인해 코드의 가독성이 떨어진다.
2. 테스트 기반이 아니기에 기능이 100% 동작한다고 확신할 수 없다.
3. 모든 오류에 대한 여러 가지 응답을 문서화할 수 없다.

위와 같은 문제를 Spring REST docs는 모두 해결할 수 있었기에 Spring REST docs로 전환하게 되었습니다.

<br>

## Querydsl
Spring Data JPA가 기본적으로 제공해주는 CRUD 메서드 및 쿼리 메서드 기능을 사용하더라도, 원하는 조건의 데이터를 수집하기 위해서는 필연적으로 JPQL을 작성하게 됩니다.  
간단한 로직을 작성하는데 큰 문제는 없으나, 복잡한 로직의 경우 개행이 포함된 쿼리 문자열이 상당히 길어집니다.  
JPQL 문자열에 오타 혹은 문법적인 오류가 존재하는 경우, 정적 쿼리라면 어플리케이션 로딩 시점에 이를 발견할 수 있으나 그 외는 런타임 시점에서 에러가 발생합니다.  
이러한 문제를 해결해 주는 것이 Querydsl이기에 Querydsl을 도입했습니다.  
Querydsl 도입으로 다음과 같은 이점을 얻었습니다.
+ 문자가 아닌 코드로 쿼리를 작성함으로써, 컴파일 시점에 문법 오류를 쉽게 확인할 수 있다.
+ 자동 완성 등 IDE의 도움을 받을 수 있다.
+ 동적인 쿼리 작성이 편리하다.
+ 쿼리 작성 시 제약 조건 등을 메서드 추출을 통해 재사용할 수 있다.

<br>

## FCM을 사용한 알림 기능
앱에서 알림 기능을 추가해야 하는 상황이 발생했습니다.  
따로 알림 서버를 구현해서 알림 메시지를 처리하는 것은 따로 관리해야할 일도 많아지고 그만한 시간을 소비해서 구축해야할 여유도 없는 상황이었기에 이미 구축되어있고 쉽게 사용할 수 있는 것을 찾아야 했습니다.  
결과적으로 FCM을 사용하여 알림 기능을 구현하게 되었습니다.  
FCM 도입으로 인해 다음과 같은 장점을 얻을 수 있었습니다.  
+ 기능 구축에 시간 절약
+ 서버리스 아키텍처를 구성하여 메모리, CPU, 트래픽 등의 관리 포인트가 크게 줄어들어 관련 비용을 감소

<br>

## 토근 저장소 MySQL -> Redis
로그인 관련해서는 JWT토큰을 이용해 구현했습니다.  
이 과정에서 Access Token과 Refresh Token의 유효시간이 지나게 되면 expire 되도록 처리를 해야했는데 이 과정을 MySQL에서 진행하기에는 부담이 되는 작업이었습니다.  
하지만 해당 작업을 Redis의 TTL기능을 사용하여 구현한다면 간단하게 처리할 수 있었기에 토큰 저장소로 Redis를 사용하게 되었습니다.  
Redis를 처음 사용해보는 것이기에 초기에는 하나의 EC2에 Redis를 띄워 사용하였으나, 멘토님의 조언을 듣고 조금 더 안전한 설계로 변경하게 되었습니다.
<div align="center">
 <img src="/images/refac-redis.PNG" alt="refac-redis">
</div>

설계 초기처럼 하나의 Redis만 사용할 경우, Redis가 죽어버리면 Redis를 사용하는 로직에 생기기 때문에 Master Redis 3대, Slave Redis 6대를 띄워 클러스터를 구축하였습니다.  
따라서 하나의 Master Redis가 죽어도 Failover을 통해 Slave가 Master로 승격되기 때문에 가용성을 높일 수 있었습니다.   

<Br>


## RabbitMQ -> Redis Expire Event + Spring batch
<div align="center">
 <img src="/images/refac-message.PNG" alt="refac-message">
</div>

결제 과정에서 그룹 수강신청의 경우, 그룹이 신청한 스케줄에 대해서는 그룹 인원만큼의 여석은 다른 고객이 신청하지 못하도록 막아서 확보해야 했습니다.  
예를 들면, 0/8 인 상태에서 4명이 있는 그룹이 수강신청을 한다면 4/8 인 상태로 변경해야 했습니다. 이 과정에서 그룹원들이 결제할 때까지 시간을 무한정으로 줄 수 없기 때문에 30분으로 제한하도록 비즈니스 로직을 설계했습니다.  
따라서 30분이 지난 후에 결제가 완료되지 않았다면 해당 그룹의 수강 신청을 취소시켜야했습니다.  
처음에는 이 로직을 구현하기 위해서 RabbitMQ를 사용하여 다음과 같이 구현했습니다.  

> 수강 신청시 RabbitMQ로 메시지를 보내고, RabbitMQ Delayed Message Plugin를 이용해 30분이 지난 후에 처리한다.

RabbitMQ를 처음 사용해 보는 기술이었기에 멘토님께 조언을 구했고 RabbitMQ도 결국 거대한 큐이기 때문에 30분 동안 저장해두고 처리하도록 설계할 경우, 수많은 요청이 몰리면 병목현상이 발생할 것이라는 조언을 받아 다른 방식을 도입해야 했습니다.  
프로젝트에서 캐시와 토큰 저장소로 Redis를 사용하고 있기에 'Redis의 TTL을 활용하면 이 문제를 해결할 수 있지 않을까?'라는 생각으로 설계를 다시 하기 시작했습니다.  
수강 신청 시 TTL을 30분으로 설정하여 redis에 저장해두고 TTL이 끝나면 pub/sub 방식으로 message를 쏘도록 만들어 준 뒤, Spring에서는 메시지 리스너를 구현해 메시지를 받아 로직을 수행하도록 구현했습니다.  
하지만 이 메시지가 100% 리스너에 도착한다고 보장할 수는 없기 때문에 이에 대한 안전 장치로 Spring batch + Quartz 를 사용하여 30분마다 배치 작업을 수행하도록 설계했습니다.  

<br>

결과적으로 문제를 해결했지만 설계상으로 아직 해결하지 못한 부분이 남아있습니다.  
redis key expire이 pub/sub 방식으로 message를 전달하기 때문에 여러 서버에서 구독을 하게 된다면 중복해서 처리하게 되는 문제가 발생합니다. 따라서 프로젝트 구성에서는 하나의 서버가 이 로직을 담당했고 스케일 아웃은 못하고 스케일 업을 해야만 했습니다.  
이 문제는 그당시에는 해결하지 못했고 현재 kafka를 공부하면서 컨슈머 그룹을 사용하면 이 문제를 해결할 수 있을 것 같다는 생각이 듭니다.

<br>

## DB Replication
<div align="center">
 <img src="/images/refac-db.PNG" alt="refac-db">
</div>

초기에는 하나의 RDS를 가지고 모든 작업을 진행했습니다.  
하지만 트래픽이 늘어날 경우, 하나의 DB에서 쿼리를 모두 처리하기에는 병목현상이 발생할 가능성이 있다고 판단했습니다.  
따라서 DB 이중화를 도입했습니다.  
DB를 이중화할 경우, Master에서는 쓰기/수정/삭제 연산을 처리하고 Slave에서는 읽기 연산만을 처리하여 병목 현상을 줄일 수 있었습니다.  

<br>

## 검색 기능 DB -> Elasticsearch
<div align="center">
 <img src="/images/refac-elasticsearch.PNG" alt="refac-elasticsearch">
</div>

기존에는 클래스 검색에 AWS RDS에서 데이터를 꺼내오도록 했으나 검색 성능 향상을 위해 Elastic Search로 전환했습니다.

<br>

## Flyway
<div align="center">
 <img src="/images/refac-flyway.PNG" alt="refac-flyway">
</div>

dev 환경에서는 단순히 ddl을 create-drop 또는 update 옵션을 사용하고 있었기에 DB에 대해 고민할 필요가 없었습니다.  
하지만 운영환경에서는 ddl을 validate 또는 none 옵션을 사용해야하기 때문에 초기에는 DB script를 뽑아서 별도로 관리를 했습니다.  
이후 기능이 추가되면서 script가 변경되는 일이 빈번해졌고, 매번 일일이 스크립트를 관리하는 것이 번거로울 뿐 아니라 실수하기 딱 좋은 부분이라 Flyway를 도입하여 데이터베이스 형상관리를 진행했습니다.

<br>

## Cloud Watch -> Kibana
<div align="center">
 <img src="/images/refac-monitor.PNG" alt="monitor">
</div>

초기 구축에서는 간단하게 Cloud Watch를 사용하고 로그 모니터링 환경을 구축했습니다.  
Cloud Watch만으로도 충분히 원하는 목적을 달성할 수 있었지만, 취업을 준비하는 입장에서 AWS 자원을 마음껏 사용할 수 있는 기회는 드물기 때문에 여러 가지를 도전해 보고 싶었습니다.  
마침 검색 엔진을 Elastic Search로 변경해서 성능을 높여보자는 의견이 팀에서 있었기에 여러 가지 도전을 해보고자 모니터링도 Kibana로 변경해서 구축하게 되었습니다.    
  각 EC2에 filebeat를 심어주고 logstash에서 가공하여 elastic search로 보내도록 설계해서 구축하였습니다.  

