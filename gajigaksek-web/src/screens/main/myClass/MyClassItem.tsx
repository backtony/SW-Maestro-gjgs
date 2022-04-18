import React from "react";
import MyclassCouponPopup from "./MyClassCouponPopup";
import "./MyClassItem.css";
import Cookies from "universal-cookie";
import LectureController from "../../../services/controllers/LectureController";

const cookies = new Cookies();

interface MyClassItemProps {
  title: string;
  status: string;
  finished: boolean;
  lectureId: number;
  rejectReason: string;
}

interface MyClassItemStates {
  popup: boolean;
}

export default class MyClassItem extends React.Component<
  MyClassItemProps,
  MyClassItemStates
> {
  constructor(props: any) {
    super(props);
    this.state = { popup: false };
  }

  private async patchRejectLecture(lectureId: number) {
    try {
      await LectureController.patchRejectLecture(lectureId);
      window.location.href = "/main/add/class/first";
    } catch (e) {}
  }

  private async deleteWritenLecture() {
    try {
      await LectureController.deleteWritenLecture();
      this.patchRejectLecture(this.props.lectureId);
    } catch (e) {}
  }

  render() {
    return (
      <div className="myclass-item">
        <div className="myclass-img-wrapper">
          <img src="https://images.unsplash.com/flagged/photo-1561668038-2742fcef75d7?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1050&q=80" />
          {this.props.status === "ACCEPT" && this.props.finished && (
            <div className="myclass-img-blocker">
              <div>종료</div>
            </div>
          )}
          {this.props.status === "CONFIRM" && (
            <div className="myclass-img-blocker">
              <div>검수중</div>
            </div>
          )}
          {this.props.status === "CREATING" && (
            <div className="myclass-img-blocker">
              <div>작성중</div>
            </div>
          )}
        </div>
        <div className="myclass-item-contents">
          <div>
            <div className="myclass-item-title pretendard">
              {this.props.title}
            </div>
            {this.props.status === "ACCEPT" && !this.props.finished && (
              <button
                className="myclass-item-button"
                onClick={() => {
                  cookies.set(
                    "schedule_lectureId",
                    JSON.stringify(this.props.lectureId)
                  );
                  window.location.href = "/main/edit-schedule";
                }}
              >
                <div>스케줄 수정하기</div>
                <i className="fas fa-chevron-right fa"></i>
              </button>
            )}
            {this.props.status === "CREATING" && (
              <button
                className="myclass-item-button"
                onClick={() => (window.location.href = "/main/add/class/first")}
              >
                <div>계속 작성하기</div>
                <i className="fas fa-chevron-right fa"></i>
              </button>
            )}
            {this.props.status === "REJECT" && (
              <button
                className="myclass-item-button"
                onClick={async () => {
                  const result = confirm(
                    "작성중인 클래스로 변경하시겠습니까? (기존의 작성중인 클래스는 삭제됩니다.)"
                  );
                  if (result) {
                    await this.deleteWritenLecture();
                    await this.patchRejectLecture(this.props.lectureId);
                  }
                }}
              >
                <div>수정하기</div>
                <i className="fas fa-chevron-right fa"></i>
              </button>
            )}
          </div>
          {this.props.status === "ACCEPT" && !this.props.finished && (
            <button onClick={() => this.setState({ popup: true })}>
              <i className="fas fa-birthday-cake fa"></i>
              <div className="pretendard">작가 쿠폰 발급</div>
            </button>
          )}
          {this.props.status === "REJECT" && (
            <div>
              <div className="pretendard myclass-item-reject-text1">
                거부 사유
              </div>
              <div>{this.props.rejectReason}</div>
            </div>
          )}
        </div>
        <MyclassCouponPopup
          lectureId={this.props.lectureId}
          on={this.state.popup}
          setVisible={(visible: boolean) => this.setState({ popup: visible })}
        />
      </div>
    );
  }
}
