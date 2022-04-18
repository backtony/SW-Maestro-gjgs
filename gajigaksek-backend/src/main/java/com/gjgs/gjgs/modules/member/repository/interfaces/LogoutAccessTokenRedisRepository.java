package com.gjgs.gjgs.modules.member.repository.interfaces;

import com.gjgs.gjgs.modules.member.redis.LogoutAccessToken;
import org.springframework.data.repository.CrudRepository;

public interface LogoutAccessTokenRedisRepository extends CrudRepository<LogoutAccessToken,String> {

}
