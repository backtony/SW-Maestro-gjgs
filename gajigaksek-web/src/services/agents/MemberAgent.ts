import axios from "axios";

class MemberAgent {
  public async getMember(params: any, accessToken: string) {
    let queryParams = "";

    if (params.authority) {
      if (queryParams.length <= 0) {
        queryParams += "?";
      } else {
        queryParams += "&";
      }
      queryParams += `authority=${params.authority}`;
    }

    if (params.createdDateEnd) {
      if (queryParams.length <= 0) {
        queryParams += "?";
      } else {
        queryParams += "&";
      }
      queryParams += `createdDateEnd=${params.createdDateEnd}`;
    }

    if (params.createdDateStart) {
      if (queryParams.length <= 0) {
        queryParams += "?";
      } else {
        queryParams += "&";
      }
      queryParams += `createdDateStart=${params.createdDateStart}`;
    }

    if (params.nickname) {
      if (queryParams.length <= 0) {
        queryParams += "?";
      } else {
        queryParams += "&";
      }
      queryParams += `nickname=${params.nickname}`;
    }

    try {
      const res = await axios.get(`/api/v1/members${queryParams}`, {
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

export default new MemberAgent();
