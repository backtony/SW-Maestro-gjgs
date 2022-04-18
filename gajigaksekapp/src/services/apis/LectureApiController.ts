import BulletinAgent from '../agents/BulletinAgent';
import GroupAgent from '../agents/GroupAgent';
import LectureAgent from '../agents/LectureAgent';
import {TokenDto} from '../login/User';
import UserDC from '../login/UserDC';
import LoginApiController from './LoginApiController';

class LectureApiController {
  public async getLectureList(params: any) {
    let tokenDto: TokenDto;
    if (!UserDC.isLogout()) {
      tokenDto = (await UserDC.getUser()).tokenDto;
    }
    try {
      const res = await LectureAgent.getLectureList(
        params,
        tokenDto ? tokenDto.accessToken : '',
      );
      console.log('LectureApiController getLectureList: ', res);
      return res;
    } catch (e) {
      console.error(e);
      if (e === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        console.log('??');
        // alert('클래스 검색을 다시 시도해주세요');
      } else if (e === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        alert('카카오 로그인을 다시 시도해주세요.');
      }
      throw e;
    }
  }

  public async getRecommendLectureList(categoryIdList: number[]) {
    let tokenDto: TokenDto;
    if (!UserDC.isLogout()) {
      tokenDto = (await UserDC.getUser()).tokenDto;
    }
    try {
      const res = await LectureAgent.getRecommendLectureList(
        categoryIdList,
        tokenDto ? tokenDto.accessToken : '',
      );
      console.log('LectureApiController getLectureList: ', res);
      return res;
    } catch (e) {
      console.error(e);
      if (e === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        console.log('??');
        // alert('클래스 검색을 다시 시도해주세요');
      } else if (e === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        alert('카카오 로그인을 다시 시도해주세요.');
      }
      throw e;
    }
  }

  public async getLectureDash(params: any) {
    let tokenDto: TokenDto;
    if (!UserDC.isLogout()) {
      tokenDto = (await UserDC.getUser()).tokenDto;
    }
    try {
      const res = await LectureAgent.getLectureDash(
        params,
        tokenDto ? tokenDto.accessToken : '',
      );
      console.log('LectureApiController getLectureDash: ', res);
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

  public async getQuestion(params: any) {
    let tokenDto: TokenDto;
    if (!UserDC.isLogout()) {
      tokenDto = (await UserDC.getUser()).tokenDto;
    }
    try {
      const res = await LectureAgent.getQuestion(
        params,
        tokenDto ? tokenDto.accessToken : '',
      );
      console.log('LectureApiController getQuestion: ', res);
      return res;
    } catch (e) {
      console.error(e);
      if (e === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        alert('다시 시도해주세요');
      } else if (e === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        alert('다시 시도해주세요.');
      }
      throw e;
    }
  }

  public async getDirectorLectures(directorId: number) {
    let tokenDto: TokenDto;
    if (!UserDC.isLogout()) {
      tokenDto = (await UserDC.getUser()).tokenDto;
    }
    try {
      const res = await LectureAgent.getDirectorLectures(
        directorId,
        tokenDto ? tokenDto.accessToken : '',
      );
      return res;
    } catch (e) {
      if (e === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        alert('다시 시도해주세요');
      } else if (e === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        alert('다시 시도해주세요.');
      }
    }
  }

  public async getLectureReviews(lectureId: number) {
    try {
      const res = await LectureAgent.getLectureReviews(lectureId);
      return res;
    } catch (e) {}
  }
}

export default new LectureApiController();
