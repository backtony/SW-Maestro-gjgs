package com.gjgs.gjgs.modules.member.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import javax.persistence.Id;
import java.io.Serializable;


// redis Repository로 이용하기 위해서는 @Redishash 어노테이션을 이용해 key를 설정해 주어야합니다.
// 최종적으로 Redis에 들어가는 key는 @RedisHash의 value + @Id가 붙어있는 멤버변수입니다.
// JPA 에 비유하자면 @RedisHash =  @Entity
@Getter
@RedisHash("refreshToken")
@AllArgsConstructor
@Builder
public class RefreshToken implements Serializable {
    @Id
    private String id; // Member의 username

    private String refreshToken;

    @TimeToLive
    private Long expiration; // seconds 가 default고 repository로 찾아오면 시간이 줄어들고 남은 시간만큼만 적혀있음

    public static RefreshToken of(String username, String refreshToken, Long remainingMilliSeconds){
        return RefreshToken.builder()
                .id(username)
                .refreshToken(refreshToken)
                .expiration(remainingMilliSeconds/1000)
                .build();
    }
}
