import {Platform, NativeModules} from 'react-native';
import {strJsonType} from './Types';
const {StatusBarManager} = NativeModules;
export const STATUSBAR_HEIGHT =
  Platform.OS === 'ios' ? StatusBarManager.HEIGHT : 0;

//API 설정 가이드
// 1. 적용시에는 2. 주석처리 후 1. 주석해제하여 빌드
// 2. 적용시에는 1. 주석처리 후 2. 주석해제하여 빌드

//1. localhost:8080 이용시

export const IOS_LOCALHOST_URL = 'http://127.0.0.1:8080/api/v1'; //iOS localhost
export const ANDROID_LOCALHOST_URL = 'http://10.0.2.2:8080/api/v1'; // Android localhost
export const API_URL =
  Platform.OS === 'ios' ? IOS_LOCALHOST_URL : ANDROID_LOCALHOST_URL;

//2. gjgs-text.com 이용시

// export const API_URL = 'https://gjgs-test.com/api/v1';

export const DAY_OF_WEEK = [
  ['일', '#ff4f4f', 'SUN'],
  ['월', '#1d1d1f', 'MON'],
  ['화', '#1d1d1f', 'TUE'],
  ['수', '#1d1d1f', 'WED'],
  ['목', '#1d1d1f', 'THU'],
  ['금', '#1d1d1f', 'FRI'],
  ['토', '#4f6cff', 'SAT'],
];

export const englishAge: strJsonType = {
  TWENTY_TO_TWENTYFIVE: '20~25세',
  TWENTYFIVE_TO_THIRTY: '25~30세',
  THIRTY_TO_THIRTYFIVE: '30~35세',
  THIRTYFIVE_TO_FORTY: '35세~40세',
  FORTY: '40세 이상',
};

export const englishTime: strJsonType = {
  MORNING: '오전',
  NOON: '오후',
  AFTERNOON: '저녁',
};

export const englishDay: strJsonType = {
  SUN: '일',
  MON: '월',
  TUE: '화',
  WED: '수',
  THU: '목',
  FRI: '금',
  SAT: '토',
};

export const impUid: string = 'imp52333583';

export const sortOrderList: strJsonType = {
  인기높은순: 'clickCount,desc',
  최신순: 'createdDate,desc',
  평점높은순: 'score,desc',
  리뷰많은순: 'reviewCount,desc',
  '': '',
};

export const searchPriceList: strJsonType = {
  전체: '',
  '~5만원': 'LOWER_EQUAL_FIVE',
  '5~10만원': 'FIVE_TO_TEN',
  '10만원~': 'GREATER_EQUAL_TEN',
  '': '',
};
