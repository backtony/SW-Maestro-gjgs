import React from 'react';
import {createStackNavigator} from '@react-navigation/stack';
import EightCategoryView from '../../screens/home/eightCategory/EightCategoryView';
import FavoriteZoneLectureView from '../../screens/home/favoriteZone/FavoriteZoneLectureView';
import CategorySelect from '../../screens/mypage/CategorySelect';
import BulletinMainView from '../../screens/home/bulletin/BulletinMainView';
import BulletinAddView from '../../screens/home/bulletin/BulletinAddView';
import BulletinDetailView from '../../screens/home/bulletin/BulletinDetailView';
import BulletinEditView from '../../screens/home/bulletin/BulletinEditView';
import BulletinSearchView from '../../screens/home/bulletin/BulletinSearchView';
import BulletinSearchResultView from '../../screens/home/bulletin/BulletinSearchResultView';
import LectureDetailView from '../../screens/home/lecture/LectureDetailView';
import FirbaseChatRoom from '../../services/firebaseAuth/FirebaseChatRoom';
import LectureSearchView from '../../screens/home/lecture/LectureSearchView';
import LectureSearchResultView from '../../screens/home/lecture/LectureSearchResultView';
import RecommendLectureView from '../../screens/home/lecture/RecommendLectureView';
import LectureQuestionView from '../../screens/home/lecture/LectureQuestionView';
import LectureQuestionWriteView from '../../screens/home/lecture/LectureQuestionWriteView';
import LectureQuestionDetailView from '../../screens/home/lecture/LectureQuestionDetailView';
import HomeView from '../../screens/home/HomeView';
import LectureBulletinView from '../../screens/home/lecture/LectureBulletinView';
import MatchingZoneView from '../../screens/home/bulletin/MatchingZoneView';
import MatchingCategoryView from '../../screens/home/bulletin/MatchingCategoryView';
import MatchingDetail from '../../screens/home/bulletin/MatchingDetailView';
import Profile from '../../screens/profile/Profile';
import PaymentView from '@/screens/payment/PaymentView';
import TmpPaymentView from '@/screens/payment/TmpPaymentView';
import DirectorProfileView from '@/screens/home/lecture/DirectorProfileView';

const homeStack = createStackNavigator();

class HomeStackNavigator extends React.Component {
  render() {
    return (
      <homeStack.Navigator initialRouteName="home">
        <homeStack.Screen
          name="home"
          component={HomeView}
          options={{
            headerShown: false,
          }}
        />
        <homeStack.Screen
          name="lectureSearch"
          component={LectureSearchView}
          options={{
            headerShown: false,
          }}
        />
        <homeStack.Screen
          name="lectureSearchResult"
          component={LectureSearchResultView}
          options={{
            headerShown: false,
          }}
        />
        <homeStack.Screen
          name="eightCategory"
          component={EightCategoryView}
          options={{
            headerShown: false,
          }}
        />
        <homeStack.Screen
          name="favoriteZoneLecture"
          component={FavoriteZoneLectureView}
          options={{
            headerShown: false,
          }}
        />
        <homeStack.Screen
          name="favoriteZoneLectureSubCategory"
          component={CategorySelect}
          options={{
            headerTitle: '관심분야 선택',
          }}
        />
        <homeStack.Screen
          name="recommendLecture"
          component={RecommendLectureView}
          options={{
            headerShown: false,
          }}
        />
        <homeStack.Screen
          name="bulletinMain"
          component={BulletinMainView}
          options={{
            headerShown: false,
          }}
        />
        <homeStack.Screen
          name="bulletinAdd"
          component={BulletinAddView}
          options={{
            headerShown: false,
          }}
        />
        <homeStack.Screen
          name="bulletinDash"
          component={BulletinDetailView}
          options={{
            headerShown: false,
          }}
        />
        <homeStack.Screen
          name="bulletinEdit"
          component={BulletinEditView}
          options={{
            headerShown: false,
          }}
        />
        <homeStack.Screen
          name="bulletinSearch"
          component={BulletinSearchView}
          options={{
            headerShown: false,
          }}
        />
        <homeStack.Screen
          name="bulletinSearchResult"
          component={BulletinSearchResultView}
          options={{
            headerShown: false,
          }}
        />
        <homeStack.Screen
          name="lectureDash"
          component={LectureDetailView}
          options={{
            headerShown: false,
          }}
        />

        <homeStack.Screen
          name="lectureQuestion"
          component={LectureQuestionView}
          options={{
            headerShown: false,
          }}
        />

        <homeStack.Screen
          name="lectureQuestionWrite"
          component={LectureQuestionWriteView}
          options={{
            headerShown: false,
          }}
        />

        <homeStack.Screen
          name="lectureQuestionDash"
          component={LectureQuestionDetailView}
          options={{
            headerShown: false,
          }}
        />

        <homeStack.Screen
          name="lectureBulletin"
          component={LectureBulletinView}
          options={{
            headerShown: false,
          }}
        />

        <homeStack.Screen
          name="matchingZone"
          component={MatchingZoneView}
          options={{
            headerShown: false,
          }}
        />

        <homeStack.Screen
          name="matchingMainCategory"
          component={MatchingCategoryView}
          options={{
            headerShown: false,
          }}
        />

        <homeStack.Screen
          name="matchingDetail"
          component={MatchingDetail}
          options={{
            headerShown: false,
          }}
        />

        <homeStack.Screen
          name="chatroom"
          component={FirbaseChatRoom}
          options={{
            headerShown: false,
          }}
        />

        <homeStack.Screen
          name="profile"
          component={Profile}
          options={{
            headerShown: false,
          }}
        />

        <homeStack.Screen
          name="payment"
          component={PaymentView}
          options={{
            headerShown: false,
          }}
        />

        <homeStack.Screen
          name="tmpPayment"
          component={TmpPaymentView}
          options={{
            headerShown: false,
          }}
        />

        <homeStack.Screen
          name="directorProfile"
          component={DirectorProfileView}
          options={{
            headerShown: false,
          }}
        />

        <homeStack.Screen
          name="lectureDash2"
          component={LectureDetailView}
          options={{
            headerShown: false,
          }}
        />
      </homeStack.Navigator>
    );
  }
}

export default HomeStackNavigator;
