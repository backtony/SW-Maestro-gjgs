import React from "react";
import Category from "../../../utils/Category";
import "./BackofficeClassBlock.css";
import { BackofficeClassInfo } from "./BackofficeClassView";
import Cookies from "universal-cookie";
import AdminLectureController from "../../../services/controllers/AdminLectureController";

const cookies = new Cookies();

interface BackofficeClassBlockProps {
  lecture: BackofficeClassInfo;
}

interface BackofficeClassBlockStates {
  rejectReason: string;
}

export default class BackofficeClassBlock extends React.Component<
  BackofficeClassBlockProps,
  BackofficeClassBlockStates
> {
  constructor(props: any) {
    super(props);
    this.state = { rejectReason: "" };
  }

  private async postLecture(
    lectureId: number,
    decideType: string,
    rejectReason: string
  ) {
    console.log(lectureId);
    if (decideType === "REJECT" && rejectReason.length < 10) {
      alert("검수거절 이유를 10자이상 입력해주세요.");
      return;
    }
    try {
      await AdminLectureController.postLecture(
        lectureId,
        decideType,
        rejectReason
      );
      window.location.reload();
    } catch (e) {}
  }
  render() {
    return (
      <div>
        <div className="backoffice-class-block">
          <div>
            <div>
              <div className="pretendard backoffice-class-block-title">
                클래스 이름
              </div>
              <div className="pretendard backoffice-class-block-text">
                {this.props.lecture.lectureTitle}
              </div>
            </div>
            <div>
              <div className="pretendard backoffice-class-block-title">
                신청일
              </div>
              <div className="pretendard backoffice-class-block-text">
                {this.props.lecture.confirmDateTime}
              </div>
            </div>
          </div>
          <div>
            <div>
              <div className="pretendard backoffice-class-block-title">
                디렉터 이름
              </div>
              <div className="pretendard backoffice-class-block-text">
                {this.props.lecture.directorNickname}
              </div>
            </div>
            <div>
              <div className="pretendard backoffice-class-block-title">
                소분류
              </div>
              <div className="pretendard backoffice-class-block-text">
                {Category.getCategory(this.props.lecture.categoryId)[1]}
              </div>
            </div>
          </div>
          <button
            className="backoffice-class-block-button"
            onClick={() => {
              cookies.set(
                "admin-lectureId",
                JSON.stringify(this.props.lecture.lectureId)
              );
              window.location.href = "/backoffice/class-detail/first";
            }}
          >
            {"상세정보 조회 >"}
          </button>
        </div>
        <div className="dkdkdk123">
          <div className="class-add-first-name-container kkk">
            <div className="class-add-mainCategory-title pretendard uuu">
              거부 사유
            </div>
            <div className="class-add-first-name-wrapper dkdkdk1234">
              <input
                className="class-add-first-name-input pretendard"
                placeholder="거부 사유"
                value={this.state.rejectReason}
                onChange={(event: any) =>
                  this.setState({ rejectReason: event.target.value })
                }
              ></input>
            </div>
          </div>
        </div>
        <div className="backoffice-class-confirm-button-wrapper">
          <button
            className="pretendard"
            onClick={() =>
              this.postLecture(this.props.lecture.lectureId, "ACCEPT", "")
            }
          >
            승인
          </button>
          <button
            className="pretendard"
            onClick={() =>
              this.postLecture(
                this.props.lecture.lectureId,
                "REJECT",
                this.state.rejectReason
              )
            }
          >
            거부
          </button>
        </div>
      </div>
    );
  }
}
