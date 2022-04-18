import GroupAgent from '../agents/GroupAgent';
import {TokenDto} from '../login/User';
import UserDC from '../login/UserDC';
import LoginApiController from './LoginApiController';

class GroupApiController {
  public async createTeam(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('createTeam: ', tokenDto);
    try {
      const res = await GroupAgent.createTeam(params, tokenDto.accessToken);
      console.log('GroupApiController: ', res);
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

  public async getTeamDash(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('getTeamDashTeam: ', tokenDto);
    try {
      const res = await GroupAgent.getTeamDash(params, tokenDto.accessToken);
      console.log('GroupApiController: ', res);
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

  public async getTeamList(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('getTeamList: ', tokenDto);
    try {
      const res = await GroupAgent.getTeamList(params, tokenDto.accessToken);
      console.log('GroupApiController get Team List: ', res);
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

  public async getPossibleTeamList(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('getPossibleTeamList: ', tokenDto);
    try {
      const res = await GroupAgent.getPossibleTeamList(
        params,
        tokenDto.accessToken,
      );
      console.log('GroupApiController getPossibleTeamList: ', res);
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

  public async editTeam(params: any, teamId: number) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('editTeam: ', tokenDto);
    try {
      const res = await GroupAgent.patchTeam(
        params,
        tokenDto.accessToken,
        teamId,
      );
      console.log('GroupApiController editTeam: ', res);
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

  public async patchLeader(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('patchLeader: ', tokenDto);
    try {
      const res = await GroupAgent.patchLeader(params, tokenDto.accessToken);
      console.log('GroupApiController patchLeader: ', res);
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

  public async deleteTeamWithLeader(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('deleteTeamWithLeader: ', tokenDto);
    try {
      const res = await GroupAgent.deleteTeamWithLeader(
        params,
        tokenDto.accessToken,
      );
      console.log('GroupApiController deleteTeamWithLeader: ', res);
      return res;
    } catch (e) {
      console.error(e);
      if (e === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        alert('그룹 나가기를 다시 시도해주세요');
      } else if (e === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        alert('카카오 로그인을 다시 시도해주세요.');
      }
    }
  }

  public async deleteTeamWithMember(params: any) {
    const tokenDto: TokenDto = (await UserDC.getUser()).tokenDto;
    console.log('deleteTeamWithMember: ', tokenDto);
    try {
      const res = await GroupAgent.deleteTeamWithMember(
        params,
        tokenDto.accessToken,
      );
      console.log('GroupApiController deleteTeamWithMember: ', res);
      return res;
    } catch (e) {
      console.error(e);
      if (e === 'TOKEN-401') {
        LoginApiController.getNewAccessToken(tokenDto);
        alert('그룹 나가기를 다시 시도해주세요');
      } else if (e === 'KAKAO-401') {
        UserDC.signOutWithKakao();
        alert('카카오 로그인을 다시 시도해주세요.');
      }
    }
  }
}

export default new GroupApiController();
