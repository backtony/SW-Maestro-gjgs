import GoogleAgent from '../agents/GoogleAgent';

class GoogleMapApiController {
  public async getRegion(address: string) {
    try {
      const res = await GoogleAgent.getCoordinates(address);
      return res?.data.results[0].geometry.location;
    } catch (e) {
      console.error(e);
    }
  }
}

export default new GoogleMapApiController();
