import React from 'react';
import {createStackNavigator} from '@react-navigation/stack';
import mypage from '../../screens/mypage/mypage';
import alarmListPage from '../../screens/mypage/alaramListPage';
import settingPage from '../../screens/mypage/settingpage/settingPage';
import Profile from '../../screens/profile/Profile';
import editMyProfile from '../../screens/mypage/editMyProfile';
import CategorySelect from '../../screens/mypage/CategorySelect';
import MyPageBulletin from '../../screens/mypage/MyPageBulletin';
import QuestionListPage from '../../screens/mypage/questionListPage/QuestionListPage';
import editDirectorProfile from '../../screens/mypage/editDirectorProfile';
import FirstLogin from '../../screens/mypage/firstLogin/FirstLogin';
import FirstSubCategory from '../../screens/mypage/firstLogin/FirstSubCategory';
import editMyProfileCategorySelect from '../../screens/mypage/editMyProfileCategorySelect';
import BulletinDetailView from '../../screens/home/bulletin/BulletinDetailView';
import LectureQuestionDetailView from '../../screens/home/lecture/LectureQuestionDetailView';
import LectureQuestionEditView from '../../screens/home/lecture/LectureQuestionEditView';
import RewardView from '@/screens/mypage/RewardView';
import CouponView from '@/screens/mypage/CouponView';
import MyClassView from '@/screens/mypage/MyClassView';
import OrderDetailView from '@/screens/mypage/OrderDetailView';
import FakeLoginView from '@/screens/mypage/FakeLoginView';
import ReviewWriteView from '@/screens/mypage/ReviewWriteView';
import MyReviewView from '@/screens/mypage/MyReviewView';
import NotificationDetailView from '@/screens/mypage/NotificationDetailView';
import NoticeListView from '@/screens/mypage/NoticeListView';
import NoticeDetailView from '@/screens/mypage/NoticeDetailView';
import NotificationSettingView from '@/screens/mypage/NotificationSettingView';

const mypageStack = createStackNavigator();

class MyPageStackNavigator extends React.Component {
  render() {
    return (
      <mypageStack.Navigator initialRouteName="mypage">
        <mypageStack.Screen
          name="mypage"
          component={mypage}
          options={{
            headerShown: false,
          }}
        />
        <mypageStack.Screen
          name="alarmPage"
          component={alarmListPage}
          options={{
            headerShown: false,
          }}
        />
        <mypageStack.Screen
          name="settingPage"
          component={settingPage}
          options={{
            headerShown: false,
          }}
        />
        <mypageStack.Screen
          name="profile"
          component={Profile}
          options={{
            headerShown: false,
          }}
        />
        <mypageStack.Screen
          name="editProfile"
          component={editMyProfile}
          options={{
            headerShown: false,
          }}
        />
        <mypageStack.Screen
          name="editDirectorProfile"
          component={editDirectorProfile}
          options={{
            headerTitle: '내 디렉터 프로필',
          }}
        />
        <mypageStack.Screen
          name="categorySelect"
          component={CategorySelect}
          options={{
            headerTitle: '관심분야 선택',
          }}
        />
        <mypageStack.Screen
          name="bulletinList"
          component={MyPageBulletin}
          options={{
            headerShown: false,
          }}
        />
        <mypageStack.Screen
          name="questionList"
          component={QuestionListPage}
          options={{
            headerShown: false,
          }}
        />

        <mypageStack.Screen
          name="lectureQuestionDash"
          component={LectureQuestionDetailView}
          options={{
            headerShown: false,
          }}
        />

        <mypageStack.Screen
          name="lectureQuestionEdit"
          component={LectureQuestionEditView}
          options={{
            headerShown: false,
          }}
        />
        <mypageStack.Screen
          name="firstLogin"
          component={FirstLogin}
          options={{
            headerShown: false,
          }}
        />
        <mypageStack.Screen
          name="firstSubCategory"
          component={FirstSubCategory}
          options={{
            headerShown: false,
          }}
        />
        <mypageStack.Screen
          name="editCategory"
          component={editMyProfileCategorySelect}
          options={{
            headerShown: false,
          }}
        />
        <mypageStack.Screen
          name="bulletinDash"
          component={BulletinDetailView}
          options={{
            headerShown: false,
          }}
        />
        <mypageStack.Screen
          name="reward"
          component={RewardView}
          options={{
            headerShown: false,
          }}
        />
        <mypageStack.Screen
          name="coupon"
          component={CouponView}
          options={{
            headerShown: false,
          }}
        />
        <mypageStack.Screen
          name="myclass"
          component={MyClassView}
          options={{
            headerShown: false,
          }}
        />
        <mypageStack.Screen
          name="order"
          component={OrderDetailView}
          options={{
            headerShown: false,
          }}
        />

        <mypageStack.Screen
          name="fakelogin"
          component={FakeLoginView}
          options={{
            headerShown: false,
          }}
        />

        <mypageStack.Screen
          name="reviewWrite"
          component={ReviewWriteView}
          options={{
            headerShown: false,
          }}
        />

        <mypageStack.Screen
          name="myreview"
          component={MyReviewView}
          options={{
            headerShown: false,
          }}
        />

        <mypageStack.Screen
          name="notiDetail"
          component={NotificationDetailView}
          options={{
            headerShown: false,
          }}
        />

        <mypageStack.Screen
          name="noticeList"
          component={NoticeListView}
          options={{
            headerShown: false,
          }}
        />

        <mypageStack.Screen
          name="noticeDetail"
          component={NoticeDetailView}
          options={{
            headerShown: false,
          }}
        />

        <mypageStack.Screen
          name="notificationSetting"
          component={NotificationSettingView}
          options={{
            headerShown: false,
          }}
        />
      </mypageStack.Navigator>
    );
  }
}

export default MyPageStackNavigator;
