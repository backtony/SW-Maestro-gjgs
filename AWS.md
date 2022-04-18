
## Jenkins
<div align="center">
 <img src="/images/jenkins.PNG" alt="jenkins">
</div>

CI/CD 구축을 처음 진행해보기에 처음에는 가장 간단한 Travis CI로 구축을 연습하고 실제 프로젝트에 적용을 시도했습니다.  
하지만 SW Maestro에서 제공하는 Gitlab 계정으로 Gitlab, Travis CI 연동이 불가능했습니다.(프로젝트 진행 후반부에야 연동이 가능하도록 업데이트 되었습니다.)  
따라서 다른 선택지가 없어 Jenkins를 선택하게 되었습니다.  
프로젝트의 규모를 생각했을 때, 다양한 세팅과 서버를 구축해야하는 Jenkins가 최선의 선택은 아니라고 생각합니다.  
하지만, 경험적 측면에서는 서버를 구축하고 Jenkins를 사용해 볼 수 있었다는 점이 경험적으로는 좋은 선택이었던 것 같습니다.  

<br>

## AWS 구축 환경 시행 착오

초기에는 간단하게 Jenkins에서 EC2로 jar 파일을 넘겨 서버를 실행하도록 구성했습니다. 그러나 프로젝트가 점점 커지면서 문제가 발생했습니다.
1. 배포 과정에서 서비스가 중단된다.
2. 모든 트래픽을 하나의 서버가 받는다.
3. 보안에 문제가 있다.
4. java -jar로 서버를 껐다가 키는 명령어를 계속 입력하기 불편하다.

해결 방식
1, 2번 : 오토스케일링과 로드밸런서를 통해 트래픽을 분산시켰고 blue/green 배포 방식을 통해 무중단 배포를 구성했습니다.  
3번 : VPC를 구성하여 격리된 네트워크 공간을 만들어 다른 사람들이 접근하는 것을 막았고 bastion EC2를 두어 이를 통해 접근하도록 구성했습니다.  
4번 : 도커를 사용해서 서버를 띄워 명령어의 불편한 점을 해결했습니다.

이 과정에서 다시 문제가 발생했는데 EC2를 생성하는 오토스케일링의 기반 AMI에는 서버 파일이 없다는 것입니다.  
따라서 배포 시에 Jenkins에서 스프링 빌드 후 도커 파일을 빌드하여 생성된 이미지 파일을 도커 허브에 올리고 배포되는 서버와 오토스케일링으로 생성되는 EC2는 모두 도커 허브에 올라가 있는 이미지 파일을 받아서 서버를 실행하도록 구성했습니다.

위의 문제 상황들을 해결하여 최종적으로 Docker, Jenkins, Auto Scaling, Load Balancer, S3, CodeDeploy blue/green 를 사용한 AWS 환경을 구축하였습니다.

<br>

## 📈 최종 아키텍처  <a name = "structure"></a>

<div align="center">
 <img src="/images/structure.PNG" alt="structure">
</div>

- VPC로 논리적으로 격리된 공간을 만들고 외부 접근 제한
    + VPC가 외부와 통신이 가능하도록 Internet Gateway 를 구성하고 라우팅 테이블에서 Public Subnet(10.0.1.0/24, 10.0.2.0/24)과 연결
    + NAT Gateway를 구성하여 나머지 Private Subnet 리소스가 인터넷으로 트래픽이 통할 수 있도록 연결
    + Bastion EC2를 통해 Private Subnet EC2로 접근
- Jenkins와 CodeDeploy를 사용한 Blue Green 무중단 배포
- Load Balancer과 Auto Scaling으로 트래픽 분산
- Redis Cluster 및 Redis stat 모니터링 구축
- Log Monitor 용 ELK 구축
- 검색용 ELK Cluster 구축
- RDS(MySQL) 이중화 구성


보안을 위해 VPC 안에서 전체적인 AWS 환경을 구축하였고, 내부 접근에는 bastion EC2를 통해 접근하도록 설계했습니다.  
로드밸런서와 오토스캐일링으로 트래픽을 분산했으며, Jenkins와 CodeDeploy를 통해 blue green 무중단 배포 환경을 구축했습니다.  
검색 엔진의 경우 RDS의 데이터를 배치작업을 통해 Elastic Search로 로드하고 ELK 클러스터를 통해 안정적으로 구축했습니다.  
Redis 또한 클러스터로 구축하여 Master가 죽어도 FailOver되어 정상 작동하도록 구축했습니다.  
RDS의 경우 DB 이중화를 통해 부하를 줄여주었습니다.  
모니터링의 경우 Kibana와 Redis-stat를 사용했습니다.

+ 구축 포스팅 : [링크](https://backtony.github.io/spring/aws/2021-08-28-spring-cicd-3/)  