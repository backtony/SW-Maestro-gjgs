import axios from 'axios';
import {Platform} from 'react-native';
import {API_URL} from '../../utils/commonParam';
import {FileForm} from '../../screens/mypage/editMyProfile';

class MypageAgent {
  public async getMypage(params: any, accessToken: String) {
    try {
      const res = await axios.get(`${API_URL}/mypage`, {
        headers: {Authorization: `Bearer ${accessToken}`},
      });
      return res;
    } catch (e) {
      console.error(e);
      throw e.response.data.code;
    }
  }

  public async getBulletin(params: any, accessToken: String) {
    try {
      const res = await axios.get(`${API_URL}/mypage/bulletins`, {
        headers: {Authorization: `Bearer ${accessToken}`},
      });
      return res;
    } catch (e) {
      console.error(e);
      throw e.response.data.code;
    }
  }

  public async getProfileEdit(params: any, accessToken: String) {
    try {
      const res = await axios.get(`${API_URL}/mypage/edit`, {
        headers: {Authorization: `Bearer ${accessToken}`},
      });
      return res;
    } catch (e) {
      console.error(e);
      throw e.response.data.code;
    }
  }

  public async getProfile(params: any) {
    try {
      const res = await axios.get(`${API_URL}/mypage/info/${params.memberId}`);
      return res;
    } catch (e) {
      console.error(e);
      throw e.response.data.code;
    }
  }

  public async putNicknameEdit(params: any, accessToken: String) {
    try {
      const res = await axios.put(`${API_URL}/mypage/nickname`, params, {
        headers: {Authorization: `Bearer ${accessToken}`},
      });
      return res;
    } catch (e) {
      console.error(e);
      alert('닉네임 변경에 실패했습니다.');
      throw e.response.data.code;
    }
  }

  public async putPhoneEdit(params: any, accessToken: String) {
    try {
      const res = await axios.put(`${API_URL}/mypage/phone`, params, {
        headers: {Authorization: `Bearer ${accessToken}`},
      });
      return res;
    } catch (e) {
      console.error(e);
      alert('핸드폰 번호 변경에 실패했습니다.');
      throw e.response.data.code;
    }
  }

  public async putCategory(params: any, accessToken: String) {
    try {
      const res = await axios.put(`${API_URL}/mypage/category`, params, {
        headers: {Authorization: `Bearer ${accessToken}`},
      });
      return res;
    } catch (e) {
      console.error(e);
      alert('카테고리 변경에 실패했습니다.');
      throw e.response.data.code;
    }
  }

  public async putTextEdit(params: any, accessToken: String) {
    try {
      const res = await axios.put(`${API_URL}/mypage/profile-text`, params, {
        headers: {Authorization: `Bearer ${accessToken}`},
      });
      return res;
    } catch (e) {
      console.error(e);
      alert('소개 변경에 실패했습니다.');
      throw e.response.data.code;
    }
  }

  public async postImage(file: FileForm, accessToken: String) {
    try {
      const data1 = new FormData();
      console.log('file: ', file);
      data1.append('file', file);

      const res = await axios.post(`${API_URL}/mypage/image`, data1, {
        headers: {
          Accept: 'application/json',
          'Content-Type': 'multipart/form-data',
          Authorization: `Bearer ${accessToken}`,
        },
      });

      return res;
    } catch (e) {
      console.error(e);
      alert('이미지 변경에 실패했습니다.');
      throw e.response.data.code;
    }
  }

  public async putZone(params: any, accessToken: String) {
    try {
      const res = await axios.put(`${API_URL}/mypage/zone`, params, {
        headers: {Authorization: `Bearer ${accessToken}`},
      });
      return res;
    } catch (e) {
      console.error(e);
      alert('지역 변경에 실패했습니다.');
      throw e.response.data.code;
    }
  }

