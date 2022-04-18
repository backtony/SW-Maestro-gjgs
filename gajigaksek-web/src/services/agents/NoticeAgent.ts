import axios from "axios";

class NoticeAgent {
  public async getNotices(page: number, accessToken: string) {
    try {
      const res = await axios.get(
        `/api/v1/notices?type=DIRECTOR&page=${page}`,
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

  public async postNotice(params: any, accessToken: string) {
    try {
      const res = await axios.post(`/api/v1/notices`, params, {
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

  public async getAllNotices(page: number, accessToken: string) {
    try {
      const res = await axios.get(`/api/v1/notices?type=ALL&page=${page}`, {
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

  public async putNotice(params: any, noticeId: number, accessToken: string) {
    try {
      const res = await axios.put(`/api/v1/notices/${noticeId}`, params, {
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

  public async deleteNotice(noticeId: number, accessToken: string) {
    try {
      const res = await axios.delete(`/api/v1/notices/${noticeId}`, {
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

  public async getNoticeDetail(noticeId: number, accessToken: string) {
    try {
      const res = await axios.get(`/api/v1/notices/${noticeId}`, {
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

export default new NoticeAgent();
