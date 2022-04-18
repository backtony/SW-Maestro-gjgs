package com.gjgs.gjgs.config.repository;

import com.gjgs.gjgs.modules.category.entity.Category;
import com.gjgs.gjgs.modules.category.repositories.CategoryRepository;
import com.gjgs.gjgs.modules.dummy.CategoryDummy;
import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.dummy.ZoneDummy;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberRepository;
import com.gjgs.gjgs.modules.zone.entity.Zone;
import com.gjgs.gjgs.modules.zone.repositories.interfaces.ZoneRepository;
import lombok.Getter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@Getter
public class SetUpMemberRepository extends RepositoryTest {

    protected Zone zone;
    protected Category category;
    protected Member director;
    protected Member leader;
    protected List<Member> anotherMembers = new ArrayList<>();

    @Autowired protected CategoryRepository categoryRepository;
    @Autowired protected ZoneRepository zoneRepository;
    @Autowired protected MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        zone = zoneRepository.save(ZoneDummy.createZone());
        category = categoryRepository.save(CategoryDummy.createCategory());
        for(int i = 1; i < 4; i++) {
            anotherMembers.add(memberRepository.save(MemberDummy.createDataJpaTestMember(i, zone, category)));
        }
        director = memberRepository.save(MemberDummy.createDataJpaTestDirector(4, zone, category));
        leader = memberRepository.save(MemberDummy.createDataJpaTestMember(5, zone, category));
    }

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
        zoneRepository.deleteAll();
        categoryRepository.deleteAll();
    }
}
