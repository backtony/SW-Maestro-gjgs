import axios from 'axios';
import {API_URL} from '../../utils/commonParam';

class MatchingAgent {
  public async postMatching(params: any, accessToken: String) {
    try {
      const res = await axios.post(`${API_URL}/matching/`, params, {
        headers: {Authorization: `Bearer ${accessToken}`},
      });
      return res;
    } catch (e) {
      console.error(e);
      throw e.response.data.code;
    }
  }

  public async getMatchingStatus(accessToken: String) {
    try {
      const res = await axios.get(`${API_URL}/matching`, {
        headers: {Authorization: `Bearer ${accessToken}`},
      });
      return res;
    } catch (e) {
      console.error(e);
      throw e.response.data.code;
    }
  }

  public async deleteMatching(accessToken: String) {
    try {
      const res = await axios.delete(`${API_URL}/matching`, {
        headers: {Authorization: `Bearer ${accessToken}`},
      });
      return res;
    } catch (e) {
      console.error(e);
      throw e.response.data.code;
    }
  }
}

export default new MatchingAgent();
