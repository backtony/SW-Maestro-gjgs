import axios from 'axios';
import {Alert} from 'react-native';
import {GoogleMapKey} from '../../utils/privateKeys/GoogleKey';

class GoogleAgent {
  public async getCoordinates(address: string) {
    try {
      const res = await axios.get(
        `https://maps.googleapis.com/maps/api/geocode/json?address=${address}&key=${GoogleMapKey}`,
      );
      return res;
    } catch (e) {
      Alert.alert('지도 위치 로드에 실패했습니다.');
      console.error(e);
    }
  }
}

export default new GoogleAgent();
