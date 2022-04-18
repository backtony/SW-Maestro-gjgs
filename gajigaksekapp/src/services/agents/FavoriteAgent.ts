import axios from 'axios';
import {Alert} from 'react-native';
import {API_URL} from '../../utils/commonParam';
// import jQuery from 'jquery';

class FavoriteAgent {
  public async postFavoriteIndividual(params: any, accessToken: String) {
    try {
      const res = await axios.post(
        `${API_URL}/favorites/lectures/${params.lectureId}`,
        {},
        {
          headers: {Authorization: `Bearer ${accessToken}`},
        },
      );
      return res;
    } catch (e) {
      console.error(e);
      Alert.alert('찜하기를 다시 시도해주세요.');
      throw e.response.data.code;
    }
  }

  public async deleteFavoriteIndividual(params: any, accessToken: String) {
    try {
      const res = await axios.delete(
        `${API_URL}/favorites/lectures/${params.lectureId}`,
        {
          headers: {Authorization: `Bearer ${accessToken}`},
        },
      );
      return res;
    } catch (e) {
      console.error(e);
      Alert.alert('찜하기를 다시 시도해주세요.');
      throw e.response.data.code;
    }
  }

  public async postFavoriteTeam(params: any, accessToken: String) {
    try {
      const res = await axios.post(
        `${API_URL}/favorites/teams/${params.teamId}/${params.lectureId}`,
        {},
        {
          headers: {Authorization: `Bearer ${accessToken}`},
        },
      );
      return res;
    } catch (e) {
      console.error(e);
      Alert.alert('찜하기를 다시 시도해주세요.');
      throw e.response.data.code;
    }
  }

  public async postFavoriteBulletin(params: any, accessToken: String) {
    try {
      const res = await axios.post(
        `${API_URL}/favorites/bulletins/${params.bulletinId}`,
        {},
        {
          headers: {Authorization: `Bearer ${accessToken}`},
        },
      );
      return res;
    } catch (e) {
      console.error(e);
      Alert.alert('찜하기를 다시 시도해주세요.');
      throw e.response.data.code;
    }
  }

  public async deleteFavoriteTeam(params: any, accessToken: String) {
    try {
      const res = await axios.delete(
        `${API_URL}/favorites/teams/${params.teamId}/${params.lectureId}`,
        {
          headers: {Authorization: `Bearer ${accessToken}`},
        },
      );
      return res;
    } catch (e) {
      console.error(e);
      Alert.alert('찜하기를 다시 시도해주세요.');
      throw e.response.data.code;
    }
  }

  public async deleteFavoriteBulletin(params: any, accessToken: String) {
    try {
      const res = await axios.delete(
        `${API_URL}/favorites/bulletins/${params.bulletinId}`,
        {
          headers: {Authorization: `Bearer ${accessToken}`},
        },
      );
      return res;
    } catch (e) {
      console.error(e);
      Alert.alert('찜하기를 다시 시도해주세요.');
      throw e.response.data.code;
    }
  }

  public async deleteTeamLecture(params: any, accessToken: String) {
    try {
      const res = await axios.delete(
        `${API_URL}/favorites/teams/${params.teamId}/${params.lectureId}`,
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

  public async getTeamList(params: any, accessToken: String) {
    try {
      const res = await axios.get(
        `${API_URL}/favorites/my-teams/info/${params.lectureId}`,
        {
          headers: {Authorization: `Bearer ${accessToken}`},
        },
      );
      return res;
    } catch (e) {
      console.error(e);
      Alert.alert('찜하기를 다시 시도해주세요.');
      throw e.response.data.code;
    }
  }

  public async getLectureIndividual(params: any, accessToken: String) {
    try {
      const res = await axios.get(`${API_URL}/favorites/lectures`, {
        headers: {Authorization: `Bearer ${accessToken}`},
      });
      return res;
    } catch (e) {
      console.error(e);
      Alert.alert('다시 시도해주세요.');
      throw e.response.data.code;
    }
  }

  public async getBulletin(params: any, accessToken: String) {
    try {
      const res = await axios.get(`${API_URL}/favorites/bulletins`, {
        headers: {Authorization: `Bearer ${accessToken}`},
      });
      return res;
    } catch (e) {
      console.error(e);
      Alert.alert('다시 시도해주세요.');
      throw e.response.data.code;
    }
  }

  public async getTeamLecture(params: any, accessToken: String) {
    try {
      const res = await axios.get(
        `${API_URL}/favorites/teams/${params.teamId}`,
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

export default new FavoriteAgent();
