import React from "react";
import NotificationController from "../../../services/controllers/NotificationController";
import "./BackofficeNotificationPopup.css";

interface BackofficeNotificationPopupProps {
  on: boolean;
  memberIds: number[];
  setVisible: (visible: boolean) => void;
}

interface BackofficeNotificationPopupStates {
  type: string;
  title: string;
  text: string;
}

export default class BackofficeNotificationPopup extends React.Component<
  BackofficeNotificationPopupProps,
  BackofficeNotificationPopupStates
> {
  constructor(props: any) {
    super(props);
    this.state = { type: "ALL", title: "", text: "" };
  }

  private checkForm(params: any) {
    if (!params.title || params.title.length <= 0) {
      alert("제목을 입력해주세요.");
      return false;
    }
    if (!params.message || params.message.length <= 0) {
      alert("본문을 입력해주세요.");
      return false;
    }

    if (params.targetType === "SELECT" && params.memberIdList.length <= 0) {
      alert("멤버를 선택해주세요.");
      return false;
    }

    return true;
  }

  private async postNotification(title: string, text: string) {
    const params = {
      title,
      message: text,
      targetType: this.state.type,
      memberIdList: this.props.memberIds,
    };

    if (!this.checkForm(params)) return;

    console.log(params);

    try {
      await NotificationController.postNotification(params);
      alert("알림을 보냈습니다.");
      this.props.setVisible(false);
    } catch (e) {}
  }

  render() {
    return (
      <div
        className={`home-popup-container ${
          this.props.on ? "home-popup-container-on" : ""
        }`}
      >
        <div className="class-add-schedule-popup">
          <div className="home-popup-title-container">
            <div className="home-popup-title pretendard">알림 보내기</div>
            <button
              className="home-popup-exit"
              onClick={() => this.props.setVisible(!this.props.on)}
            >
              <i className="fas fa-times fa-2x"></i>
            </button>
          </div>
          <div className="class-add-schedule-popup-main-container">
            <div className="class-add-price-coupon-coupon-wrapper">
              <div className="class-add-price-coupon-coupon-title pretendard">
                대상
              </div>
              <button
                className={`class-manage-button backoffice-noti-popup-button pretendard ${
                  this.state.type === "ALL" ? "class-manage-button-on" : ""
                }`}
                onClick={() => this.setState({ type: "ALL" })}
              >
                모두에게
              </button>
              <button
                className={`class-manage-button backoffice-noti-popup-button pretendard ${
                  this.state.type === "SELECT" ? "class-manage-button-on" : ""
                }`}
                onClick={() => this.setState({ type: "SELECT" })}
              >
                선택한 회원에게
              </button>
            </div>

            <div className="class-add-price-coupon-coupon-wrapper">
              <div className="class-add-price-coupon-coupon-title pretendard">
                제목
              </div>
              <div className="class-add-price-coupon-coupon-input-wrapper">
                <input
                  className="home-popup-nickname-input pretendard"
                  value={this.state.title}
                  onChange={(e: any) =>
                    this.setState({ title: e.target.value })
                  }
                ></input>
              </div>
            </div>
            <div className="home-popup-intro-container">
              <div className="class-add-price-coupon-coupon-title pretendard">
                내용
              </div>
              <div className="home-popup-intro-textarea-wrapper">
                <textarea
                  className="home-popup-intro-textarea pretendard"
                  value={this.state.text}
                  onChange={(e: any) => this.setState({ text: e.target.value })}
                ></textarea>
              </div>
            </div>
            <button
              className="pretendard"
              onClick={() =>
                this.postNotification(this.state.title, this.state.text)
              }
            >
              보내기
            </button>
          </div>
        </div>
      </div>
    );
  }
}
