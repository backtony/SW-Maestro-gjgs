import Cookies from "universal-cookie";
import LectureAgent from "../agents/LectureAgent";

const cookies = new Cookies();

class LectureController {
  public async putFirst(formData: FormData) {
    const accessToken = cookies.get("accessToken");
    try {
      const res = LectureAgent.putFirst(formData, accessToken);
      return res;
    } catch (e) {
      console.error(e);
    }
  }

  public async getFirst() {
    const accessToken = cookies.get("accessToken");
    try {
      const res = LectureAgent.getFirst(accessToken);
      return res;
    } catch (e) {
      console.error(e);
      throw e;
    }
  }

  public async putIntro(formData: FormData) {
    const accessToken = cookies.get("accessToken");
    try {
      const res = LectureAgent.putIntro(formData, accessToken);
      return res;
    } catch (e) {
      console.error(e);
    }
  }

  public async getIntro() {
    const accessToken = cookies.get("accessToken");
    try {
      const res = LectureAgent.getIntro(accessToken);
      return res;
    } catch (e) {
      console.error(e);
    }
  }

  public async putCurriculum(formData: FormData) {
    const accessToken = cookies.get("accessToken");
    try {
      const res = LectureAgent.putCurriculum(formData, accessToken);
      return res;
    } catch (e) {
      console.error(e);
    }
  }

  public async getCurriculum() {
    const accessToken = cookies.get("accessToken");
    try {
      const res = LectureAgent.getCurriculum(accessToken);
      return res;
    } catch (e) {
      console.error(e);
    }
  }

  public async putSchedule(formData: FormData) {
    const accessToken = cookies.get("accessToken");
    try {
      const res = LectureAgent.putSchedule(formData, accessToken);
      return res;
    } catch (e) {
      console.error(e);
    }
  }

  public async getSchedule() {
    const accessToken = cookies.get("accessToken");
    try {
      const res = LectureAgent.getSchedule(accessToken);
      return res;
    } catch (e) {
      console.error(e);
    }
  }

  public async putPriceCoupon(formData: FormData) {
    const accessToken = cookies.get("accessToken");
    try {
      const res = LectureAgent.putPriceCoupon(formData, accessToken);
      return res;
    } catch (e) {
      console.error(e);
    }
  }

  public async getPriceCoupon() {
    const accessToken = cookies.get("accessToken");
    try {
      const res = LectureAgent.getPriceCoupon(accessToken);
      return res;
    } catch (e) {
      console.error(e);
    }
  }

  public async postTerms(params: any) {
    const accessToken = cookies.get("accessToken");
    try {
      const res = LectureAgent.postTerms(params, accessToken);
      return res;
    } catch (e) {
      console.error(e);
    }
  }

  public async getMyClass(params: any) {
    const accessToken = cookies.get("accessToken");
    try {
      const res = LectureAgent.getMyClass(params, accessToken);
      return res;
    } catch (e) {
      console.error(e);
    }
  }

  public async getMyClassSchedule(params: any) {
    const accessToken = cookies.get("accessToken");
    try {
      const res = LectureAgent.getMyClassSchedule(params, accessToken);
      return res;
    } catch (e) {
      console.error(e);
    }
  }

  public async getQuestions(params: any) {
    const accessToken = cookies.get("accessToken");
    try {
      const res = LectureAgent.getQuestions(params, accessToken);
      return res;
    } catch (e) {
      console.error(e);
    }
  }

  public async putAnswer(params: any, questionId: number) {
    const accessToken = cookies.get("accessToken");
    try {
      const res = LectureAgent.putAnswer(params, questionId, accessToken);
      return res;
    } catch (e) {
      console.error(e);
    }
  }

  public async getLectureSchedule(lectureId: number) {
    const accessToken = cookies.get("accessToken");
    try {
      const res = LectureAgent.getLectureSchedule(lectureId, accessToken);
      return res;
    } catch (e) {
      console.error(e);
    }
  }

  public async deleteLectureSchedule(lectureId: number, scheduleId: number) {
    const accessToken = cookies.get("accessToken");
    try {
      const res = LectureAgent.deleteLectureSchedule(
        lectureId,
        scheduleId,
        accessToken
      );
      return res;
    } catch (e) {
      console.error(e);
    }
  }

  public async postLectureSchedule(lectureId: number, params: any) {
    const accessToken = cookies.get("accessToken");
    try {
      const res = LectureAgent.postLectureSchedule(
        lectureId,
        params,
        accessToken
      );
      return res;
    } catch (e) {
      console.error(e);
    }
  }

  public async patchRejectLecture(lectureId: number) {
    const accessToken = cookies.get("accessToken");
    try {
      const res = LectureAgent.patchRejectLecture(lectureId, accessToken);
      return res;
    } catch (e) {
      console.error(e);
    }
  }

  public async deleteWritenLecture() {
    const accessToken = cookies.get("accessToken");
    try {
      const res = LectureAgent.deleteWritenLecture(accessToken);
      return res;
    } catch (e) {
      console.error(e);
    }
  }
}

export default new LectureController();
