import axios from 'axios';
import {API_URL} from '../../utils/commonParam';
// import jQuery from 'jquery';

class LectureAgent {
  public async getLectureList(params: any, accessToken: String) {
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

          if (key === 'keyword' && !params[key]) {
            return '';
          }

          return (
            encodeURIComponent(key) + '=' + encodeURIComponent(params[key])
          );
        })
        .join('&');
      console.log('queryString: ', queryString);
      const res = await axios.get(
        `${API_URL}/lectures?${queryString}`,
        accessToken
          ? {
              headers: {Authorization: `Bearer ${accessToken}`},
            }
          : {},
      );
      return res;
    } catch (e) {
      console.error(e);
      alert('클래스 검색을 다시 시도해주세요.');
      throw e.response.data.code;
    }
  }

  public async getRecommendLectureList(
    categoryIdList: number[],
    accessToken: String,
  ) {
    try {
      const res = await axios.get(
        `${API_URL}/lectures?categoryIdList=${categoryIdList.toString()}&reviewCount,desc&score,desc&clickCount,desc&createdDate,desc&reviewCount%2Cdesc=&score%2Cdesc=&clickCount%2Cdesc=&createdDate%2Cdesc=`,
        accessToken
          ? {
              headers: {Authorization: `Bearer ${accessToken}`},
            }
          : {},
      );
      return res;
    } catch (e) {
      console.error(e);
      alert('클래스 검색을 다시 시도해주세요.');
      throw e.response.data.code;
    }
  }

  public async getLectureDash(params: any, accessToken: String) {
    try {
      const res = await axios.get(
        `${API_URL}/lectures/${params.lectureId}`,
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

  public async getQuestion(params: any, accessToken: String) {
    try {
      const res = await axios.get(
        `${API_URL}/lectures/${params.lectureId}/questions`,
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

  public async getDirectorLectures(directorId: number, accessToken: String) {
    try {
      const res = await axios.get(
        `${API_URL}/lectures/directors/${directorId}`,
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
    }
  }

  public async getLectureReviews(lectureId: number) {
    try {
      const res = await axios.get(`${API_URL}/lectures/${lectureId}/reviews`);
      return res;
    } catch (e) {
      console.error(e);
      alert('다시 시도해주세요.');
    }
  }
}

export default new LectureAgent();
