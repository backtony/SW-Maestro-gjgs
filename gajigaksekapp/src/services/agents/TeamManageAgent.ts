import axios from 'axios';
import {API_URL} from '../../utils/commonParam';

class TeamManageAgent {
  public async getAppliers(params: any, accessToken: String) {
    try {
      const res = await axios.get(
        `${API_URL}/teams/${params.teamId}/appliers`,
        {
          headers: {Authorization: `Bearer ${accessToken}`},
        },
      );
      return res;
    } catch (e) {
      console.error(e);
      //   alert('그룹 상세페이지를 다시 로드해주세요.');
      throw e.response.data.code;
    }
  }

  public async postApplierConfirm(params: any, accessToken: String) {
    try {
      const res = await axios.post(
        `${API_URL}/teams/${params.teamId}/appliers/${params.applierId}`,
        {},
        {
          headers: {Authorization: `Bearer ${accessToken}`},
        },
      );
      return res;
    } catch (e) {
      console.error(e);
      //   alert('그룹 상세페이지를 다시 로드해주세요.');
      throw e.response.data.code;
    }
  }

  public async postTeamApply(params: any, accessToken: String) {
    try {
      const res = await axios.post(
        `${API_URL}/teams/${params.teamId}/appliers`,
        {},
        {
          headers: {Authorization: `Bearer ${accessToken}`},
        },
      );
      return res;
    } catch (e) {
      console.error(e);
      //   alert('그룹 상세페이지를 다시 로드해주세요.');
      throw e.response.data.code;
    }
  }

  public async deleteApplier(params: any, accessToken: String) {
    try {
      const res = await axios.delete(
        `${API_URL}/teams/${params.teamId}/appliers/${params.applierId}`,
        {
          headers: {Authorization: `Bearer ${accessToken}`},
        },
      );
      return res;
    } catch (e) {
      console.error(e);
      //   alert('그룹 상세페이지를 다시 로드해주세요.');
      throw e.response.data.code;
    }
  }

  public async deleteMember(params: any, accessToken: String) {
    try {
      const res = await axios.delete(
        `${API_URL}/teams/${params.teamId}/members/${params.memberId}`,
        {
          headers: {Authorization: `Bearer ${accessToken}`},
        },
      );
      return res;
    } catch (e) {
      console.error(e);
      //   alert('그룹 상세페이지를 다시 로드해주세요.');
      throw e.response.data.code;
    }
  }
}

export default new TeamManageAgent();
