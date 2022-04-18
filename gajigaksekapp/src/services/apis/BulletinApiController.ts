import BulletinAgent from '../agents/BulletinAgent';
import GroupAgent from '../agents/GroupAgent';
import {TokenDto} from '../login/User';
import UserDC from '../login/UserDC';
import LoginApiController from './LoginApiController';

class BulletinApiController {
  public async postCreateBulletin(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('postCreateBulletin: ', tokenDto);
    try {
      const res = await BulletinAgent.postCreateBulletin(
        params,
        tokenDto.accessToken,
      );
      console.log('BulletinApiController postCreateBulletin: ', res);
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
      throw e;
    }
  }

  public async getSearchBulletin(params: any) {
    let tokenDto: TokenDto;
    if (!UserDC.isLogout()) {
      tokenDto = (await UserDC.getUser()).tokenDto;
    }
    try {
      const res = await BulletinAgent.getSearchBulletin(
        params,
        tokenDto ? tokenDto.accessToken : '',
      );
      console.log('BulletinApiController getSearchBulletin: ', res);
      return res;
    } catch (e) {
      console.error(e);
      throw e;
    }
  }

  public async getLectureBulletin(params: any, lectureId: string) {
    let tokenDto: TokenDto;
    if (!UserDC.isLogout()) {
      tokenDto = (await UserDC.getUser()).tokenDto;
    }
    try {
      const res = await BulletinAgent.getLectureBulletin(
        params,
        lectureId,
        tokenDto ? tokenDto.accessToken : '',
      );
      console.log('BulletinApiController getLectureBulletin: ', res);
      return res;
    } catch (e) {
      console.error(e);
      throw e;
    }
  }

  public async getBulletinDash(params: any) {
    let tokenDto: TokenDto;
    if (!UserDC.isLogout()) {
      tokenDto = (await UserDC.getUser()).tokenDto;
    }
    console.log('postCreateBulletin: ', tokenDto);
    try {
      const res = await BulletinAgent.getBulletinDash(
        params,
        tokenDto ? tokenDto.accessToken : '',
      );
      console.log('BulletinApiController getBulletinDash: ', res);
      return res;
    } catch (e) {
      console.error(e);
      throw e;
    }
  }

  public async deleteBulletin(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('deleteBulletin: ', tokenDto);
    try {
      const res = await BulletinAgent.deleteBulletin(
        params,
        tokenDto.accessToken,
      );
      console.log('BulletinApiController deleteBulletin: ', res);
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
      throw e;
    }
  }

  public async patchBulletinEdit(params: any, bulletinId: number) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('patchBulletinEdit: ', tokenDto);
    try {
      const res = await BulletinAgent.patchBulletinEdit(
        params,
        bulletinId,
        tokenDto.accessToken,
      );
      console.log('BulletinApiController patchBulletinEdit: ', res);
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

  public async patchBulletinStatus(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('patchBulletinStatus: ', tokenDto);
    try {
      const res = await BulletinAgent.patchBulletinStatus(
        params,
        tokenDto.accessToken,
      );
      console.log('BulletinApiController patchBulletinStatus: ', res);
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

export default new BulletinApiController();
