import {API_URL} from '@/utils/commonParam';
import axios from 'axios';

class ApplyAgent {
  public async postApply(params: any, accessToken: String) {
    try {
      const res = await axios.post(
        `${API_URL}/schedules/${params.scheduleId}`,
        {lectureId: params.lectureId, teamId: params.teamId},
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

  public async deleteSchedule(
    scheduleId: number,
    teamId: number,
    accessToken: String,
  ) {
    try {
      const res = await axios.delete(
        `${API_URL}/schedules/${scheduleId}/teams/${teamId}`,
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
}

export default new ApplyAgent();
