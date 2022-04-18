import {KakaoOAuthToken} from '@react-native-seoul/kakao-login';

export class TokenDto {
  public grantType: String;
  public accessToken: String;
  public refreshToken: String;
  public accessTokenExpiresIn: number;
  public refreshTokenExpiresIn: number;
}

export class UserApiData {
  public tokenDto: TokenDto;
  public memberId: number;
  public id: number;
  public imageFileUrl: string;
}

export default class User {
  public accessToken: string;
  public JwtToken: string;
  public nickname: string;
  public name: string;
  public phone: string;
  public memberId: number;
  public id: number;
  public imageFileUrl: string;
  public tokenDto: TokenDto;
  public kakaoToken: KakaoOAuthToken;

  constructor(kakaoToken: KakaoOAuthToken) {
    this.kakaoToken = kakaoToken;
  }

  public setData(data: UserApiData) {
    console.log('setData: ', data);
    if (data?.tokenDto) {
      this.tokenDto = data.tokenDto;
    }
    if (data?.memberId) {
      this.memberId = data.memberId;
    }
    if (data?.id) {
      this.id = data.id;
    }
    if (data?.imageFileUrl) {
      this.imageFileUrl = data.imageFileUrl;
    }
  }
}
