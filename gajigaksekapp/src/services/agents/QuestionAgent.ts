import axios from 'axios';
import {API_URL} from '../../utils/commonParam';

class QuestionAgent {
  public async postQuestion(params: any, accessToken: String) {
    try {
      const res = await axios.post(`${API_URL}/questions/`, params, {
        headers: {Authorization: `Bearer ${accessToken}`},
      });
      return res;
    } catch (e) {
      console.error(e);
      alert('다시 시도해주세요.');
      throw e.response.data.code;
    }
  }

  public async putQuestionEdit(params: any, accessToken: String) {
    try {
      const res = await axios.put(
        `${API_URL}/mypage/question/${params.questionId}`,
        {mainText: params.mainText},
        {
          headers: {Authorization: `Bearer ${accessToken}`},
        },
      );
      return res;
    } catch (e) {
      console.error(e);
      alert('다시 시도해주세요.');
      throw e.response.data.code;
    }
  }

  public async deleteMypageQuestion(params: any, accessToken: String) {
    try {
      const res = await axios.delete(
        `${API_URL}/mypage/question/${params.questionId}`,
        {
          headers: {Authorization: `Bearer ${accessToken}`},
        },
      );
      return res;
    } catch (e) {
      console.error(e);
      alert('다시 시도해주세요.');
      throw e.response.data.code;
    }
  }

  public async getQuestionDash(params: any, accessToken: String) {
    try {
      const res = await axios.get(
        `${API_URL}/questions/${params.questionId}`,
        accessToken
          ? {
              headers: {Authorization: `Bearer ${accessToken}`},
            }
          : {},
      );
      return res;
    } catch (e) {
      console.error(e);
      alert('다시 시도해주세요.');
      throw e.response.data.code;
    }
  }
}

export default new QuestionAgent();
