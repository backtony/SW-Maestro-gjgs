import CouponController from '@/services/apis/CouponController';
import UserDC from '@/services/login/UserDC';
import React from 'react';
import {StyleSheet} from 'react-native';
import {Alert, TouchableOpacity} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';

interface LectureDetailCouponButtonProps {
  lectureId: number;
}

export default class LectureDetailCouponButton extends React.Component<
  LectureDetailCouponButtonProps,
  {}
> {
  private async postCoupon(lectureId: number) {
    try {
      await CouponController.postCoupon(lectureId);
      Alert.alert('쿠폰 발급이 완료되었습니다.');
    } catch (e) {}
  }
  render() {
    return (
      <TouchableOpacity
        style={this.styles.container}
        onPress={() => {
          if (UserDC.isLogout()) {
            Alert.alert('로그인 해주세요.');
            return;
          }
          this.postCoupon(this.props.lectureId);
        }}>
        <Icon name="pricetag" size={20} color="#FFF" />
      </TouchableOpacity>
    );
  }

  private styles = StyleSheet.create({
    container: {
      width: 44,
      height: 44,
      borderRadius: 44,
      backgroundColor: '#4f6cff',
      position: 'absolute',
      right: 20,
      bottom: 20,
      alignItems: 'center',
      justifyContent: 'center',

      shadowColor: '#4c4f6cff',
      shadowOffset: {
        width: 0,
        height: 4,
      },
      shadowOpacity: 0.29,
      shadowRadius: 4.65,

      elevation: 7,
    },
  });
}
