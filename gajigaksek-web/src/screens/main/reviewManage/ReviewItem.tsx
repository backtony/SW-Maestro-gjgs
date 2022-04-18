import React from "react";

export interface ReviewInfo {
  nickname: string;
  profileImageFileUrl: string;
  replyText: string;
  reviewId: number;
  reviewImageFileUrl: string;
  score: number;
  text: string;
}

interface ReviewItemProps {
  reviewInfo: ReviewInfo;
  open: boolean;
  buttonVisible: boolean;
  onClick: (open: boolean) => void;
  onSend: (text: string) => void;
}

interface ReviewItemStates {
  answer: string;
}

export default class ReviewItem extends React.Component<
  ReviewItemProps,
  ReviewItemStates
> {
  constructor(props: any) {
    super(props);
    this.state = { answer: this.props.reviewInfo.replyText };
  }
  render() {
    return (
      <div className="question-item">
        <button
          className="question-button"
          onClick={() => this.props.onClick(!this.props.open)}
        >
          <div>
            <div className="question-button-Q pretendard">R</div>
            <div className="question-button-title-wrapper">
              <div className="question-button-title pretendard">
                {this.props.reviewInfo.nickname}
              </div>
              <div className="question-button-class-title pretendard">
                {this.props.reviewInfo.text}
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
          <div className="question-item-content-text">
            {this.props.reviewInfo.text}
          </div>
          <div className="question-input-wrapper">
            <textarea
              placeholder="답변내용을 입력해주세요."
              className="pretendard"
              value={
                this.props.buttonVisible
                  ? this.state.answer
                  : this.props.reviewInfo.replyText
              }
              onChange={(e: any) => this.setState({ answer: e.target.value })}
            ></textarea>
            <div>
              {this.props.buttonVisible && (
                <button
                  className="pretendard"
                  onClick={() => this.props.onSend(this.state.answer)}
                >
                  답변 등록
                </button>
              )}
            </div>
          </div>
        </div>
      </div>
    );
  }
}
