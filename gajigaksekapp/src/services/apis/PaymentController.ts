import {Alert} from 'react-native';
import CouponAgent from '../agents/CouponAgent';
import PaymentAgent from '../agents/PaymentAgent';
import {TokenDto} from '../login/User';
import UserDC from '../login/UserDC';
import LoginApiController from './LoginApiController';

class PaymentController {
  public async getPaymentInfo(payType: string, scheduleId: number) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('getPaymentInfo: ', tokenDto);
    try {
      const res = await PaymentAgent.getPaymentInfo(
        payType,
        scheduleId,
        tokenDto.accessToken,
      );
      console.log('getPaymentInfo: ', res);
      return res;
    } catch (e) {
      console.error(e);
      if (e.response.data.code === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        Alert.alert('다시 시도해주세요');
      } else if (e.response.data.code === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        Alert.alert('카카오 로그인을 다시 시도해주세요.');
      }
      throw e;
    }
  }

  public async postPayment(payType: string, scheduleId: number, params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('postPayment: ', tokenDto);
    try {
      const res = await PaymentAgent.postPayment(
        payType,
        scheduleId,
        params,
        tokenDto.accessToken,
      );
      console.log('postPayment: ', res);
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
  public async patchPayment(payType: string, scheduleId: number, params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('patchPayment: ', tokenDto);
    try {
      const res = await PaymentAgent.patchPayment(
        payType,
        scheduleId,
        params,
        tokenDto.accessToken,
      );
      console.log('patchPayment: ', res);
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

  public async deletePayment(payType: string, orderId: number) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('deletePayment: ', tokenDto);
    try {
      const res = await PaymentAgent.deletePayment(
        payType,
        orderId,
        tokenDto.accessToken,
      );
      return res;
    } catch (e) {
      console.error(e);
      if (e.response.data.code === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        Alert.alert('다시 시도해주세요');
      } else if (e.response.data.code === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        Alert.alert('카카오 로그인을 다시 시도해주세요.');
      }
      throw e;
    }
  }
}

export default new PaymentController();
