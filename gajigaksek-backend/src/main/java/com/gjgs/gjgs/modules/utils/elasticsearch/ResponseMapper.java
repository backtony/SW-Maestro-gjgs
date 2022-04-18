package com.gjgs.gjgs.modules.utils.elasticsearch;

import java.util.List;

public interface ResponseMapper<S, R> {

    List<R> toResponse (List<S> source);
}
