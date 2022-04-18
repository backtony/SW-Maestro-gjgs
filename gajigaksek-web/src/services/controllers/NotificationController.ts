import Cookies from "universal-cookie";
import NotificationAgent from "../agents/NotificationAgent";

const cookies = new Cookies();

class NotificationController {
  public async postNotification(params: any) {
    const accessToken = cookies.get("accessToken");
    try {
      const res = NotificationAgent.postNotification(params, accessToken);
      return res;
    } catch (e) {
      throw e;
    }
  }
}

export default new NotificationController();
