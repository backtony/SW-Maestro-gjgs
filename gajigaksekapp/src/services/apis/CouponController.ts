import {Alert} from 'react-native';
import CouponAgent from '../agents/CouponAgent';
import NotificationAgent from '../agents/NotificationAgent';
import {TokenDto} from '../login/User';
import UserDC from '../login/UserDC';
import LoginApiController from './LoginApiController';

class CouponController {
  public async getCoupon(lectureId: number) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('getCoupon: ', tokenDto);
    try {
      const res = await CouponAgent.getCoupon(lectureId, tokenDto.accessToken);
      console.log('getCoupon: ', res);
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

  public async getMyCoupon() {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    try {
      const res = await CouponAgent.getMyCoupon(tokenDto.accessToken);
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

  public async postCoupon(lectureId: number) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    try {
      const res = await CouponAgent.postCoupon(lectureId, tokenDto.accessToken);
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

export default new CouponController();
