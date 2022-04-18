import ApplyAgent from '../agents/ApplyAgent';
import {TokenDto} from '../login/User';
import UserDC from '../login/UserDC';
import LoginApiController from './LoginApiController';

class ApplyApiController {
  public async postApply(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('postApply: ', tokenDto);
    try {
      const res = await ApplyAgent.postApply(params, tokenDto.accessToken);
      console.log('ApplyApiController postApply: ', res);
      return res;
    } catch (e) {
      console.error(e);
      if (e === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        alert('다시 시도해주세요');
      } else if (e === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        alert('카카오 로그인을 다시 시도해주세요.');
      }
      throw e;
    }
  }

  public async deleteSchedule(scheduleId: number, teamId: number) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    try {
      const res = await ApplyAgent.deleteSchedule(
        scheduleId,
        teamId,
        tokenDto.accessToken,
      );
      return res;
    } catch (e) {
      console.error(e);
      if (e === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        alert('다시 시도해주세요');
      } else if (e === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        alert('카카오 로그인을 다시 시도해주세요.');
      }
      throw e;
    }
  }
}

export default new ApplyApiController();
