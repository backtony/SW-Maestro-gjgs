import Cookies from "universal-cookie";
import NoticeAgent from "../agents/NoticeAgent";

const cookies = new Cookies();

class NoticeController {
  public async getNotices(page: number) {
    const accessToken = cookies.get("accessToken");
    try {
      const res = NoticeAgent.getNotices(page, accessToken);
      return res;
    } catch (e) {
      console.error(e);
    }
  }

  public async postNotice(params: any) {
    const accessToken = cookies.get("accessToken");
    try {
      const res = NoticeAgent.postNotice(params, accessToken);
      return res;
    } catch (e) {
      console.error(e);
    }
  }

  public async getAllNotices(page: number) {
    const accessToken = cookies.get("accessToken");
    try {
      const res = NoticeAgent.getAllNotices(page, accessToken);
      return res;
    } catch (e) {
      console.error(e);
    }
  }

  public async putNotice(params: any, noticeId: number) {
    const accessToken = cookies.get("accessToken");
    try {
      const res = NoticeAgent.putNotice(params, noticeId, accessToken);
      return res;
    } catch (e) {
      console.error(e);
    }
  }

  public async deleteNotice(noticeId: number) {
    const accessToken = cookies.get("accessToken");
    try {
      const res = NoticeAgent.deleteNotice(noticeId, accessToken);
      return res;
    } catch (e) {
      console.error(e);
    }
  }

  public async getNoticeDetail(noticeId: number) {
    const accessToken = cookies.get("accessToken");
    try {
      const res = NoticeAgent.getNoticeDetail(noticeId, accessToken);
      return res;
    } catch (e) {
      console.error(e);
    }
  }
}

export default new NoticeController();
