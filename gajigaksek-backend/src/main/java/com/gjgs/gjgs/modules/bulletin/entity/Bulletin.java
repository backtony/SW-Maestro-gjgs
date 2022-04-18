package com.gjgs.gjgs.modules.bulletin.entity;

import com.gjgs.gjgs.modules.bulletin.dto.CreateBulletinRequest;
import com.gjgs.gjgs.modules.bulletin.enums.Age;
import com.gjgs.gjgs.modules.exception.team.TeamNotRecruitmentException;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.team.entity.Team;
import com.gjgs.gjgs.modules.utils.base.BaseEntity;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
public class Bulletin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BULLETIN_ID")
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "TEAM_ID", nullable = false)
    private Team team;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "LECTURE_ID", nullable = false)
    private Lecture lecture;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    // true 보이는 것, false 닫힌 것
    @Column(nullable = false)
    private boolean status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Age age;

    @Column(nullable = false)
    private String dayType;

    @Column(nullable = false)
    private String timeType;

    public static Bulletin of(Long bulletinId) {
        return Bulletin.builder().id(bulletinId).build();
    }

    public static Bulletin of(CreateBulletinRequest request) {
        return Bulletin.builder()
                .title(request.getTitle())
                .description(request.getText())
                .status(true)
                .age(Age.valueOf(request.getAge()))
                .dayType(request.getDayType())
                .timeType(request.getTimeType())
                .build();
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }

    public void modify(CreateBulletinRequest request) {
        this.title = request.getTitle();
        this.description = request.getText();
        this.age = Age.valueOf(request.getAge());
        this.timeType = request.getTimeType();
        this.dayType = request.getDayType();
        this.team.checkTeamIsFullDoThrow();
        this.status = true;
    }

    public void stopRecruit() {
        if (status) {
            status = false;
        }
    }

    public void changeRecruitStatus() {
        if (status) {
            stopRecruit();
        } else {
            team.checkTeamIsFullDoThrow();
            status = true;
        }
    }

    public boolean isDifferenceLecture(Long requestLectureId) {
        return !getLectureId().equals(requestLectureId);
    }

    public Long getLectureId() {
        return getLecture().getId();
    }

    public void checkRecruit() {
        if (!isStatus()) {
            throw new TeamNotRecruitmentException();
        }
    }
}
