import axios from "axios";

class CouponAgent {
  public async getCoupons(accessToken: string) {
    try {
      const res = await axios.get(`/api/v1/mypage/directors/coupons`, {
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

  public async patchCoupons(lectureId: number, accessToken: string) {
    try {
      const res = await axios.patch(
        `/api/v1/mypage/directors/coupons/${lectureId}`,
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

  public async postCoupons(
    lectureId: number,
    params: any,
    accessToken: string
  ) {
    try {
      const res = await axios.post(
        `/api/v1/mypage/directors/coupons/${lectureId}`,
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
      alert(e.response.data.message);
    }
  }
}

export default new CouponAgent();
