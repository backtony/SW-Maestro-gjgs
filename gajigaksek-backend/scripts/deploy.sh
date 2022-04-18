# 가동중인 gjgs 도커 중단 및 삭제
sudo docker ps -a -q --filter "name=gjgs" | grep -q . && docker stop gjgs && docker rm gjgs | true

# 기존 이미지 삭제
sudo docker rmi soma1218/gjgs:latest

# 도커허브 이미지 pull
sudo docker pull soma1218/gjgs:latest

# 도커 run
docker run -d -p 80:8080 -e TZ=Asia/Seoul -v /home/ec2-user/logs:/logs --name gjgs soma1218/gjgs:latest
# docker run -d -p 80:8080 --name gjgs -v /home/ec2-user/logs:/logs -e TZ=Asia/Seoul soma1218/gjgs:latest
#docker run -d -p 8080:8080 -v /home/ec2-user:/config --name gjgs --network gjgs soma1218/gjgs:1.0

# code deploy 연동 후 1.0 버전 빼고 latest로 변경할것
# webhook test

# 사용하지 않는 불필요한 이미지 삭제 -> 현재 컨테이너가 물고 있는 이미지는 삭제 안됨
docker rmi -f $(docker images -f "dangling=true" -q) || true

