package com.gjgs.gjgs.infra.config.db;

import org.springframework.context.annotation.Profile;

import java.util.List;

@Profile("prod")
public class ReplicationRoutingCircularList<T> {
    private List<T> list;
    private static Integer counter = 0;

    public ReplicationRoutingCircularList(List<T> list) {
        this.list = list;
    }

    public T getOne() {
        int circularSize = list.size();
        if (counter + 1 > circularSize) {
            counter = 0;
        }
        return list.get(counter++ % circularSize);
    }
}
