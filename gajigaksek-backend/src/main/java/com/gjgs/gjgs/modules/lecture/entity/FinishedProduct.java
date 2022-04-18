package com.gjgs.gjgs.modules.lecture.entity;

import com.gjgs.gjgs.modules.exception.lecture.ProductAndFileNotEqualException;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLecture;
import com.gjgs.gjgs.modules.utils.base.BaseEntity;
import com.gjgs.gjgs.modules.utils.vo.FileInfoVo;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
public class FinishedProduct extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY) @Column(name = "FINISHED_PRODUCT_ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "LECTURE_ID", nullable = false)
    private Lecture lecture;

    @Column(nullable = false)
    private int orders;

    @Column(nullable = false)
    private String finishedProductImageName;

    @Column(nullable = false)
    private String finishedProductImageUrl;

    @Column(nullable = false)
    private String text;

    public static List<FinishedProduct> of(List<CreateLecture.FinishedProductInfoDto> productInfoList,
                                           List<FileInfoVo> productImageInfoList,
                                           Lecture lecture) {
        if (productInfoList.size() != productImageInfoList.size()) {
            throw new ProductAndFileNotEqualException();
        }
        return getFinishedProductList(productInfoList, productImageInfoList, lecture);
    }

    private static List<FinishedProduct> getFinishedProductList(List<CreateLecture.FinishedProductInfoDto> productInfoList,
                                                                List<FileInfoVo> productImageInfoList,
                                                                Lecture lecture) {
        int size = productImageInfoList.size();
        List<FinishedProduct> productList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            productList.add(FinishedProduct.of(productInfoList.get(i), productImageInfoList.get(i), lecture));
        }
        return productList;
    }

    private static FinishedProduct of(CreateLecture.FinishedProductInfoDto productInfo, FileInfoVo fileInfoVo, Lecture lecture) {
        return FinishedProduct.builder()
                .lecture(lecture)
                .finishedProductImageName(fileInfoVo.getFileName())
                .finishedProductImageUrl(fileInfoVo.getFileUrl())
                .orders(productInfo.getOrder())
                .text(productInfo.getText())
                .build();
    }
}
