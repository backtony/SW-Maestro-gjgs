import axios from "axios";

class AdminLectureAgent {
  public async getLectures(accessToken: string) {
    try {
      const res = await axios.get(`/api/v1/admin/lectures`, {
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

  public async getDetail(lectureId: number, accessToken: string) {
    try {
      const res = await axios.get(`/api/v1/admin/lectures/${lectureId}`, {
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

  public async postLecture(
    lectureId: number,
    decideType: string,
    rejectReason: string,
    accessToken: string
  ) {
    try {
      const res = await axios.post(
        `/api/v1/admin/lectures/${lectureId}/${decideType}`,
        { rejectReason: rejectReason },
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );
      return res;
    } catch (e) {
      console.error(e);
      throw e.response.data.code;
    }
  }
}

export default new AdminLectureAgent();