  public async getQuestion(params: any, accessToken: String) {
    try {
      const res = await axios.get(`${API_URL}/mypage/question`, {
        headers: {Authorization: `Bearer ${accessToken}`},
      });
      return res;
    } catch (e) {
      console.error(e);
      alert('다시 시도해주세요.');
      throw e.response.data.code;
    }
  }

  public async postSwitchDirector(accessToken: String) {
    try {
      const res = await axios.post(`${API_URL}/mypage/switch-director`, null, {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      });

      return res;
    } catch (e) {
      console.error(e);
      alert('다시 시도해주세요.');
      throw e.response.data.code;
    }
  }

  public async getReward(type: string, accessToken: String) {
    try {
      const res = await axios.get(`${API_URL}/mypage/reward/${type}`, {
        headers: {Authorization: `Bearer ${accessToken}`},
      });
      return res;
    } catch (e) {
      console.error(e);
      alert('다시 시도해주세요.');
      throw e.response.data.code;
    }
  }

  public async getLectures(accessToken: String) {
    try {
      const res = await axios.get(`${API_URL}/mypage/lectures`, {
        headers: {Authorization: `Bearer ${accessToken}`},
      });
      return res;
    } catch (e) {
      console.error(e);
      throw e.response.data.code;
    }
  }

  public async getTeamPaymentStatus(
    scheduleId: number,
    teamId: number,
    accessToken: String,
  ) {
    try {
      const res = await axios.get(
        `${API_URL}/mypage/lectures/payment/${scheduleId}/teams/${teamId}`,
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

  public async getOrder(orderId: number, accessToken: String) {
    try {
      const res = await axios.get(`${API_URL}/mypage/payment/${orderId}`, {
        headers: {Authorization: `Bearer ${accessToken}`},
      });
      return res;
    } catch (e) {
      console.error(e);
      throw e.response.data.code;
    }
  }

  public async postReview(multiPart: FormData, accessToken: String) {
    try {
      const res = await axios.post(`${API_URL}/reviews`, multiPart, {
        headers: {
          Accept: 'application/json',
          'Content-Type': 'multipart/form-data',
          Authorization: `Bearer ${accessToken}`,
        },
      });

      return res;
    } catch (e) {
      console.error(e);
      throw e.response.data.code;
    }
  }

  public async getMyReview(accessToken: String) {
    try {
      const res = await axios.get(`${API_URL}/mypage/reviews`, {
        headers: {Authorization: `Bearer ${accessToken}`},
      });
      return res;
    } catch (e) {
      console.error(e);
      throw e.response.data.code;
    }
  }

  public async getDirectorReviews(directorId: number) {
    try {
      const res = await axios.get(
        `${API_URL}/reviews/directors/${directorId}`,
        {},
      );
      return res;
    } catch (e) {
      console.error(e);
      throw e.response.data.code;
    }
  }

  public async getNotices(accessToken: String) {
    try {
      const res = await axios.get(`${API_URL}/notices?type=ALL`, {
        headers: {Authorization: `Bearer ${accessToken}`},
      });
      return res;
    } catch (e) {
      console.error(e);
      throw e.response.data.code;
    }
  }

  public async getNoticeDetail(noticeId: number, accessToken: String) {
    try {
      const res = await axios.get(`${API_URL}/notices/${noticeId}`, {
        headers: {Authorization: `Bearer ${accessToken}`},
      });
      return res;
    } catch (e) {
      console.error(e);
      throw e.response.data.code;
    }
  }

  public async getAlarmStatus(accessToken: String) {
    try {
      const res = await axios.get(`${API_URL}/mypage/alarm`, {
        headers: {Authorization: `Bearer ${accessToken}`},
      });
      return res;
    } catch (e) {
      console.error(e);
      throw e.response.data.code;
    }
  }

  public async postAlarmStatus(params: any, accessToken: String) {
    try {
      const res = await axios.post(`${API_URL}/mypage/alarm`, params, {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      });

      return res;
    } catch (e) {
      console.error(e);
      throw e.response.data.code;
    }
  }
}

export default new MypageAgent();
