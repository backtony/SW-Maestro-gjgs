import axios from "axios";

class ReviewAgent {
  public async getAllReviews(directorId: number, accessToken: string) {
    try {
      const res = await axios.get(`/api/v1/reviews/directors/${directorId}`, {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      });
      return res;
    } catch (e) {
      console.error(e);
      alert(e.response.data.message);
    }
  }

  public async patchAnswer(params: any, reviewId: number, accessToken: string) {
    try {
      const res = await axios.patch(`/api/v1/reviews/${reviewId}`, params, {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      });
      return res;
    } catch (e) {
      console.error(e);
      alert(e.response.data.message);
    }
  }

  public async getLectureReviews(lectureId: number) {
    try {
      const res = await axios.get(`/api/v1/lectures/${lectureId}/reviews`, {});
      return res;
    } catch (e) {
      console.error(e);
      alert(e.response.data.message);
    }
  }
}

export default new ReviewAgent();
