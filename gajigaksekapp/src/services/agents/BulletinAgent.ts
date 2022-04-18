import axios from 'axios';
import {Alert} from 'react-native';
import {API_URL} from '../../utils/commonParam';
// import jQuery from 'jquery';

class BulletinAgent {
  public async postCreateBulletin(params: any, accessToken: String) {
    try {
      const res = await axios.post(`${API_URL}/bulletins/`, params, {
        headers: {Authorization: `Bearer ${accessToken}`},
      });
      return res;
    } catch (e) {
      console.error(e);
      Alert.alert('게시글 생성을 다시 시도해주세요.');
      throw e.response.data.code;
    }
  }

  public async getSearchBulletin(params: any, accessToken: String) {
    try {
      const queryString = Object.keys(params)
        .map(key => {
          if (key === 'categoryIdList') {
            return (
              encodeURIComponent(key) +
              '=' +
              params[key].map(item => encodeURIComponent(item)).join(',')
            );
          }

          return (
            encodeURIComponent(key) + '=' + encodeURIComponent(params[key])
          );
        })
        .join('&');
      const res = await axios.get(
        `${API_URL}/bulletins?${queryString}`,
        accessToken
          ? {
              headers: {Authorization: `Bearer ${accessToken}`},
            }
          : {},
      );
      return res;
    } catch (e) {
      console.error(e);
      throw e.response.data.code;
    }
  }

  public async getLectureBulletin(
    params: any,
    lectureId: string,
    accessToken: String,
  ) {
    try {
      const queryString = Object.keys(params)
        .map(key => {
          return (
            encodeURIComponent(key) + '=' + encodeURIComponent(params[key])
          );
        })
        .join('&');
      console.log('queryString: ', queryString);
      console.log('accessToken: ', accessToken);
      const res = await axios.get(
        `${API_URL}/lectures/${lectureId}/bulletins?${queryString}`,
        accessToken
          ? {
              headers: {Authorization: `Bearer ${accessToken}`},
            }
          : {},
      );
      return res;
    } catch (e) {
      console.error(e);
      Alert.alert('게시글 생성을 다시 시도해주세요.');
      throw e.response.data.code;
    }
  }

  public async getBulletinDash(params: any, accessToken: String) {
    try {
      const header = accessToken
        ? {
            headers: {Authorization: `Bearer ${accessToken}`},
          }
        : {};
      const res = await axios.get(
        `${API_URL}/bulletins/${params.bulletinId}`,
        header,
      );
      return res;
    } catch (e) {
      console.error(e);
      Alert.alert('게시글을 다시 로드해주세요.');
      throw e.response.data.code;
    }
  }

  public async deleteBulletin(params: any, accessToken: String) {
    try {
      const res = await axios.delete(
        `${API_URL}/bulletins/${params.bulletinId}`,
        {
          headers: {Authorization: `Bearer ${accessToken}`},
        },
      );
      return res;
    } catch (e) {
      console.error(e);
      Alert.alert('게시글 삭제를 다시 시도해주세요.');
      throw e.response.data.code;
    }
  }

  public async patchBulletinEdit(
    params: any,
    bulletinId: number,
    accessToken: String,
  ) {
    try {
      const res = await axios.patch(
        `${API_URL}/bulletins/${bulletinId}`,
        params,
        {
          headers: {Authorization: `Bearer ${accessToken}`},
        },
      );
      return res;
    } catch (e) {
      console.error(e);
      Alert.alert('게시글 수정을 다시 시도해주세요.');
      throw e.response.data.code;
    }
  }

  public async patchBulletinStatus(params: any, accessToken: String) {
    try {
      const res = await axios.patch(
        `${API_URL}/bulletins/${params.bulletinId}/recruit`,
        params,
        {
          headers: {Authorization: `Bearer ${accessToken}`},
        },
      );
      return res;
    } catch (e) {
      console.error(e);
      Alert.alert('다시 시도해주세요.');
      throw e.response.data.code;
    }
  }
}

export default new BulletinAgent();
