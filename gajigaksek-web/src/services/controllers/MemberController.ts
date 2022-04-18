import Cookies from "universal-cookie";
import MemberAgent from "../agents/MemberAgent";

const cookies = new Cookies();

class MemberController {
  public async getMember(params: any) {
    const accessToken = cookies.get("accessToken");
    try {
      const res = MemberAgent.getMember(params, accessToken);
      return res;
    } catch (e) {
      console.error(e);
      throw e;
    }
  }
}

export default new MemberController();
