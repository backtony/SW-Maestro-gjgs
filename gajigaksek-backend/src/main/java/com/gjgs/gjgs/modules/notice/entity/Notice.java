package com.gjgs.gjgs.modules.notice.entity;

import com.gjgs.gjgs.modules.notice.dto.NoticeForm;
import com.gjgs.gjgs.modules.notice.dto.NoticeResponse;
import com.gjgs.gjgs.modules.notice.enums.NoticeType;
import com.gjgs.gjgs.modules.utils.base.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTICE_ID")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NoticeType noticeType;

    public static Notice from(NoticeForm noticeForm){
            return Notice.builder()
                    .noticeType(noticeForm.getNoticeType())
                    .text(noticeForm.getText())
                    .title(noticeForm.getTitle())
                    .build();
    }

    public void changeNotice(NoticeForm noticeForm){
        this.title = noticeForm.getTitle();
        this.text = noticeForm.getText();
        this.noticeType = noticeForm.getNoticeType();
    }

    public NoticeResponse toNoticeResponse(){
        return NoticeResponse.builder()
                .noticeId(this.id)
                .title(this.title)
                .text(this.text)
                .createdDate(getCreatedDate())
                .build();
    }

}
