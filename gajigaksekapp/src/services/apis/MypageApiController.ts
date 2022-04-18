import GroupAgent from '../agents/GroupAgent';
import MypageAgent from '../agents/MypageAgent';
import {TokenDto} from '../login/User';
import UserDC from '../login/UserDC';
import {FileForm} from '../../screens/mypage/editMyProfile';
import LoginApiController from './LoginApiController';

class MypageApiController {
  public async getMypage(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('getMypage: ', tokenDto);
    try {
      const res = await MypageAgent.getMypage(params, tokenDto.accessToken);
      console.log('MypageApiController getMypage: ', res);
      return res;
    } catch (e) {
      console.error(e);
      if (e === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        alert('그룹 생성하기를 다시 시도해주세요');
      } else if (e === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        alert('카카오 로그인을 다시 시도해주세요.');
      }
    }
  }

  public async getBulletin(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('getBulletin: ', tokenDto);
    try {
      const res = await MypageAgent.getBulletin(params, tokenDto.accessToken);
      console.log('MypageApiController getBulletin: ', res);
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
    }
  }

  public async getProfileEdit(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('getProfileEdit: ', tokenDto);
    try {
      const res = await MypageAgent.getProfileEdit(
        params,
        tokenDto.accessToken,
      );
      console.log('MypageApiController getProfileEdit: ', res);
      return res;
    } catch (e) {
      console.error(e);
      if (e === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        alert('그룹 생성하기를 다시 시도해주세요');
      } else if (e === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        alert('카카오 로그인을 다시 시도해주세요.');
      }
    }
  }

  public async getProfile(params: any) {
    try {
      const res = await MypageAgent.getProfile(params);
      console.log('MypageApiController getProfile: ', res);
      return res;
    } catch (e) {
      console.error(e);
    }
  }

  public async putNicknameEdit(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('putNicknameEdit: ', tokenDto);
    try {
      const res = await MypageAgent.putNicknameEdit(
        params,
        tokenDto.accessToken,
      );
      console.log('MypageApiController putNicknameEdit: ', res);
      return res;
    } catch (e) {
      console.error(e);
      if (e === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        alert('그룹 생성하기를 다시 시도해주세요');
      } else if (e === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        alert('카카오 로그인을 다시 시도해주세요.');
      }
    }
  }

  public async putPhoneEdit(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('putPhoneEdit: ', tokenDto);
    try {
      const res = await MypageAgent.putPhoneEdit(params, tokenDto.accessToken);
      console.log('MypageApiController putPhoneEdit: ', res);
      return res;
    } catch (e) {
      console.error(e);
      if (e === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        alert('그룹 생성하기를 다시 시도해주세요');
      } else if (e === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        alert('카카오 로그인을 다시 시도해주세요.');
      }
    }
  }

  public async putTextEdit(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('putTextEdit: ', tokenDto);
    try {
      const res = await MypageAgent.putTextEdit(params, tokenDto.accessToken);
      console.log('MypageApiController putTextEdit: ', res);
      return res;
    } catch (e) {
      console.error(e);
      if (e === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        alert('그룹 생성하기를 다시 시도해주세요');
      } else if (e === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        alert('카카오 로그인을 다시 시도해주세요.');
      }
    }
  }

