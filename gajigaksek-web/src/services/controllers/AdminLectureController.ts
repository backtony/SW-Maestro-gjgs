import Cookies from "universal-cookie";
import AdminLectureAgent from "../agents/AdminLectureAgent";

const cookies = new Cookies();

class AdminLectureController {
  public async getLectures() {
    const accessToken = cookies.get("accessToken");
    try {
      const res = AdminLectureAgent.getLectures(accessToken);
      return res;
    } catch (e) {
      console.error(e);
      throw e;
    }
  }

  public async getDetail(lectureId: number) {
    const accessToken = cookies.get("accessToken");
    try {
      const res = AdminLectureAgent.getDetail(lectureId, accessToken);
      return res;
    } catch (e) {
      console.error(e);
      throw e;
    }
  }

  public async postLecture(
    lectureId: number,
    decideType: string,
    rejectReason: string
  ) {
    const accessToken = cookies.get("accessToken");
    try {
      const res = AdminLectureAgent.postLecture(
        lectureId,
        decideType,
        rejectReason,
        accessToken
      );
      return res;
    } catch (e) {
      console.error(e);
      throw e;
    }
  }
}

export default new AdminLectureController();
