import React from "react";
// import './App.css';
import "./LoginView.css";

import KakaoLogin from "react-kakao-login";
import Cookies from "universal-cookie";
import LoginController from "../../services/controllers/LoginController";
import FirebaseAuth from "../../services/FirebaseAuth";
const cookies = new Cookies();

const token = "650a7b335e49d69b35c6b8a86f1f06ba";

declare let window: any;

export default class LoginView extends React.Component<
  Record<string, never>,
  { isLogin: boolean }
> {
  constructor(props: any) {
    super(props);
    this.state = { isLogin: false };
  }

  private onSuccess(res: any) {
    console.log("onSuccess: ", res);
    const kakaoAccessToken = res.response.access_token;
    const kakaoRefreshToken = res.response.refresh_token;
    const kakaoId = res.profile.id;

    cookies.set("kakao_access_token", kakaoAccessToken, { sameSite: "strict" });
    cookies.set("kakao_refresh_token", kakaoRefreshToken, {
      sameSite: "strict",
    });

    cookies.set("kakao_id", kakaoId, { sameSite: "strict" });

    this.setState({ isLogin: true });

    this.postLogin(kakaoAccessToken, kakaoId);
  }

  private async getProfile(memberId: number) {
    try {
      const res = await LoginController.getProfile(memberId);
      const data = res?.data;
      cookies.set("nickname", data.nickname);
    } catch (e) {}
  }

  private async postLogin(kakaoAccessToken: string, kakaoId: number) {
    if (!(await FirebaseAuth.signInWithFirebase(kakaoId))) {
      console.error("Firebase login Fail!");
    }

    try {
      const res = await LoginController.login(kakaoAccessToken);

      if (!res?.data.tokenDto) {
        alert("최초로그인시 앱에서 로그인해주세요.");
        return;
      }

      cookies.set("memberId", res.data.memberId);
      cookies.set("accessToken", res.data.tokenDto.accessToken);
      cookies.set("refreshToken", res.data.tokenDto.refreshToken);

      this.getProfile(res.data.memberId);

      window.location.href = "/main/home";
    } catch (e) {
      // console.error(e);
    }
  }

  private onFail(res: any) {
    console.log("onFail: ", res);
  }

  private onLogout() {
    this.setState({ isLogin: false });

    cookies.remove("kakao_access_token");
    cookies.remove("kakao_refresh_token");

    alert("다시 로그인 해주세요.");
  }

  render() {
    return (
      <div className="login-container">
        <img
          className="login-img"
          src="https://images.unsplash.com/photo-1485546784815-e380f3297414?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1650&q=80"
        />
        <div className="login-inner-container">
          <div className="login-title-container">
            <div className="login-title-wrapper">
              <div className="jalnan login-big-title">가</div>
              <div className="jalnan login-small-title">까운</div>
            </div>
            <div className="login-title-wrapper">
              <div className="jalnan login-big-title">지</div>
              <div className="jalnan login-small-title">역 사람들과</div>
            </div>
            <div className="login-title-wrapper">
              <div className="jalnan login-big-title">각</div>
              <div className="jalnan login-small-title">종</div>
            </div>
            <div className="login-title-wrapper">
              <div className="jalnan login-big-title">색</div>
              <div className="jalnan login-small-title">다른 취미 즐기기</div>
            </div>
          </div>
          <div className="login-button-container">
            <KakaoLogin
              token={token}
              onSuccess={(res: any) => this.onSuccess(res)}
              onFail={(res: any) => this.onFail(res)}
              onLogout={() => this.onLogout()}
              render={({ onClick }) => {
                return (
                  <button
                    className="login-button"
                    onClick={(e) => {
                      e.preventDefault();
                      onClick();
                    }}
                  >
                    <i className="fas fa-comment fa-2x"></i>
                    <div className="pretendard">카카오로 로그인하기</div>
                  </button>
                );
              }}
            />
          </div>
        </div>
      </div>
    );
  }
}

function Home1() {
  return <h2>Home</h2>;
}

function About() {
  return <h2>About</h2>;
}

function Users() {
  return <h2>Users</h2>;
}