  public async postImage(file: FileForm) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('postImage: ', tokenDto);
    try {
      const res = await MypageAgent.postImage(file, tokenDto.accessToken);
      console.log('MypageApiController postImage: ', res);
      return res;
    } catch (e) {
      console.error(e);
      if (e === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        alert('그룹 생성하기를 다시 시도해주세요');
      } else if (e === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        alert('카카오 로그인을 다시 시도해주세요.');
      }
    }
  }

  public async putCategory(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('putCategory: ', tokenDto);
    try {
      const res = await MypageAgent.putCategory(params, tokenDto.accessToken);
      console.log('MypageApiController putCategory: ', res);
      return res;
    } catch (e) {
      console.error(e);
      if (e === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        alert('그룹 생성하기를 다시 시도해주세요');
      } else if (e === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        alert('카카오 로그인을 다시 시도해주세요.');
      }
    }
  }

  public async putZone(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('putZone: ', tokenDto);
    try {
      const res = await MypageAgent.putZone(params, tokenDto.accessToken);
      console.log('MypageApiController putZone: ', res);
      return res;
    } catch (e) {
      console.error(e);
      if (e === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        alert('그룹 생성하기를 다시 시도해주세요');
      } else if (e === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        alert('카카오 로그인을 다시 시도해주세요.');
      }
    }
  }

  public async getQuestion(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('getQuestion: ', tokenDto);
    try {
      const res = await MypageAgent.getQuestion(params, tokenDto.accessToken);
      console.log('MypageApiController getQuestion: ', res);
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
    }
  }

  public async postSwitchDirector() {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('postSwitchDirector: ', tokenDto);
    try {
      const res = await MypageAgent.postSwitchDirector(tokenDto.accessToken);
      console.log('MypageApiController postSwitchDirector: ', res);
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

  public async getReward(type: string) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('getReward: ', tokenDto);
    try {
      const res = await MypageAgent.getReward(type, tokenDto.accessToken);
      console.log('MypageApiController getReward: ', res);
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
    }
  }

  public async getLectures() {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('getLectures: ', tokenDto);
    try {
      const res = await MypageAgent.getLectures(tokenDto.accessToken);
      console.log('MypageApiController getLectures: ', res);
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
    }
  }

  public async getTeamPaymentStatus(scheduleId: number, teamId: number) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('getTeamPaymentStatus: ', tokenDto);
    try {
      const res = await MypageAgent.getTeamPaymentStatus(
        scheduleId,
        teamId,
        tokenDto.accessToken,
      );
      console.log('MypageApiController getTeamPaymentStatus: ', res);
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
    }
  }

  public async getOrder(orderId: number) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('getOrder: ', tokenDto);
    try {
      const res = await MypageAgent.getOrder(orderId, tokenDto.accessToken);
      console.log('MypageApiController getOrder: ', res);
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
    }
  }

  public async postReview(multiPart: FormData) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    try {
      const res = await MypageAgent.postReview(multiPart, tokenDto.accessToken);
      console.log('MypageApiController postReview: ', res);
      return res;
    } catch (e) {
      console.error(e);
      if (e === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        alert('그룹 생성하기를 다시 시도해주세요');
      } else if (e === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        alert('카카오 로그인을 다시 시도해주세요.');
      }
    }
  }

  public async getMyReview() {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    try {
      const res = await MypageAgent.getMyReview(tokenDto.accessToken);
      console.log('MypageApiController getMyReview: ', res);
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
    }
  }

  public async getDirectorReviews(directorId: number) {
    try {
      const res = await MypageAgent.getDirectorReviews(directorId);
      return res;
    } catch (e) {}
  }

  public async getNotices() {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    try {
      const res = await MypageAgent.getNotices(tokenDto.accessToken);
      console.log('MypageApiController getNotices: ', res);
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
    }
  }

  public async getNoticeDetail(noticeId: number) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    try {
      const res = await MypageAgent.getNoticeDetail(
        noticeId,
        tokenDto.accessToken,
      );
      console.log('MypageApiController getNoticeDetail: ', res);
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
    }
  }

  public async getAlarmStatus() {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    try {
      const res = await MypageAgent.getAlarmStatus(tokenDto.accessToken);
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
    }
  }

  public async postAlarmStatus(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    try {
      const res = await MypageAgent.postAlarmStatus(
        params,
        tokenDto.accessToken,
      );
      return res;
    } catch (e) {
      console.error(e);
      if (e === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        alert('그룹 생성하기를 다시 시도해주세요');
      } else if (e === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        alert('카카오 로그인을 다시 시도해주세요.');
      }
    }
  }
}

export default new MypageApiController();
