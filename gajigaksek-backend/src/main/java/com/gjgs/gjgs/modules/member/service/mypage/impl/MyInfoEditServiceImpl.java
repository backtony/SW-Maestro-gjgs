package com.gjgs.gjgs.modules.member.service.mypage.impl;

import com.gjgs.gjgs.modules.exception.member.MemberNotFoundException;
import com.gjgs.gjgs.modules.member.dto.myinfo.*;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberCategoryRepository;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberJdbcRepository;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberRepository;
import com.gjgs.gjgs.modules.member.service.mypage.interfaces.MyInfoEditService;
import com.gjgs.gjgs.modules.utils.s3.FileManager;
import com.gjgs.gjgs.modules.utils.s3.FilePaths;
import com.gjgs.gjgs.modules.utils.s3.interfaces.AmazonS3Service;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import com.gjgs.gjgs.modules.zone.entity.Zone;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class MyInfoEditServiceImpl implements MyInfoEditService {

    private final AmazonS3Service amazonS3Service;
    private final MemberRepository memberRepository;
    private final MemberCategoryRepository memberCategoryRepository;
    private final MemberJdbcRepository memberJdbcRepository;
    private final FileManager fileManager;
    private final SecurityUtil securityUtil;

    @Value("${spring.social.kakao.image_url}")
    private String kakaoImageUrl;

    @Override
    @Transactional(readOnly = true)
    public MyPageEditResponse editMyPage() {
        Member currentUserWithMemberCategory = memberRepository.findWithMemberCategoryByUsername(getUsernameOrThrow())
                .orElseThrow(() -> new MemberNotFoundException());
        return MyPageEditResponse.from(currentUserWithMemberCategory);
    }

    @Override
    public void editNickname(NicknameModifyRequest nicknameModifyRequest) {
        Member currentUser = securityUtil.getCurrentUserOrThrow();
        currentUser.changeNickname(nicknameModifyRequest.getNickname());
    }

    @Override
    public void editPhone(PhoneModifyRequest phoneModifyRequest) {
        Member currentUser = securityUtil.getCurrentUserOrThrow();
        currentUser.changePhone(phoneModifyRequest.getPhone());
    }

    @Override
    public void editCategory(List<Long> categoryIdList) {
        Long currentUserId = memberRepository.findIdByUsername(getUsernameOrThrow())
                .orElseThrow(() -> new MemberNotFoundException());

        if (memberCategoryRepository.existsByMemberId(currentUserId)) {
            memberCategoryRepository.deleteAllWithBulkByMemberId(currentUserId);
        }
        memberJdbcRepository.insertMemberCategoryList(currentUserId, categoryIdList);
    }


    private String getUsernameOrThrow() {
        return securityUtil.getCurrentUsername()
                .orElseThrow(() -> new MemberNotFoundException());
    }

    @Override
    public void editProfileText(ProfileTextModifyRequest profileTextModifyRequest) {
        Member currentUser = securityUtil.getCurrentUserOrThrow();
        currentUser.changeProfileText(profileTextModifyRequest.getProfileText());
    }

    @Override
    public void editImage(MultipartFile file) {
        Member member = securityUtil.getCurrentUserOrThrow();
        String savedPath = FilePaths.MEMBER_IMAGE_PATH.getPath();
        String filename = fileManager.getUploadedFileName(file, savedPath);
        String uploadImageUrl = fileManager.upload(file, filename, savedPath);

        if (!member.getImageFileUrl().startsWith(kakaoImageUrl)) {
            amazonS3Service.delete(savedPath, member.getImageFileName());
        }

        member.changeFileInfo(filename, uploadImageUrl);
    }

    @Override
    public void editZone(Long zoneId) {
        Member currentUser = securityUtil.getCurrentUserOrThrow();
        currentUser.changeZone(Zone.of(zoneId));
    }


    @Override
    public void editMyEventAlarm(AlarmEditRequest alarmEditRequest) {
        Member currentUser = securityUtil.getCurrentUserOrThrow();
        currentUser.changeAlarm(alarmEditRequest.getType(), alarmEditRequest.isActive());
    }


}
