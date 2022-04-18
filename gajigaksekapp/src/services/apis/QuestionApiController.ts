import BulletinAgent from '../agents/BulletinAgent';
import GroupAgent from '../agents/GroupAgent';
import LectureAgent from '../agents/LectureAgent';
import QuestionAgent from '../agents/QuestionAgent';
import {TokenDto} from '../login/User';
import UserDC from '../login/UserDC';
import LoginApiController from './LoginApiController';

class QuestionApiController {
  public async postQuestion(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('postQuestion: ', tokenDto);
    try {
      const res = await QuestionAgent.postQuestion(
        params,
        tokenDto.accessToken,
      );
      console.log('QuestionApiController postQuestion: ', res);
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

  public async putQuestionEdit(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('postQuestionEdit: ', tokenDto);
    try {
      const res = await QuestionAgent.putQuestionEdit(
        params,
        tokenDto.accessToken,
      );
      console.log('QuestionApiController postQuestionEdit: ', res);
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

  public async deleteMypageQuestion(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('deleteMypageQuestion: ', tokenDto);
    try {
      const res = await QuestionAgent.deleteMypageQuestion(
        params,
        tokenDto.accessToken,
      );
      console.log('QuestionApiController deleteMypageQuestion: ', res);
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

  public async getQuestionDash(params: any) {
    let tokenDto: TokenDto;
    if (!UserDC.isLogout()) {
      tokenDto = (await UserDC.getUser()).tokenDto;
    }
    try {
      const res = await QuestionAgent.getQuestionDash(
        params,
        tokenDto ? tokenDto.accessToken : '',
      );
      console.log('QuestionApiController getQuestionDash: ', res);
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

export default new QuestionApiController();
