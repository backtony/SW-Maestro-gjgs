import axios from 'axios';
import {API_URL} from '../../utils/commonParam';

class NotificationAgent {
  public async postRead(uuid: any, accessToken: String) {
    try {
      const res = await axios.post(`${API_URL}/notification/${uuid}`, null, {
        headers: {Authorization: `Bearer ${accessToken}`},
      });
      return res;
    } catch (e) {
      console.error(e);
      throw e.response.data.code;
    }
  }

  public async getMyNotification(accessToken: String) {
    try {
      const res = await axios.get(`${API_URL}/mypage/notifications`, {
        headers: {Authorization: `Bearer ${accessToken}`},
      });
      return res;
    } catch (e) {
      console.error(e);
      throw e.response.data.code;
    }
  }
}

export default new NotificationAgent();
