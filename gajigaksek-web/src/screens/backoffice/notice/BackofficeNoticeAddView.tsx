import React from "react";
import NoticeController from "../../../services/controllers/NoticeController";
import "./BackofficeNoticeAddView.css";

interface BackofficeNoticeAddViewStates {
  title: string;
  text: string;
  noticeType: string;
}

export default class BackofficeNoticeAddView extends React.Component<
  Record<string, never>,
  BackofficeNoticeAddViewStates
> {
  constructor(props: any) {
    super(props);
    this.state = { title: "", text: "", noticeType: "ALL" };
  }

  private checkForm(params: any) {
    if (!params.title) {
      alert("제목을 입력하세요.");
      return false;
    }

    if (!params.text) {
      alert("본문을 입력하세요.");
      return false;
    }

    return true;
  }

  private async postNotice() {
    const params = {
      title: this.state.title,
      text: this.state.text,
      noticeType: this.state.noticeType,
    };

    if (!this.checkForm(params)) return;

    try {
      await NoticeController.postNotice(params);
      window.location.reload();
    } catch (e) {}
  }

  render() {
    return (
      <div className="backoffice-notice-add-container">
        <div className="backoffice-notice-add-title-wrapper">
          <div className="pretendard">대상</div>
          <select
            className="class-add-curriculum-info-select pretendard meme"
            value={this.state.noticeType}
            onChange={(e: any) => this.setState({ noticeType: e.target.value })}
          >
            <option value="ALL">전체</option>
            <option value="DIRECTOR">디렉터</option>
          </select>
        </div>
        <div className="backoffice-notice-add-title-wrapper">
          <div className="pretendard">제목</div>
          <div className="class-add-price-coupon-price-item-price-wrapper">
            <input
              value={this.state.title}
              onChange={(e: any) => this.setState({ title: e.target.value })}
            ></input>
          </div>
        </div>
        <div className="backoffice-notice-add-title-wrapper">
          <div className="pretendard">본문</div>
          <div className="home-popup-intro-textarea-wrapper">
            <textarea
              className="pretendard"
              value={this.state.text}
              onChange={(e: any) => this.setState({ text: e.target.value })}
            ></textarea>
          </div>
        </div>
        <button onClick={() => this.postNotice()}>업로드</button>
      </div>
    );
  }
}
