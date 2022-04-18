package com.gjgs.gjgs.modules.matching.aop;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckIsAlreadyMatching {
}
