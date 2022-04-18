import axios from "axios";

class LectureAgent {
  public async putFirst(formData: FormData, accessToken: string) {
    try {
      const res = await axios.put(`/api/v2/director/lectures`, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
          Authorization: `Bearer ${accessToken}`,
        },
      });
      return res;
    } catch (e) {
      console.error(e);
      throw e.response.data.code;
    }
  }

  public async getFirst(accessToken: string) {
    try {
      const res = await axios.get(`/api/v2/director/lectures`, {
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

  public async putIntro(formData: FormData, accessToken: string) {
    try {
      const res = await axios.put(`/api/v2/director/lectures`, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
          Authorization: `Bearer ${accessToken}`,
        },
      });
      return res;
    } catch (e) {
      console.error(e);
      throw e.response.data.code;
    }
  }

  public async getIntro(accessToken: string) {
    try {
      const res = await axios.get("/api/v2/director/lectures", {
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

  public async putCurriculum(formData: FormData, accessToken: string) {
    try {
      const res = await axios.put("/api/v2/director/lectures", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
          Authorization: `Bearer ${accessToken}`,
        },
      });
      return res;
    } catch (e) {
      console.error(e);
      throw e.response.data.code;
    }
  }

  public async getCurriculum(accessToken: string) {
    try {
      const res = await axios.get(`/api/v2/director/lectures`, {
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

  public async putSchedule(formData: FormData, accessToken: string) {
    try {
      const res = await axios.put("/api/v2/director/lectures", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
          Authorization: `Bearer ${accessToken}`,
        },
      });
      return res;
    } catch (e) {
      console.error(e);
      throw e.response.data.code;
    }
  }

  public async getSchedule(accessToken: string) {
    try {
      const res = await axios.get("/api/v2/director/lectures", {
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

  public async putPriceCoupon(formData: FormData, accessToken: string) {
    try {
      const res = await axios.put("/api/v2/director/lectures", formData, {
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

  public async getPriceCoupon(accessToken: string) {
    try {
      const res = await axios.get("/api/v2/director/lectures", {
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

  public async postTerms(params: any, accessToken: string) {
    try {
      const res = await axios.post("/api/v2/director/lectures", params, {
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

  public async getMyClass(params: any, accessToken: string) {
    try {
      const res = await axios.get(
        `/api/v1/mypage/directors/lectures?condition=${params.condition}`,
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

  public async getMyClassSchedule(params: any, accessToken: string) {
    try {
      const res = await axios.get(
        `/api/v1/mypage/directors/schedules?${
          params.endDate ? `endDate=${params.endDate}` : ""
        }${
          params.startDate
            ? `${params.endDate ? "&" : ""}startDate=${params.startDate}`
            : ""
        }${
          params.keyword
            ? `${params.endDate || params.startDate ? "&" : ""}keyword=${
                params.keyword
              }`
            : ""
        }${
          params.endDate || params.startDate || params.keyword ? "&" : ""
        }searchType=${params.searchType}`,
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

  public async getQuestions(params: any, accessToken: string) {
    try {
      const res = await axios.get(
        `/api/v1/mypage/directors/questions?questionStatus=${
          params.questionStatus
        }${params.lectureId === 0 ? "" : `&lectureId=${params.lectureId}`}`,
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

  public async putAnswer(params: any, questionId: number, accessToken: string) {
    try {
      const res = await axios.put(`/api/v1/questions/${questionId}`, params, {
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

  public async getLectureSchedule(lectureId: any, accessToken: string) {
    try {
      const res = await axios.get(
        `/api/v1/mypage/directors/lectures/${lectureId}/schedules`,
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

  public async deleteLectureSchedule(
    lectureId: number,
    scheduleId: number,
    accessToken: string
  ) {
    try {
      const res = await axios.delete(
        `/api/v1/mypage/directors/lectures/${lectureId}/schedules/${scheduleId}`,
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

  public async postLectureSchedule(
    lectureId: number,
    params: any,
    accessToken: string
  ) {
    try {
      const res = await axios.post(
        `/api/v1/mypage/directors/lectures/${lectureId}/schedules`,
        params,
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

  public async patchRejectLecture(lectureId: number, accessToken: string) {
    try {
      const res = await axios.patch(
        `/api/v1/mypage/directors/lectures/${lectureId}`,
        null,
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

  public async deleteWritenLecture(accessToken: string) {
    try {
      const res = await axios.delete(`/api/v2/director/lectures`, {
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

export default new LectureAgent();
