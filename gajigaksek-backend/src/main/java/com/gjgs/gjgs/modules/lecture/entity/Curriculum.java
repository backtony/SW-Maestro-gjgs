package com.gjgs.gjgs.modules.lecture.entity;

import com.gjgs.gjgs.modules.exception.lecture.CurriculumFileNotEqualException;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLecture;
import com.gjgs.gjgs.modules.utils.base.BaseEntity;
import com.gjgs.gjgs.modules.utils.vo.FileInfoVo;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
public class Curriculum extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "CURRICULUM_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LECTURE_ID", nullable = false)
    private Lecture lecture;

    @Column(nullable = false)
    private int orders;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String detailText;

    @Column(nullable = false)
    private String curriculumImageName;

    @Column(nullable = false)
    private String curriculumImageUrl;

    public static List<Curriculum> of(List<CreateLecture.CurriculumDto> curriculumDto,
                                      List<FileInfoVo> fileInfoVoList,
                                      Lecture lecture) {
        if (curriculumDto.size() != fileInfoVoList.size()) {
            throw new CurriculumFileNotEqualException();
        }
        return getCurriculumList(curriculumDto, fileInfoVoList, lecture);
    }

    private static List<Curriculum> getCurriculumList(List<CreateLecture.CurriculumDto> curriculumDtoList,
                                                      List<FileInfoVo> fileInfoVoList,
                                                      Lecture lecture) {
        int size = curriculumDtoList.size();
        List<Curriculum> curriculumEntityList = new ArrayList<>();
        for(int i = 0; i < size; i++) {
            curriculumEntityList.add(Curriculum.of(curriculumDtoList.get(i), fileInfoVoList.get(i), lecture));
        }
        return curriculumEntityList;
    }

    private static Curriculum of(CreateLecture.CurriculumDto curriculumDto,
                                 FileInfoVo fileInfoVo,
                                 Lecture lecture) {
        return Curriculum.builder()
                .lecture(lecture)
                .orders(curriculumDto.getOrder())
                .title(curriculumDto.getTitle())
                .detailText(curriculumDto.getDetailText())
                .curriculumImageName(fileInfoVo.getFileName())
                .curriculumImageUrl(fileInfoVo.getFileUrl())
                .build();
    }
}
