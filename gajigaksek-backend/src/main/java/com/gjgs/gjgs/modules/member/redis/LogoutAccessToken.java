package com.gjgs.gjgs.modules.member.redis;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;
import java.io.Serializable;

@Getter
@RedisHash("logoutAccessToken")
@AllArgsConstructor
@Builder
public class LogoutAccessToken implements Serializable {
    @Id
    private String id; // username으로 하면 logout 여러번 하면 덮어씌워지므로 username으로 하면 안됨, id는 accessToken

    @Indexed // 필드 값으로 데이터 찾을 수 있게 하는 어노테이션(findByAccessToken)
    private String username;

    @TimeToLive
    private Long expiration; // seconds

    public static LogoutAccessToken of(String accessToken, String username, Long remainingMilliSeconds){
        return LogoutAccessToken.builder()
                .id(accessToken)
                .username(username)
                .expiration(remainingMilliSeconds/1000)
                .build();
    }
}
