import React from "react";
import Cookies from "universal-cookie";
import NoticeController from "../../../../services/controllers/NoticeController";
import "./HomeBulletinDetail.css";

const cookies = new Cookies();

interface NoticeDetailInfo {
  createdDate: string;
  noticeId: number;
  text: string;
  title: string;
}

interface HomeBulletinDetailStates {
  noticeInfo: NoticeDetailInfo;
}

export default class HomeBulletinDetail extends React.Component<
  Record<string, never>,
  HomeBulletinDetailStates
> {
  constructor(props: any) {
    super(props);
    this.state = {
      noticeInfo: {
        createdDate: "2021-09-30T02:38:54.178Z",
        noticeId: 0,
        text: `
        안녕하세요. 디렉터님. 가지각색입니다:)

        수강생이 클래스를 확인할 때 가장 큰 영향을 주는 것이 바로 커버 이미지(썸네일)이에요!
        디렉터님과 클래스의 특징과 강점이 잘 드러나도록 커버 이미지를 변경해주세요!
        
        이미지는 이렇게 등록해주세요!
        
        + 사진은 최소 3장 이상을 등록해주세요
        + 이미지 사이즈는 가로형 14:9의 고해상도 이미지를 권장해요
        + 이미지 용량은 10MB 이하로 등록해주세요
        + 파일 형식은 jpeg, jpg, png만 가능해요
        `,
        title: "커버 이미지를 변경해주세요!",
      },
    };
    this.getNoticeDetail(cookies.get("noticeId"));
  }

  private async getNoticeDetail(noticeId: number) {
    try {
      const res = await NoticeController.getNoticeDetail(noticeId);
      this.setState({ noticeInfo: res?.data });
    } catch (e) {}
  }
  render() {
    return (
      <div className="class-add-first-container">
        <div className="class-add-first-header">
          <button
            className="class-add-first-back"
            onClick={() => (window.location.href = "/main/home")}
          >
            <i className="fas fa-chevron-left fa-3x"></i>
          </button>
          <div className="class-add-first-header-title pretendard">
            공지사항
          </div>
        </div>
        <div className="class-add-first-name-container bulletin-detail-title">
          <div className="class-add-mainCategory-title pretendard">제목</div>
          <div className="class-add-first-name-wrapper">
            <div>{this.state.noticeInfo.title}</div>
          </div>
        </div>
        <div className="class-add-first-name-container">
          <div className="class-add-mainCategory-title pretendard">내용</div>
          <div className="class-add-intro-input-wrapper bulletin-detail-text">
            <textarea
              className="pretendard"
              value={this.state.noticeInfo.text}
            ></textarea>
            {/* <div>{this.state.noticeInfo.text}</div> */}
          </div>
        </div>
      </div>
    );
  }
}
