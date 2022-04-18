import axios from 'axios';
import {API_URL} from '../../utils/commonParam';

class GroupAgent {
  public async createTeam(params: any, accessToken: String) {
    try {
      const res = await axios.post(
        `${API_URL}/teams/`,
        {
          teamName: params.teamName,
          maxPeople: params.maxPeople,
          zoneId: params.zoneId,
          categoryList: params.categoryList,
          dayType: params.dayType,
          timeType: params.timeType,
        },
        {headers: {Authorization: `Bearer ${accessToken}`}},
      );
      return res;
    } catch (e) {
      console.error(e);
      alert('그룹생성을 다시 시도해주세요.');
      throw e.response.data.code;
    }
  }

  public async getTeamDash(params: any, accessToken: String) {
    try {
      const res = await axios.get(`${API_URL}/teams/${params.teamId}`, {
        headers: {Authorization: `Bearer ${accessToken}`},
      });
      return res;
    } catch (e) {
      console.error(e);
      alert('그룹 상세페이지를 다시 로드해주세요.');
      throw e.response.data.code;
    }
  }

  public async getTeamList(params: any, accessToken: String) {
    try {
      const res = await axios.get(`${API_URL}/teams/`, {
        headers: {Authorization: `Bearer ${accessToken}`},
      });
      return res;
    } catch (e) {
      console.error(e);
      throw e.response.data.code;
    }
  }

  public async getPossibleTeamList(params: any, accessToken: String) {
    try {
      const res = await axios.get(`${API_URL}/teams/lead`, {
        headers: {Authorization: `Bearer ${accessToken}`},
      });
      return res;
    } catch (e) {
      console.error(e);
      throw e.response.data.code;
    }
  }

  public async patchTeam(params: any, accessToken: String, teamId: number) {
    try {
      const res = await axios.patch(`${API_URL}/teams/${teamId}`, params, {
        headers: {Authorization: `Bearer ${accessToken}`},
      });
      return res;
    } catch (e) {
      console.error(e);
      alert('그룹 수정을 다시 시도해주세요.');
      throw e.response.data.code;
    }
  }

  public async patchLeader(params: any, accessToken: String) {
    try {
      const res = await axios.patch(
        `${API_URL}/teams/${params.teamId}/members/${params.memberId}`,
        null,
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

  public async deleteTeamWithLeader(params: any, accessToken: String) {
    try {
      const res = await axios.delete(`${API_URL}/teams/${params.teamId}`, {
        headers: {Authorization: `Bearer ${accessToken}`},
      });
      return res;
    } catch (e) {
      console.error(e);
      alert('그룹 나가기를 다시 시도해주세요.');
      throw e.response.data.code;
    }
  }

  public async deleteTeamWithMember(params: any, accessToken: String) {
    try {
      const res = await axios.delete(
        `${API_URL}/teams/${params.teamId}/members`,
        {
          headers: {Authorization: `Bearer ${accessToken}`},
        },
      );
      return res;
    } catch (e) {
      console.error(e);
      alert('그룹 나가기를 다시 시도해주세요.');
      throw e.response.data.code;
    }
  }
}

export default new GroupAgent();
