import React from "react";
import LectureController from "../../../services/controllers/LectureController";
import ReviewController from "../../../services/controllers/ReviewController";
import MyClassHeaderButton from "../myClass/MyClassHeaderButton";
import { MyClassInfo } from "../myClass/MyClassView";
import QuestionItem from "../questionManage/QuestionItem";
import ReviewItem, { ReviewInfo } from "./ReviewItem";
import Cookies from "universal-cookie";

const cookies = new Cookies();

interface ReviewManageViewProps {
  path: string;
}

interface ReviewManageViewStates {
  tab: number;
  reviews: ReviewInfo[];
  openedIds: number[];
  on: boolean;
  on2: boolean;
  classes: MyClassInfo[];
  lectureId: number;
}

export default class ReviewManageView extends React.Component<
  ReviewManageViewProps,
  ReviewManageViewStates
> {
  constructor(props: any) {
    super(props);
    this.state = {
      tab: 0,
      reviews: [],
      openedIds: [],
      on: false,
      on2: false,
      classes: [],
      lectureId: 0,
    };
    this.getLectures("PROGRESS");
  }
  private async getLectures(condition: string) {
    const params = {
      condition,
    };

    try {
      const res = await LectureController.getMyClass(params);
      if (!res) return;
      const lectureList = res.data.lectureList;

      this.setState({ classes: lectureList }, () =>
        this.getAllReviews(cookies.get("memberId"))
      );
    } catch (e) {}
  }

  private async getAllReviews(directorId: number) {
    try {
      const res = await ReviewController.getAllReviews(directorId);
      this.setState({ reviews: res?.data.content });
    } catch (e) {}
  }

  private async getLectureReviews(lectureId: number) {
    try {
      const res = await ReviewController.getLectureReviews(lectureId);
      this.setState({ reviews: res?.data.content });
    } catch (e) {}
  }

  private checkAnswer(answer: string) {
    if (!answer) {
      alert("답변을 입력해주세요.");
      return false;
    }

    if (answer.length < 10 || answer.length > 1000) {
      alert("답변은 10자이상 1000자이하로 작성해주세요.");
      return false;
    }

    return true;
  }

  private async patchAnswer(answer: string, reviewId: number) {
    const params = { replyText: answer };

    if (!this.checkAnswer(answer)) return;

    try {
      await ReviewController.patchAnswer(params, reviewId);
      alert("답변등록이 완료되었습니다.");
      window.location.href = "/main/review-manage";
    } catch (e) {}
  }

  render() {
    return (
      <div className="myclass-container">
        <div className="myclass-header pretendard">리뷰 관리</div>
        <div className="myclass-list-wrapper">
          <div className="myclass-splitor" />
          <div className="question-manage-header">
            <div className="pretendard">{`전체 ${this.state.reviews.length}개`}</div>
            <select
              className="class-add-curriculum-info-select pretendard meme"
              value={this.state.lectureId}
              onChange={(e: any) =>
                this.setState({ lectureId: +e.target.value }, () => {
                  if (this.state.lectureId === 0) {
                    this.getAllReviews(cookies.get("memberId"));
                  } else {
                    this.getLectureReviews(this.state.lectureId);
                  }
                })
              }
            >
              <option value={0}>전체</option>
              {this.state.classes.map((myclass: MyClassInfo) => (
                <option value={myclass.savedLectureId}>{myclass.title}</option>
              ))}
            </select>
          </div>
          <div className="question-manage-list">
            {this.state.reviews.map((item: ReviewInfo, index: number) => (
              <div>
                <ReviewItem
                  key={index}
                  reviewInfo={item}
                  open={this.state.openedIds.includes(item.reviewId)}
                  onClick={() => {
                    let ids = this.state.openedIds;

                    if (ids.includes(item.reviewId)) {
                      ids = ids.filter((id: number) => id !== item.reviewId);
                    } else {
                      ids.push(item.reviewId);
                    }
                    this.setState({ openedIds: ids });
                  }}
                  buttonVisible={this.state.tab === 0}
                  onSend={(text: string) =>
                    this.patchAnswer(text, item.reviewId)
                  }
                />
                <div className="myclass-splitor" />
              </div>
            ))}
          </div>
        </div>
      </div>
    );
  }
}
