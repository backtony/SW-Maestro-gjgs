import axios from 'axios';
import {Alert} from 'react-native';
import {API_URL} from '../../utils/commonParam';

class PaymentAgent {
  public async getPaymentInfo(
    payType: string,
    scheduleId: number,
    accessToken: String,
  ) {
    try {
      const res = await axios.get(
        `${API_URL}/payment/${payType}/${scheduleId}`,
        {
          headers: {Authorization: `Bearer ${accessToken}`},
        },
      );
      return res;
    } catch (e) {
      console.error(e);
      throw e;
    }
  }

  public async postPayment(
    payType: string,
    scheduleId: number,
    params: any,
    accessToken: String,
  ) {
    try {
      const res = await axios.post(
        `${API_URL}/payment/${payType}/${scheduleId}`,
        params,
        {
          headers: {Authorization: `Bearer ${accessToken}`},
        },
      );
      return res;
    } catch (e) {
      console.error(e);
      throw e.response.data.code;
    }
  }

  public async patchPayment(
    payType: string,
    scheduleId: number,
    params: any,
    accessToken: String,
  ) {
    try {
      const res = await axios.patch(
        `${API_URL}/payment/${payType}/${scheduleId}`,
        params,
        {
          headers: {Authorization: `Bearer ${accessToken}`},
        },
      );
      return res;
    } catch (e) {
      console.error(e);
      Alert.alert(e.response.data.message);
      throw e.response.data.code;
    }
  }

  public async deletePayment(
    payType: string,
    orderId: number,
    accessToken: String,
  ) {
    try {
      const res = await axios.delete(
        `${API_URL}/payment/${payType}/${orderId}`,
        {
          headers: {Authorization: `Bearer ${accessToken}`},
        },
      );
      return res;
    } catch (e) {
      console.error(e);
      throw e;
    }
  }
}

export default new PaymentAgent();
