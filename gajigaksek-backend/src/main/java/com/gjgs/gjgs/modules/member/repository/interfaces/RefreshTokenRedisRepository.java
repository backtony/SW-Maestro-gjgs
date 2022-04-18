package com.gjgs.gjgs.modules.member.repository.interfaces;

import com.gjgs.gjgs.modules.member.redis.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken,String> {

}
