import axios from 'axios';
import {Alert} from 'react-native';
import {API_URL} from '../../utils/commonParam';

class CouponAgent {
  public async getCoupon(lectureId: any, accessToken: String) {
    try {
      const res = await axios.get(`${API_URL}/lectures/${lectureId}/coupon`, {
        headers: {Authorization: `Bearer ${accessToken}`},
      });
      return res;
    } catch (e) {
      console.error(e);
      throw e.response.data.code;
    }
  }

  public async getMyCoupon(accessToken: String) {
    try {
      const res = await axios.get(`${API_URL}/mypage/coupons`, {
        headers: {Authorization: `Bearer ${accessToken}`},
      });
      return res;
    } catch (e) {
      console.error(e);
      throw e.response.data.code;
    }
  }

  public async postCoupon(lectureId: number, accessToken: String) {
    try {
      const res = await axios.post(
        `${API_URL}/lectures/${lectureId}/coupon`,
        null,
        {
          headers: {Authorization: `Bearer ${accessToken}`},
        },
      );
      return res;
    } catch (e) {
      console.error(e);

      if (e.response.data.message !== '회원이 이미 쿠폰을 갖고 있습니다.') {
        Alert.alert('쿠폰이 모두 소진되었습니다.');
      } else {
        Alert.alert(e.response.data.message);
      }

      throw e.response.data.code;
    }
  }
}

export default new CouponAgent();
