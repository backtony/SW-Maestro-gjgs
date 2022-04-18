import GroupAgent from '../agents/GroupAgent';
import TeamManageAgent from '../agents/TeamManageAgent';
import {TokenDto} from '../login/User';
import UserDC from '../login/UserDC';
import LoginApiController from './LoginApiController';

class TeamManageApiController {
  public async getAppliers(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('getAppliers: ', tokenDto);
    try {
      const res = await TeamManageAgent.getAppliers(
        params,
        tokenDto.accessToken,
      );
      console.log('TeamManageApiController getAppliers: ', res);
      return res;
    } catch (e) {
      console.error(e);
      if (e === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        alert('신청자리스트를 다시 열어주세요.');
      } else if (e === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        alert('카카오 로그인을 다시 시도해주세요.');
      }
      throw e;
    }
  }

  public async postApplierConfirm(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('postApplierConfirm: ', tokenDto);
    try {
      const res = await TeamManageAgent.postApplierConfirm(
        params,
        tokenDto.accessToken,
      );
      console.log('TeamManageApiController postApplierConfirm: ', res);
      return res;
    } catch (e) {
      console.error(e);
      if (e === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        alert('신청자리스트를 다시 열어주세요.');
      } else if (e === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        alert('카카오 로그인을 다시 시도해주세요.');
      }
      throw e;
    }
  }

  public async postTeamApply(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('postTeamApply: ', tokenDto);
    try {
      const res = await TeamManageAgent.postTeamApply(
        params,
        tokenDto.accessToken,
      );
      console.log('TeamManageApiController postTeamApply: ', res);
      return res;
    } catch (e) {
      console.error(e);
      if (e === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        alert('다시 시도해주세요.');
      } else if (e === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        alert('카카오 로그인을 다시 시도해주세요.');
      }
      throw e;
    }
  }

  public async deleteApplier(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('deleteApplier: ', tokenDto);
    try {
      const res = await TeamManageAgent.deleteApplier(
        params,
        tokenDto.accessToken,
      );
      console.log('TeamManageApiController deleteApplier: ', res);
      return res;
    } catch (e) {
      console.error(e);
      if (e === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        alert('신청자리스트를 다시 열어주세요.');
      } else if (e === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        alert('카카오 로그인을 다시 시도해주세요.');
      }
      throw e;
    }
  }

  public async deleteMember(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('deleteMember: ', tokenDto);
    try {
      const res = await TeamManageAgent.deleteMember(
        params,
        tokenDto.accessToken,
      );
      console.log('TeamManageApiController deleteMember: ', res);
      return res;
    } catch (e) {
      console.error(e);
      if (e === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        alert('다시 시도해주세요.');
      } else if (e === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        alert('카카오 로그인을 다시 시도해주세요.');
      }
      throw e;
    }
  }
}

export default new TeamManageApiController();
