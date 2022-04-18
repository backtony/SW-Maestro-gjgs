import BulletinAgent from '../agents/BulletinAgent';
import FavoriteAgent from '../agents/FavoriteAgent';
import GroupAgent from '../agents/GroupAgent';
import LectureAgent from '../agents/LectureAgent';
import {TokenDto} from '../login/User';
import UserDC from '../login/UserDC';
import LoginApiController from './LoginApiController';

class FavoriteApiController {
  public async postFavoriteIndividual(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('postFavoriteIndividual: ', tokenDto);
    try {
      const res = await FavoriteAgent.postFavoriteIndividual(
        params,
        tokenDto.accessToken,
      );
      console.log('FavoriteApiController postFavoriteIndividual: ', res);
      return res;
    } catch (e) {
      console.error(e);
      if (e === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        alert('찜하기를 다시 시도해주세요');
      } else if (e === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        alert('카카오 로그인을 다시 시도해주세요.');
      }
      throw e;
    }
  }

  public async deleteFavoriteIndividual(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('deleteFavoriteIndividual: ', tokenDto);
    try {
      const res = await FavoriteAgent.deleteFavoriteIndividual(
        params,
        tokenDto.accessToken,
      );
      console.log('FavoriteApiController deleteFavoriteIndividual: ', res);
      return res;
    } catch (e) {
      console.error(e);
      if (e === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        alert('찜하기를 다시 시도해주세요');
      } else if (e === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        alert('카카오 로그인을 다시 시도해주세요.');
      }
      throw e;
    }
  }

  public async getTeamList(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('getTeamList: ', tokenDto);
    try {
      const res = await FavoriteAgent.getTeamList(params, tokenDto.accessToken);
      console.log('FavoriteApiController getTeamList: ', res);
      return res;
    } catch (e) {
      console.error(e);
      if (e === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        alert('찜하기를 다시 시도해주세요');
      } else if (e === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        alert('카카오 로그인을 다시 시도해주세요.');
      }
      throw e;
    }
  }

  public async getLectureIndividual(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('getLectureIndividual: ', tokenDto);
    try {
      const res = await FavoriteAgent.getLectureIndividual(
        params,
        tokenDto.accessToken,
      );
      console.log('FavoriteApiController getLectureIndividual: ', res);
      return res;
    } catch (e) {
      console.error(e);
      if (e === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        alert('다시 로드해주세요');
      } else if (e === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        alert('카카오 로그인을 다시 시도해주세요.');
      }
      throw e;
    }
  }

  public async getBulletin(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('getBulletin: ', tokenDto);
    try {
      const res = await FavoriteAgent.getBulletin(params, tokenDto.accessToken);
      console.log('FavoriteApiController getBulletin: ', res);
      return res;
    } catch (e) {
      console.error(e);
      if (e === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        alert('다시 로드해주세요');
      } else if (e === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        alert('카카오 로그인을 다시 시도해주세요.');
      }
      throw e;
    }
  }

  public async getTeamLecture(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('getTeamLecture: ', tokenDto);
    try {
      const res = await FavoriteAgent.getTeamLecture(
        params,
        tokenDto.accessToken,
      );
      console.log('FavoriteApiController getTeamLecture: ', res);
      return res;
    } catch (e) {
      console.error(e);
      if (e === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        alert('다시 로드해주세요');
      } else if (e === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        alert('카카오 로그인을 다시 시도해주세요.');
      }
      throw e;
    }
  }

  public async postFavoriteTeam(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('postFavoriteTeam: ', tokenDto);
    try {
      const res = await FavoriteAgent.postFavoriteTeam(
        params,
        tokenDto.accessToken,
      );
      console.log('FavoriteApiController postFavoriteTeam: ', res);
      return res;
    } catch (e) {
      console.error(e);
      if (e === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        alert('찜하기를 다시 시도해주세요');
      } else if (e === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        alert('카카오 로그인을 다시 시도해주세요.');
      }
      throw e;
    }
  }

  public async postFavoriteBulletin(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('postFavoriteBulletin: ', tokenDto);
    try {
      const res = await FavoriteAgent.postFavoriteBulletin(
        params,
        tokenDto.accessToken,
      );
      console.log('FavoriteApiController postFavoriteBulletin: ', res);
      return res;
    } catch (e) {
      console.error(e);
      if (e === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        alert('찜하기를 다시 시도해주세요');
      } else if (e === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        alert('카카오 로그인을 다시 시도해주세요.');
      }
      throw e;
    }
  }

  public async deleteFavoriteTeam(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('deleteFavoriteTeam: ', tokenDto);
    try {
      const res = await FavoriteAgent.deleteFavoriteTeam(
        params,
        tokenDto.accessToken,
      );
      console.log('FavoriteApiController deleteFavoriteTeam: ', res);
      return res;
    } catch (e) {
      console.error(e);
      if (e === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        alert('찜하기를 다시 시도해주세요');
      } else if (e === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        alert('카카오 로그인을 다시 시도해주세요.');
      }
      throw e;
    }
  }

  public async deleteFavoriteBulletin(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('deleteFavoriteBulletin: ', tokenDto);
    try {
      const res = await FavoriteAgent.deleteFavoriteBulletin(
        params,
        tokenDto.accessToken,
      );
      console.log('FavoriteApiController deleteFavoriteBulletin: ', res);
      return res;
    } catch (e) {
      console.error(e);
      if (e === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        alert('찜하기를 다시 시도해주세요');
      } else if (e === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        alert('카카오 로그인을 다시 시도해주세요.');
      }
      throw e;
    }
  }

  public async deleteTeamLecture(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('deleteTeamLecture: ', tokenDto);
    try {
      const res = await FavoriteAgent.deleteTeamLecture(
        params,
        tokenDto.accessToken,
      );
      console.log('FavoriteApiController deleteTeamLecture: ', res);
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

export default new FavoriteApiController();
