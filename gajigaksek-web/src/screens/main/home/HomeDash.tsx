import React from "react";
import LoginController from "../../../services/controllers/LoginController";
import "./HomeDash.css";
import Cookies from "universal-cookie";

const cookies = new Cookies();

interface HomeDashProps {
  onClickEdit: () => void;
}

interface HomeDashState {
  profile: any;
}

export default class HomeDash extends React.Component<
  HomeDashProps,
  HomeDashState
> {
  constructor(props: HomeDashProps) {
    super(props);
    this.state = { profile: undefined };
    this.getProfile(cookies.get("memberId"));
  }

  private async getProfile(memberId: number) {
    if (!memberId) return;
    try {
      const res = await LoginController.getProfile(memberId);
      this.setState({ profile: res?.data });
    } catch (e) {}
  }
  render() {
    return (
      <div className="home-dash-container">
        <div className="home-profile-container">
          <img
            className="home-profile-img"
            src={
              this.state.profile
                ? this.state.profile.imageFileUrl
                : "https://images.unsplash.com/photo-1526413232644-8a40f03cc03b?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=334&q=80"
            }
          />
          <div className="home-profile-nickname pretendard">{`디렉터 ${
            this.state.profile ? this.state.profile.nickname : ""
          }`}</div>
          <button
            className="home-profile-button pretendard"
            onClick={() => this.props.onClickEdit()}
          >
            작가 프로필 편집
          </button>
        </div>
        <div className="home-right-container">
          <div className="home-class-account">
            <div className="home-class-account-left">
              <div className="home-class-account-title pretendard">
                클래스 누적 정산 금액
              </div>
              <div className="home-class-account-price pretendard">
                2,123,424원
              </div>
            </div>
            <button
              className="home-profile-button pretendard"
              onClick={() => {
                // this.getLecture();
                window.location.href = "/main/add/class/first";
              }}
            >
              클래스 등록하기
            </button>
          </div>
          <div className="home-info">
            <div className="home-info-class-count">
              <div className="home-info-class-count-title pretendard">
                운영중인 클래스 수
              </div>
              <div className="home-info-class-count-text pretendard">5개</div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}
