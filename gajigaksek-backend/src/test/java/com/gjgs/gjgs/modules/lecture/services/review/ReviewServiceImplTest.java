package com.gjgs.gjgs.modules.lecture.services.review;

import com.gjgs.gjgs.modules.dummy.LectureDummy;
import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.lecture.dtos.review.CreateReviewReplyRequest;
import com.gjgs.gjgs.modules.lecture.dtos.review.CreateReviewRequest;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.entity.Participant;
import com.gjgs.gjgs.modules.lecture.entity.Review;
import com.gjgs.gjgs.modules.lecture.entity.Schedule;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureQueryRepository;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureRepository;
import com.gjgs.gjgs.modules.lecture.repositories.participant.ParticipantQueryRepository;
import com.gjgs.gjgs.modules.lecture.repositories.review.ReviewQueryRepository;
import com.gjgs.gjgs.modules.lecture.repositories.review.ReviewRepository;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberRepository;
import com.gjgs.gjgs.modules.utils.s3.FileManager;
import com.gjgs.gjgs.modules.utils.s3.FilePaths;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import com.gjgs.gjgs.modules.utils.vo.FileInfoVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.gjgs.gjgs.modules.lecture.enums.LectureStatus.ACCEPT;
import static com.gjgs.gjgs.modules.lecture.enums.ScheduleStatus.END;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    private final String PATH = FilePaths.REVIEW_IMAGE_PATH.getPath();

    @Mock SecurityUtil securityUtil;
    @Mock ReviewQueryRepository reviewQueryRepository;
    @Mock LectureRepository lectureRepository;
    @Mock LectureQueryRepository lectureQueryRepository;
    @Mock ReviewRepository reviewRepository;
    @Mock FileManager fileManager;
    @Mock MemberRepository memberRepository;
    @Mock ParticipantQueryRepository participantQueryRepository;
    @InjectMocks ReviewServiceImpl reviewService;

    @Test
    @DisplayName("리뷰 작성하기")
    void create_review_test() throws Exception {

        // given
        Member director = MemberDummy.createTestDirectorMember();
        Lecture lecture = Lecture.builder().id(1L).director(director).build();
        Member reviewer = Member.builder().id(1L).username("username").build();
        Schedule schedule = Schedule.builder().id(1L)
                .lecture(lecture)
                .participantList(List.of(Participant.builder().member(reviewer).build()))
                .scheduleStatus(END)
                .build();
        lecture.addSchedule(schedule);
        MultipartFile file = createFile();
        stubbingFileManager(file);
        stubbingSecurityUtil(reviewer);
        stubbingMemberRepository(reviewer);
        stubbingParticipantRepository();
        stubbingLectureExist();
        double avg = 2.4d;
        stubbingReviewScoreAvg(avg);
        CreateReviewRequest request = createReviewRequest(lecture, schedule);

        // when
        reviewService.createReview(request, file);

        // then
        assertAll(
                () -> verify(fileManager).uploadFile(file, PATH),
                () -> verify(memberRepository).findIdByUsername(reviewer.getUsername()),
                () -> verify(participantQueryRepository).existParticipantByScheduleIdMemberId(schedule.getId(), reviewer.getId()),
                () -> verify(lectureRepository).existsLectureByIdAndLectureStatus(lecture.getId(), ACCEPT),
                () -> verify(reviewRepository).save(any()),
                () -> verify(reviewQueryRepository).findLectureReviewsScoreAvg(lecture.getId()),
                () -> verify(lectureQueryRepository).updateLectureScore(lecture.getId(), avg)
        );
    }

    @Test
    @DisplayName("리뷰 답글 작성하기")
    void reply_review_test() throws Exception {

        // given
        CreateReviewReplyRequest request = createReviewReplyRequest();
        Member director = MemberDummy.createTestDirectorMember();
        Lecture lecture = LectureDummy.createLecture(1, director);
        Review review = Review.builder().id(1L).lecture(lecture).text("test").score(5).build();
        stubbingCurrentUsername(director);
        stubbingFindReview(review, director);

        // when
        reviewService.replyReview(review.getId(), request);

        // then
        assertAll(
                () -> verify(reviewQueryRepository).findWithLectureByReviewIdDirectorUsername(review.getId(), director.getUsername()),
                () -> assertEquals(review.getReplyText(), request.getReplyText())
        );
    }

    private void stubbingReviewScoreAvg(Double avg) {
        when(reviewQueryRepository.findLectureReviewsScoreAvg(any())).thenReturn(avg);
    }

    private void stubbingSecurityUtil(Member reviewer) {
        when(securityUtil.getCurrentUsername()).thenReturn(Optional.of(reviewer.getUsername()));
    }

    private void stubbingLectureExist() {
        when(lectureRepository.existsLectureByIdAndLectureStatus(any(), any())).thenReturn(true);
    }

    private void stubbingParticipantRepository() {
        when(participantQueryRepository.existParticipantByScheduleIdMemberId(any(), any())).thenReturn(true);
    }

    private void stubbingCurrentUsername(Member director) {
        when(securityUtil.getCurrentUsername()).thenReturn(Optional.of(director.getUsername()));
    }

    private void stubbingFindReview(Review review, Member director) {
        when(reviewQueryRepository.findWithLectureByReviewIdDirectorUsername(review.getId(), director.getUsername()))
                .thenReturn(Optional.of(review));
    }

    private void stubbingFileManager(MultipartFile file) throws IOException {
        when(fileManager.uploadFile(file, PATH))
                .thenReturn(FileInfoVo.of("test", "test"));
    }

    private void stubbingMemberRepository(Member reviewer) {
        when(memberRepository.findIdByUsername(reviewer.getUsername())).thenReturn(Optional.of(reviewer.getId()));
    }

    private MultipartFile createFile() {
        return new MockMultipartFile("files",
                "image.img",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                "image".getBytes());
    }

    private CreateReviewReplyRequest createReviewReplyRequest() {
        return CreateReviewReplyRequest.builder()
                .replyText("testtesttest")
                .build();
    }

    private CreateReviewRequest createReviewRequest(Lecture lecture, Schedule schedule) {
        return CreateReviewRequest.builder().lectureId(lecture.getId()).text("testtest").scheduleId(schedule.getId()).score(5).build();
    }
}