package com.batch.redisbatch.batch;

import com.batch.redisbatch.TestBatchConfig;
import com.batch.redisbatch.batch.config.order.OrderCancelJpaJobConfig;
import com.batch.redisbatch.domain.TeamOrder;
import com.batch.redisbatch.dummy.TeamOrderDummy;
import com.batch.redisbatch.enums.TeamOrderStatus;
import com.batch.redisbatch.repository.interfaces.TeamOrderRepository;
import com.batch.redisbatch.service.interfaces.OrderService;
import com.siot.IamportRestClient.IamportClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBatchTest
@SpringBootTest(classes = {OrderCancelJpaJobConfig.class, TestBatchConfig.class})
class OrderReaderTest {

    @Autowired JpaPagingItemReader<TeamOrder> reader;
    @Autowired TeamOrderRepository teamOrderRepository;
    @MockBean IamportClient iamportClient;
    @MockBean OrderService orderService;

    @AfterEach
    void teardown(){
        teamOrderRepository.deleteAllInBatch();
        teamOrderRepository.deleteAll();
    }

    @DisplayName("reader")
    @Test
    void reader() throws Exception{
        // given
        TeamOrder teamOrder = TeamOrderDummy.createTeamOrder(Long.valueOf(1L), Long.valueOf(1L), TeamOrderStatus.WAIT);
        teamOrder.setCreatedDate(LocalDateTime.now().minusMinutes(33));
        teamOrderRepository.save(teamOrder);

        //when
        reader.open(new ExecutionContext());

        //then
        assertThat(reader.read().getTeamOrderStatus()).isEqualTo(TeamOrderStatus.WAIT);
        assertThat(reader.read()).isNull();
    }
}
