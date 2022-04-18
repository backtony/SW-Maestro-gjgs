package com.gjgs.gjgs.modules.lecture.entity;

import com.gjgs.gjgs.modules.category.entity.Category;
import com.gjgs.gjgs.modules.exception.schedule.ScheduleNotFoundException;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLecture;
import com.gjgs.gjgs.modules.lecture.embedded.Price;
import com.gjgs.gjgs.modules.lecture.embedded.Terms;
import com.gjgs.gjgs.modules.lecture.enums.LectureStatus;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.utils.base.BaseEntity;
import com.gjgs.gjgs.modules.utils.vo.FileInfoVo;
import com.gjgs.gjgs.modules.zone.entity.Zone;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.gjgs.gjgs.modules.lecture.enums.LectureStatus.*;
import static java.util.stream.Collectors.toList;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
public class Lecture extends BaseEntity implements CheckLectureDirector{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "LECTURE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member director;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ZONE_ID", nullable = false)
    private Zone zone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    private Category category;

    @Lob
    @Column(nullable = false)
    private String thumbnailImageFileUrl;

    @Lob
    @Column(nullable = false)
    private String thumbnailImageFileName;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "int default 0")
    private int favoriteCount;

    @Embedded
    private Price price;

    @Embedded
    private Terms terms;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LectureStatus lectureStatus;

    @OneToMany(mappedBy = "lecture")
    @Builder.Default
    private List<FinishedProduct> finishedProductList = new ArrayList<>();

    @OneToMany(mappedBy = "lecture")
    @Builder.Default
    private List<Curriculum> curriculumList = new ArrayList<>();

    @OneToMany(mappedBy = "lecture")
    @Builder.Default
    private List<Schedule> scheduleList = new ArrayList<>();

    @Column(nullable = false)
    private boolean finished;

    private String mainText;

    private int progressTime;

    @Column(columnDefinition = "int default 0")
    private int clickCount;

    @Column(nullable = false)
    private String fullAddress;

    private double score;

    private boolean onlyGjgs;

    private int minParticipants;

    private int maxParticipants;

    private String rejectReason;

    public static Lecture of(Long lectureId){
        return Lecture.builder()
                .id(lectureId)
                .build();
    }

    public static Lecture of(List<FileInfoVo> fileInfoVo, Category lecturesCategory, Zone lecturesZone, CreateLecture.FirstRequest firstRequest, Member director) {
        return Lecture.builder()
                .category(lecturesCategory)
                .zone(lecturesZone)
                .thumbnailImageFileName(fileInfoVo.get(0).getFileName())
                .thumbnailImageFileUrl(fileInfoVo.get(0).getFileUrl())
                .fullAddress(firstRequest.getAddress())
                .title(firstRequest.getTitle())
                .director(director)
                .clickCount(0)
                .finished(true)
                .price(Price.builder().regularPrice(0).priceOne(0).priceTwo(0).priceThree(0).priceFour(0).build())
                .terms(Terms.builder().termsOne(false).termsTwo(false).termsThree(false).termsFour(false).build())
                .lectureStatus(CREATING).build();
    }

    public void putIntro(CreateLecture.IntroRequest introRequest, List<FileInfoVo> finishedProductImageInfoList) {
        this.mainText = introRequest.getMainText();
        clearFinishedProducts();
        List<FinishedProduct> finishedProducts = FinishedProduct
                .of(introRequest.getFinishedProductInfoList(), finishedProductImageInfoList, this);
        this.getFinishedProductList().addAll(finishedProducts);
    }

    private void clearFinishedProducts() {
        if (finishedProductIsNotEmpty())
            this.getFinishedProductList().clear();
    }

    public boolean finishedProductIsNotEmpty() {
        return !this.getFinishedProductList().isEmpty();
    }

    public boolean isDifferentCategory(Long categoryId) {
        return !this.category.getId().equals(categoryId);
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isDifferentZone(Long zoneId) {
        return !this.zone.getId().equals(zoneId);
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public List<String> getThumbnailImageFileNames() {
        return List.of(this.getThumbnailImageFileName());
    }

    public void putFirst(CreateLecture.FirstRequest request, List<FileInfoVo> saveFiles) {
        setThumbnail(saveFiles.get(0));
        setTitleAndAddress(request);
    }

    public void setTitleAndAddress(CreateLecture.FirstRequest firstRequest) {
        this.title = firstRequest.getTitle();
        this.fullAddress = firstRequest.getAddress();
    }

    public void setThumbnail(FileInfoVo fileInfoVo) {
        this.thumbnailImageFileName = fileInfoVo.getFileName();
        this.thumbnailImageFileUrl = fileInfoVo.getFileUrl();
    }

    public List<String> getFinishedProductsFileNames() {
        return this.getFinishedProductList().stream()
                .map(FinishedProduct::getFinishedProductImageName)
                .collect(toList());
    }

    public void putCurriculums(List<CreateLecture.CurriculumDto> curriculumDtoList,
                               List<FileInfoVo> fileInfoVoList) {
        clearCurriculums();
        List<Curriculum> curriculumEntityList = Curriculum.of(curriculumDtoList,
                fileInfoVoList, this);
        this.getCurriculumList().addAll(curriculumEntityList);
    }

    private void clearCurriculums() {
        if (curriculumsNotEmpty()) {
            this.getCurriculumList().clear();
        }
    }

    public boolean curriculumsNotEmpty() {
        return !this.getCurriculumList().isEmpty();
    }

    public List<String> getCurriculumFileNames() {
        return getCurriculumList().stream()
                .map(Curriculum::getCurriculumImageName)
                .collect(toList());
    }

    public void putSchedule(CreateLecture.ScheduleRequest scheduleRequest) {
        clearSchedules();
        scheduleRequest.getScheduleList().forEach(CreateLecture.ScheduleDto::setEndTimes);
        this.minParticipants = scheduleRequest.getMinParticipants();
        this.maxParticipants = scheduleRequest.getMaxParticipants();
        this.getScheduleList().addAll(Schedule.of(scheduleRequest.getScheduleList(), this));
    }

    private void clearSchedules() {
        if(scheduleIsNotEmpty()) {
            this.getScheduleList().clear();
        }
    }

    public boolean scheduleIsNotEmpty() {
        return !this.getScheduleList().isEmpty();
    }

    public void putPrice(CreateLecture.PriceDto price) {
        this.price = Price.of(price);
    }

    public void putTermsChangeStatus(CreateLecture.TermsRequest termsRequest) {
        putTerms(termsRequest);
        changeLectureConfirm();
    }

    private void putTerms(CreateLecture.TermsRequest termsRequest) {
        this.terms = Terms.of(termsRequest);
    }

    private void changeLectureConfirm() {
        this.lectureStatus = CONFIRM;
    }

    public void addSchedule(Schedule schedule) {
        this.getScheduleList().add(schedule);
    }

    public Schedule getSchedule(Long scheduleId) {
        return this.getScheduleList().stream()
                .filter(schedule -> schedule.getId().equals(scheduleId))
                .findFirst()
                .orElseThrow(() -> new ScheduleNotFoundException());
    }

    public void removeSchedule(Schedule schedule) {
        this.getScheduleList().remove(schedule);
    }

    public void reject(String rejectReason) {
        this.lectureStatus = REJECT;
        this.rejectReason = rejectReason;
    }

    public void accept() {
        this.lectureStatus = ACCEPT;
        this.finished = false;
    }

    public int getPricePerMember(int memberSize) {
        return this.getPrice().getPricePerMember(memberSize);
    }

    public void rewrite() {
        this.lectureStatus = CREATING;
    }
}
