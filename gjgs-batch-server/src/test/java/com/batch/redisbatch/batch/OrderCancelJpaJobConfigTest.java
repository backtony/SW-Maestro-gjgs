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
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(classes = {TestBatchConfig.class, OrderCancelJpaJobConfig.class})
@SpringBatchTest
public class OrderCancelJpaJobConfigTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired TeamOrderRepository teamOrderRepository;
    @MockBean OrderService orderService;
    @MockBean IamportClient iamportClient;

    @AfterEach
    void teardown(){
        teamOrderRepository.deleteAllInBatch();
        teamOrderRepository.deleteAll();
    }

    @DisplayName("orderCancelJob 테스트")
    @Test
    void orderJob() throws Exception{
        //given
        for(int i=1;i<=500;i++){
            TeamOrder teamOrder = TeamOrderDummy.createTeamOrder(Long.valueOf(i), Long.valueOf(i), TeamOrderStatus.WAIT);
            teamOrder.setCreatedDate(LocalDateTime.now().minusMinutes(33));
            teamOrderRepository.save(teamOrder);
        }

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        List<TeamOrder> teamOrderList = teamOrderRepository.findAll();


        //then
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertEquals(TeamOrderStatus.CANCEL,teamOrderList.get(0).getTeamOrderStatus());
    }
}
