import {KakaoOAuthToken} from '@react-native-seoul/kakao-login';
import LoginAgent from '../agents/LoginAgent';
import {TokenDto} from '../login/User';
import UserDC from '../login/UserDC';
import messaging from '@react-native-firebase/messaging';
import AsyncStorage from '@react-native-async-storage/async-storage';
import {Alert} from 'react-native';

class LoginApiController {
  public async getNewAccessToken(tokenDto: TokenDto) {
    try {
      const res = await LoginAgent.reissue(tokenDto.refreshToken);
      await UserDC.setData(res.data);
      AsyncStorage.setItem('user', JSON.stringify(UserDC.getUser()));

      return res.data;
    } catch (e) {
      console.error(e);
      this.logout(tokenDto);
      UserDC.removeCurrentUser();
      if (e === 'KAKAO-401') {
        Alert.alert('알림', '카카오 로그인을 시도해주세요.');
      }
    }
  }
  public async logout(tokenDto: TokenDto) {
    try {
      const res = LoginAgent.logout(tokenDto);
      return res;
    } catch (e) {
      console.error(e);
    }
  }

  public async login(kakaoToken: KakaoOAuthToken) {
    try {
      const fcmToken = await messaging().getToken();
      console.log('fcmToken: ', fcmToken);
      const res = LoginAgent.login(kakaoToken, fcmToken);
      return res;
    } catch (e) {
      console.error(e);
    }
  }

  public async firstLogin(params: any) {
    try {
      const fcmToken = await messaging().getToken();
      console.log('fcmToken: ', fcmToken);
      const res = await LoginAgent.firstLogin(params, fcmToken);
      console.log('LoginApiController res: ', res);
      return res;
    } catch (e) {
      console.error(e);
      // alert('다시 시도 해주세요.');
      throw e;
    }
  }

  public async postFakeLogin(username: string) {
    try {
      const res = LoginAgent.postFakeLogin(username);
      return res;
    } catch (e) {
      console.error(e);
    }
  }
}

export default new LoginApiController();
