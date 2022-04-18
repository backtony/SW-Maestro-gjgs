import React from "react";
import NoticeController from "../../../services/controllers/NoticeController";
import { NoticeInfo } from "../../main/home/bulletin/HomeBulletin";
import "./BackofficeNoticeBlock.css";

interface BackofficeNoticeBlockProps {
  notice: NoticeInfo;
  noticeType: string;
  open: boolean;
  onClick: (open: boolean) => void;
}

interface QuestionItemStates {
  title: string;
  text: string;
}

export default class BackofficeNoticeBlock extends React.Component<
  BackofficeNoticeBlockProps,
  QuestionItemStates
> {
  constructor(props: any) {
    super(props);
    this.state = { title: props.notice.title, text: props.notice.text };
  }

  private async putNotice() {
    const params = {
      noticeType: this.props.noticeType,
      text: this.state.text,
      title: this.state.title,
    };

    try {
      await NoticeController.putNotice(params, this.props.notice.noticeId);
      alert("수정되었습니다.");
      window.location.reload();
    } catch (e) {}
  }

  private async deleteNotice() {
    try {
      await NoticeController.deleteNotice(this.props.notice.noticeId);
      alert("삭제되었습니다.");
      window.location.reload();
    } catch (e) {}
  }

  render() {
    return (
      <div className="question-item">
        <button
          className="question-button"
          onClick={() => this.props.onClick(!this.props.open)}
        >
          <div>
            <div className="question-button-Q pretendard">N</div>
            <div className="question-button-title-wrapper">
              <div className="question-button-title pretendard">
                {this.props.notice.title}
              </div>
              <div className="question-button-class-title pretendard">
                {this.props.notice.text}
              </div>
            </div>
          </div>
          <div className="wtf22">
            <i className="fas fa-chevron-down fa-2x"></i>
          </div>
        </button>
        <div
          className={`question-item-content ${
            this.props.open
              ? "question-item-content-on"
              : "question-item-content-off"
          }`}
        >
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

          <div className="backoffice-notice-edit-block-bottom">
            <button className="pretendard" onClick={() => this.putNotice()}>
              수정하기
            </button>
            <button className="pretendard" onClick={() => this.deleteNotice()}>
              삭제하기
            </button>
          </div>
        </div>
      </div>
    );
  }
}
