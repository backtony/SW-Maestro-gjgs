import Cookies from "universal-cookie";
import ReviewAgent from "../agents/ReviewAgent";

const cookies = new Cookies();

class ReviewController {
  public async getAllReviews(directorId: number) {
    const accessToken = cookies.get("accessToken");
    try {
      const res = ReviewAgent.getAllReviews(directorId, accessToken);
      return res;
    } catch (e) {
      throw e;
    }
  }

  public async patchAnswer(params: any, reviewId: number) {
    const accessToken = cookies.get("accessToken");
    try {
      const res = ReviewAgent.patchAnswer(params, reviewId, accessToken);
      return res;
    } catch (e) {
      throw e;
    }
  }

  public async getLectureReviews(lectureId: number) {
    try {
      const res = ReviewAgent.getLectureReviews(lectureId);
      return res;
    } catch (e) {
      throw e;
    }
  }
}

export default new ReviewController();
