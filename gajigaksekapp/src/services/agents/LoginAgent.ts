import {KakaoOAuthToken} from '@react-native-seoul/kakao-login';
import axios from 'axios';
import {API_URL} from '../../utils/commonParam';
import {TokenDto} from '../login/User';

class LoginAgent {
  public async reissue(refreshToken: String) {
    try {
      const res = await axios.post(`${API_URL}/reissue`, null, {
        headers: {
          Authorization: `Bearer ${refreshToken}`,
        },
      });
      return res;
    } catch (e) {
      console.error('LoginAgent', e);
      return e.response.data.code;
    }
  }

  public async login(kakaoToken: KakaoOAuthToken, fcmToken: string) {
    try {
      const res = await axios.post(
        `${API_URL}/login/kakao`,
        {fcmToken},
        {
          headers: {
            KakaoAccessToken: `Bearer ${kakaoToken.accessToken}`,
          },
        },
      );
      return res;
    } catch (e) {
      console.error(e);
      alert('로그인을 다시 시도해주세요.');
      throw e.response.data.code;
    }
  }

  public async logout(tokenDto: TokenDto) {
    try {
      const res = await axios.post(`${API_URL}/logout`, null, {
        headers: {
          Authorization: `Bearer ${tokenDto.accessToken}`,
          RefreshToken: `Bearer ${tokenDto.refreshToken}`,
        },
      });
      return res;
    } catch (e) {
      console.error(e);
      alert('로그아웃을 다시 시도해주세요.');
      throw e.response.data.code;
    }
  }

  public async firstLogin(params: any, fcmToken: string) {
    const {
      age,
      id,
      imageFileUrl,
      name,
      nickname,
      phone,
      sex,
      zoneId,
      categoryIdList,
      recommendNickname,
    } = params;
    console.log('firstLogin params: ', params);
    try {
      const res = await axios.post(`${API_URL}/sign-up`, {
        id,
        imageFileUrl,
        name,
        phone,
        nickname,
        age,
        sex,
        zoneId,
        categoryIdList,
        fcmToken,
        recommendNickname,
      });
      console.log('first login res: ', res);
      return res;
    } catch (e) {
      alert(e.response.data.message);
      throw e;
    }
  }

  public async postFakeLogin(username: string) {
    try {
      const res = await axios.post(`${API_URL}/fake/login/${username}`, null, {
        headers: {},
      });
      return res;
    } catch (e) {
      console.error(e);
      alert('로그인을 다시 시도해주세요.');
      throw e.response.data.code;
    }
  }
}

export default new LoginAgent();
