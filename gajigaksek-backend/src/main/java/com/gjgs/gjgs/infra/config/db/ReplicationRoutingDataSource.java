package com.gjgs.gjgs.infra.config.db;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Map;

import static java.util.stream.Collectors.toList;

@Profile("prod")
public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {

    private ReplicationRoutingCircularList<String> replicationRoutingDataSourceNameList;

    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);

        replicationRoutingDataSourceNameList = new ReplicationRoutingCircularList<>(
                targetDataSources.keySet()
                        .stream()
                        .filter(key -> key.toString().contains("slave"))
                        .map(Object::toString)
                        .collect(toList()));
    }

    @Override
    protected Object determineCurrentLookupKey() {
        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        if (isReadOnly) {
            return replicationRoutingDataSourceNameList.getOne();
        }
        return "master";
    }
}
