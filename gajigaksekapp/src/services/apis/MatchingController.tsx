import {Alert} from 'react-native';
import MatchingAgent from '../agents/MatchingAgent';
import {TokenDto} from '../login/User';
import UserDC from '../login/UserDC';
import LoginApiController from './LoginApiController';

class MatchingController {
  public async postMatching(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('postMatching: ', tokenDto);
    try {
      const res = await MatchingAgent.postMatching(
        params,
        tokenDto.accessToken,
      );
      console.log('postMatching: ', res);
      return res;
    } catch (e) {
      console.error(e);
      if (e === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        Alert.alert('다시 시도해주세요');
      } else if (e === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        Alert.alert('카카오 로그인을 다시 시도해주세요.');
      }
      throw e;
    }
  }

  public async getMatchingStatus() {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    try {
      const res = await MatchingAgent.getMatchingStatus(tokenDto.accessToken);
      return res;
    } catch (e) {
      console.error(e);
      if (e === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        Alert.alert('다시 시도해주세요');
      } else if (e === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        Alert.alert('카카오 로그인을 다시 시도해주세요.');
      }
      throw e;
    }
  }

  public async deleteMatching() {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    try {
      const res = await MatchingAgent.deleteMatching(tokenDto.accessToken);
      return res;
    } catch (e) {
      console.error(e);
      if (e === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        Alert.alert('다시 시도해주세요');
      } else if (e === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        Alert.alert('카카오 로그인을 다시 시도해주세요.');
      }
      throw e;
    }
  }
}

export default new MatchingController();
