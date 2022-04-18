import {
  KakaoOAuthToken,
  KakaoProfile,
  login,
  logout,
  getProfile as getKakaoProfile,
} from '@react-native-seoul/kakao-login';
import AsyncStorage from '@react-native-async-storage/async-storage';
import User from './User';
import axios from 'axios';
import LoginApiController from '../apis/LoginApiController';
import FirebaseAuth from '../firebaseAuth/FirebaseAuth';

class UserDC {
  private user: User;
  private listeners: UserDCListener[];

  constructor() {
    this.user = null;
    this.loadAsyncStorage();
    this.listeners = [];
  }

  public async loadAsyncStorage() {
    const storedUser = await AsyncStorage.getItem('user');
    if (storedUser) {
      console.log('storedUser: ', JSON.parse(storedUser));
      this.user = JSON.parse(storedUser);
    }
  }

  public addListener(listener: UserDCListener) {
    this.listeners.push(listener); //ss
  }

  public signInWithKakao = async (): Promise<void> => {
    try {
      const token: KakaoOAuthToken = await login();
      this.user = new User(token);
      await this.getProfile();
      await FirebaseAuth.signInWithFirebase(this.user.id);
      const res = await LoginApiController.login(token);

      console.log('UserDC res: ', res);

      if (res) {
        this.user.setData(res.data);
      }

      AsyncStorage.setItem('user', JSON.stringify(this.user));
      this.listeners.forEach(listener => {
        listener.userLoggedIn(this.user);
      });
      console.log('user: ', this.user);
    } catch (e) {
      console.log('카카오 로그인 에러: ', e);
    }
  };

  public signOutWithKakao = async (): Promise<void> => {
    try {
      this.removeCurrentUser();
      const message = await logout();

      LoginApiController.logout(this.user.tokenDto);

      FirebaseAuth.signOutWithFirebase();
    } catch (e) {
      console.log('카카오 로그아웃 에러: ', e);
    }
  };

  public removeCurrentUser = () => {
    this.user = undefined;
    AsyncStorage.setItem('user', JSON.stringify(null));
    this.listeners.forEach(listener => {
      listener.userLoggedOut();
    });
  };

  public getProfile = async (): Promise<User> => {
    if (!this.user) {
      await this.signInWithKakao();
    }
    const profile: KakaoProfile = await getKakaoProfile();
    this.user.nickname = profile.nickname;
    this.user.id = +profile.id;
    this.user.imageFileUrl = profile.thumbnailImageUrl;
    return this.user;
  };

  public async setData(data: any) {
    console.log('UserDC setData');
    if (this.user && data) {
      console.log('before setData user: ', this.user);
      console.log('before data: ', data);
      const setData2 = this.user.setData.bind(this.user);
      setData2(data);
      // this.user.setData(data);
      console.log('setData: ', this.user);
      this.listeners.forEach(listener => {
        listener.userLoggedIn(this.user);
      });
    }
  }

  public async setFakeUser(data: any) {
    this.user = new User({
      accessToken: 'assdff',
      refreshToken: 'assss',
      accessTokenExpiresAt: new Date(),
      refreshTokenExpiresAt: new Date(),
      scopes: ['sss'],
    });

    this.setData(data);
  }

  public isLogout(): boolean {
    return !this.user;
  }

  public getUser(): User {
    // if (!this.user && AsyncStorage.getItem('user')) {
    //   const storedUser = await AsyncStorage.getItem('user');
    //   if (storedUser) {
    //     this.user = JSON.parse(storedUser);
    //     this.listeners.forEach(listener => {
    //       listener.userLoggedIn(this.user);
    //     });
    //   }
    // }
    return this.user;
  }
}

export interface UserDCListener {
  userLoggedIn(user: User): void;
  userLoggedOut(): void;
}

export default new UserDC();
