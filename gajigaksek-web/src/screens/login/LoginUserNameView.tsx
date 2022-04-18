import React from "react";
import LoginController from "../../services/controllers/LoginController";
import FirebaseAuth from "../../services/FirebaseAuth";
import Cookies from "universal-cookie";

const cookies = new Cookies();

interface LoginUserNameViewStates {
  username: string;
}

export default class LoginUserNameView extends React.Component<
  Record<string, never>,
  LoginUserNameViewStates
> {
  constructor(props: Record<string, never>) {
    super(props);
    this.state = { username: "" };
  }

  private async getProfile(memberId: number) {
    try {
      const res = await LoginController.getProfile(memberId);
      const data = res?.data;
      cookies.set("nickname", data.nickname);
    } catch (e) {}
  }

  private async postLogin(username: string) {
    if (!(await FirebaseAuth.signInWithFirebase(20210909))) {
      console.error("Firebase login Fail!");
    }
    try {
      const res = await LoginController.usernameLogin(username);
      if (!res?.data.tokenDto) {
        alert("다시 로그인해주세요.");
        return;
      }

      cookies.set("memberId", res.data.memberId);
      cookies.set("accessToken", res.data.tokenDto.accessToken);
      cookies.set("refreshToken", res.data.tokenDto.refreshToken);
      this.getProfile(res.data.memberId);
      window.location.href = "/main/home";
    } catch (e) {}
  }
  render() {
    return (
      <div>
        <input
          value={this.state.username}
          onChange={(e: any) => this.setState({ username: e.target.value })}
        ></input>
        <button
          onClick={() => {
            this.postLogin(this.state.username);
          }}
        >
          login
        </button>
      </div>
    );
  }
}
