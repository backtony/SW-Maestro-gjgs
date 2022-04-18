import {Alert} from 'react-native';
import NotificationAgent from '../agents/NotificationAgent';
import {TokenDto} from '../login/User';
import UserDC from '../login/UserDC';
import LoginApiController from './LoginApiController';

class NotificationController {
  public async postRead(uuid: string) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('postRead: ', tokenDto);
    try {
      const res = await NotificationAgent.postRead(uuid, tokenDto.accessToken);
      console.log('postRead: ', res);
      return res;
    } catch (e) {
      console.error(e);
      if (e === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        Alert.alert('다시 시도해주세요');
      } else if (e === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        Alert.alert('카카오 로그인을 다시 시도해주세요.');
      }
      throw e;
    }
  }

  public async getMyNotification() {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    try {
      const res = await NotificationAgent.getMyNotification(
        tokenDto.accessToken,
      );
      return res;
    } catch (e) {
      console.error(e);
      if (e === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        Alert.alert('다시 시도해주세요');
      } else if (e === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        Alert.alert('카카오 로그인을 다시 시도해주세요.');
      }
      throw e;
    }
  }
}

export default new NotificationController();
