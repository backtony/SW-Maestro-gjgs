import Cookies from "universal-cookie";
import CouponAgent from "../agents/CouponAgent";

const cookies = new Cookies();

class CouponController {
  public async getCoupons() {
    const accessToken = cookies.get("accessToken");
    try {
      const res = CouponAgent.getCoupons(accessToken);
      return res;
    } catch (e) {
      console.error(e);
      throw e;
    }
  }

  public async patchCoupons(lectureId: number) {
    const accessToken = cookies.get("accessToken");
    try {
      const res = CouponAgent.patchCoupons(lectureId, accessToken);
      return res;
    } catch (e) {
      console.error(e);
      throw e;
    }
  }

  public async postCoupons(lectureId: number, params: any) {
    const accessToken = cookies.get("accessToken");
    try {
      const res = CouponAgent.postCoupons(lectureId, params, accessToken);
      return res;
    } catch (e) {
      throw e;
    }
  }
}

export default new CouponController();
