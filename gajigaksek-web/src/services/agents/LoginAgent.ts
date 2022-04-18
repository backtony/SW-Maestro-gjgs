import axios from "axios";

class LoginAgent {
  public async login(kakaoAccessToken: string) {
    try {
      const res = await axios.post("/api/v1/web/director/login", null, {
        headers: {
          KakaoAccessToken: `Bearer ${kakaoAccessToken}`,
        },
      });
      return res;
    } catch (e) {
      console.error(e);
      if (e.response.data.code === "MEMBER-403") {
        alert("디렉터로 전환 후 로그인해주세요.");
      }
      throw e;
    }
  }

  public async usernameLogin(username: string) {
    try {
      const res = await axios.post(`/api/v1/fake/login/${username}`, null, {
        headers: {},
      });
      return res;
    } catch (e) {
      throw e;
    }
  }

  public async getProfile(memberId: number) {
    try {
      const res = await axios.get(`/api/v1/mypage/info/${memberId}`);
      return res;
    } catch (e) {
      console.error(e);
    }
  }

  public async postProfileImg(formData: FormData, accessToken: string) {
    try {
      const res = await axios.post("/api/v1/mypage/image", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
          Authorization: `Bearer ${accessToken}`,
        },
      });
      return res;
    } catch (e) {
      console.error(e);
      throw e.response.data.code;
    }
  }

  public async putNickname(nickname: string, accessToken: string) {
    try {
      const res = await axios.put(
        "/api/v1/mypage/nickname",
        { nickname },
        {
          headers: { Authorization: `Bearer ${accessToken}` },
        }
      );
      return res;
    } catch (e) {
      throw e;
    }
  }

  public async putProfileText(params: any, accessToken: string) {
    try {
      const res = await axios.put("/api/v1/mypage/profile-text", params, {
        headers: { Authorization: `Bearer ${accessToken}` },
      });
      return res;
    } catch (e) {
      throw e;
    }
  }

  public async postAdminLogin(kakaoAccessToken: string) {
    try {
      const res = await axios.post("/api/v1/web/admin/login", null, {
        headers: {
          KakaoAccessToken: `Bearer ${kakaoAccessToken}`,
        },
      });
      return res;
    } catch (e) {
      console.error(e);
      if (e.response.data.code === "MEMBER-403") {
        alert("admin로 전환 후 로그인해주세요.");
      }
      throw e;
    }
  }
}

export default new LoginAgent();
