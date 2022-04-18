package com.gjgs.gjgs.modules.member.service.mypage.interfaces;

import com.gjgs.gjgs.modules.member.dto.myinfo.DirectorTextModifyRequest;
import com.gjgs.gjgs.modules.member.dto.mypage.DirectorMyPageResponse;

public interface DirectorEditService {

    DirectorMyPageResponse getDirectorEditPage();

    void editDirectorText(DirectorTextModifyRequest directorText);
}
