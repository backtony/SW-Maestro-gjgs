package com.gjgs.gjgs.modules.member.service.mypage.impl;

import com.gjgs.gjgs.modules.member.dto.myinfo.DirectorTextModifyRequest;
import com.gjgs.gjgs.modules.member.dto.mypage.DirectorMyPageResponse;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.service.mypage.interfaces.DirectorEditService;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DirectorEditServiceImpl implements DirectorEditService {

    private final SecurityUtil securityUtil;

    @Override
    @Transactional(readOnly = true)
    public DirectorMyPageResponse getDirectorEditPage() {
        return DirectorMyPageResponse.from(securityUtil.getCurrentUserOrThrow());
    }

    @Override
    public void editDirectorText(DirectorTextModifyRequest directorTextModifyRequest) {
        Member currentUser = securityUtil.getCurrentUserOrThrow();
        currentUser.changeDirectorText(directorTextModifyRequest.getDirectorText());
    }

}
