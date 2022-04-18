import LoginAgent from "../agents/LoginAgent";
import Cookies from "universal-cookie";
const cookies = new Cookies();

class LoginController {
  public async login(kakaoAccessToken: string) {
    try {
      const res = LoginAgent.login(kakaoAccessToken);
      return res;
    } catch (e) {
      console.error(e);
      alert("로그인을 다시 시도해주세요.");
    }
  }

  public async usernameLogin(username: string) {
    try {
      const res = LoginAgent.usernameLogin(username);
      return res;
    } catch (e) {
      console.error(e);
    }
  }

  public async getProfile(memberId: number) {
    try {
      const res = LoginAgent.getProfile(memberId);
      return res;
    } catch (e) {}
  }

  public async postProfileImg(formData: FormData) {
    const accessToken = cookies.get("accessToken");
    try {
      const res = LoginAgent.postProfileImg(formData, accessToken);
      return res;
    } catch (e) {
      console.error(e);
    }
  }

  public async putNickname(nickname: string) {
    const accessToken = cookies.get("accessToken");
    try {
      const res = LoginAgent.putNickname(nickname, accessToken);
      return res;
    } catch (e) {
      console.error(e);
    }
  }

  public async putProfileText(params: any) {
    const accessToken = cookies.get("accessToken");
    try {
      const res = LoginAgent.putProfileText(params, accessToken);
      return res;
    } catch (e) {
      console.error(e);
    }
  }

  public async postAdminLogin(kakaoAccessToken: string) {
    try {
      const res = LoginAgent.postAdminLogin(kakaoAccessToken);
      return res;
    } catch (e) {
      console.error(e);
      alert("로그인을 다시 시도해주세요.");
    }
  }
}

export default new LoginController();
