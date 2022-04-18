package com.gjgs.gjgs.modules.member.service;


import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.member.dto.myinfo.DirectorTextModifyRequest;
import com.gjgs.gjgs.modules.member.dto.mypage.DirectorMyPageResponse;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.service.mypage.impl.DirectorEditServiceImpl;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DirectorEditServiceTest {


    @InjectMocks DirectorEditServiceImpl directorEditService;
    @Mock SecurityUtil securityUtil;

    Member member;

    @BeforeEach
    void setup(){
        member = MemberDummy.createTestMember();
    }

    @DisplayName("directorText 수정")
    @Test
    void edit_directorText() throws Exception {

        //given
        stubGetCurrentUser();
        DirectorTextModifyRequest directorTextModifyRequest = createDirectorTextModifyRequest();


        // when
        directorEditService.editDirectorText(directorTextModifyRequest);

        // then
        assertEquals(directorTextModifyRequest.getDirectorText(), member.getDirectorText());
    }

    private DirectorTextModifyRequest createDirectorTextModifyRequest() {
        String text = "hello world";
        DirectorTextModifyRequest directorTextModifyRequest = DirectorTextModifyRequest.from(text);
        return directorTextModifyRequest;
    }

    @DisplayName("director Mypage 편집 페이지 정보 가져오기")
    @Test
    void get_directorEdit_page() throws Exception {

        //given
        stubGetCurrentUser();

        // when then
        assertThat(directorEditService.getDirectorEditPage())
                .isInstanceOf(DirectorMyPageResponse.class);
    }



    private void stubGetCurrentUser() {
        when(securityUtil.getCurrentUserOrThrow()).thenReturn(member);
    }

}
