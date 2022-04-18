import * as React from 'react';
import {
  ScrollView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';
import {Button} from 'react-native-elements';
import Icon from 'react-native-vector-icons/Ionicons';
import MypageApiController from '../../../services/apis/MypageApiController';
import {STATUSBAR_HEIGHT} from '../../../utils/commonParam';
import UserDC from '../../../services/login/UserDC';
import SettingPageButton from './SettingPageButton';
import SettingPageSpliter from './SettingPageSpliter';

class settingPage extends React.Component {
  private async getProfileEditApi() {
    try {
      const res = await MypageApiController.getProfileEdit({});
      const profileData = res?.data;

      this.props.navigation.navigate('editProfile', {
        nickname: profileData.nickname,
        name: profileData.name,
        phone: profileData.phone,
        memberCategoryId: profileData.memberCategoryId,
        profileText: profileData.profileText,
        gender: profileData.sex,
        age: profileData.age,
        imageFileUrl: profileData.imageFileUrl,
        zoneId: profileData.zoneId,
      });
    } catch (e) {
      console.error(e);
    }
  }
  render() {
    return (
      <View
        style={{
          flex: 1,
          justifyContent: 'center',
          alignItems: 'center',
          backgroundColor: '#FFF',
        }}>
        <View
          style={{
            marginTop: STATUSBAR_HEIGHT,
            height: 60,
            width: '100%',
            flexDirection: 'row',
            justifyContent: 'flex-start',
            alignItems: 'center',
          }}>
          <Button
            icon={<Icon name="chevron-back" size={24} color="#1d1d1f" />}
            buttonStyle={{backgroundColor: 'white'}}
            onPress={() => {
              this.props.route.params.refreshParent();
              this.props.navigation.goBack();
            }}
          />
          <Text
            style={{
              // width: 93,
              // height: 20,
              fontFamily: 'NotoSansCJKkr-Bold',
              fontSize: 20,
              color: '#1d1d1f',
            }}>
            설정
          </Text>
        </View>
        <ScrollView style={this.styles.scrollview}>
          <SettingPageSpliter title={'내정보'} />

          <SettingPageButton
            title={'프로필 편집'}
            bottomBorder={true}
            onPress={async () => {
              this.getProfileEditApi();
            }}
          />
          <SettingPageButton
            title={'알림 설정'}
            bottomBorder={true}
            onPress={() =>
              this.props.navigation.navigate('notificationSetting')
            }
          />
          <SettingPageSpliter title={'친구초대'} />
          <SettingPageButton
            title={'코드로 친구 초대하기'}
            bottomBorder={false}
            onPress={() => {}}
          />
          <SettingPageSpliter title={'고객센터'} />
          <SettingPageButton
            title={'공지사항'}
            bottomBorder={false}
            onPress={() => {}}
          />
          <SettingPageButton
            title={'디렉터 공지사항'}
            bottomBorder={false}
            onPress={() => {}}
          />
          <SettingPageButton
            title={'자주묻는질문'}
            bottomBorder={false}
            onPress={() => {}}
          />
          <SettingPageButton
            title={'카카오톡 문의하기'}
            bottomBorder={false}
            onPress={() => {}}
          />
          <SettingPageButton
            title={'전화로 문의하기'}
            bottomBorder={false}
            onPress={() => {}}
          />
          <SettingPageButton
            title={'이용약관'}
            bottomBorder={false}
            onPress={() => {}}
          />
          <SettingPageSpliter title={'계정'} />
          <TouchableOpacity
            style={[
              {
                // backgroundColor: 'brown',
                flexDirection: 'row',
                alignItems: 'center',
                height: 50,
                marginLeft: 20,
                paddingRight: 15,
              },
            ]}
            onPress={() => {
              UserDC.signOutWithKakao();
              this.props.navigation.navigate('mypage');
            }}>
            <Icon name="ios-log-out-outline" size={18} color="#ff4f4f" />
            <Text
              style={{
                color: '#ff4f4f',
                fontSize: 16,
                lineHeight: 18,
                fontFamily: 'NotoSansCJKkr-Regular',
                paddingTop: 5,
                marginLeft: 11,
              }}>
              로그아웃
            </Text>
          </TouchableOpacity>
          <View style={{height: 50}} />
        </ScrollView>
      </View>
    );
  }

  private styles = StyleSheet.create({
    scrollview: {
      width: '100%',
      padding: 10,
    },
    header: {
      fontSize: 25,
    },
    button: {
      margin: 5,
    },
  });
}

export default settingPage;
