import axios from "axios";

class NotificationAgent {
  public async postNotification(params: any, accessToken: string) {
    try {
      const res = await axios.post(`/api/v1/notification`, params, {
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
}

export default new NotificationAgent();
