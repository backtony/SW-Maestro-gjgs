import {navigate} from '@/navigation/RootNavigation';
import ApplyApiController from '@/services/apis/ApplyApiController';
import MypageApiController from '@/services/apis/MypageApiController';
import PaymentController from '@/services/apis/PaymentController';
import {parseDate} from '@/utils/commonFunctions';
import {strJsonType} from '@/utils/Types';
import Payment from 'iamport-react-native/lib/typescript/components/Payment';
import React from 'react';
import {
  Alert,
  Image,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';
import DropDownPicker from 'react-native-dropdown-picker';
import {MyPageClassInfo} from './MyClassView';

interface MemberPaymentInfo {
  nickname: string;
  orderState: string;
}

interface MyClassBlockProps {
  classInfo: MyPageClassInfo;
  navigation: any;
}

interface MyClassBlockStates {
  dropDown: boolean;
  members: MemberPaymentInfo[];
}

const paymentStatus: strJsonType = {
  WAIT: '결제 대기중',
  COMPLETE: '결제 완료',
  CANCEL: '결제 취소',
};

export default class MyClassBlock extends React.Component<
  MyClassBlockProps,
  MyClassBlockStates
> {
  constructor(props: any) {
    super(props);
    this.state = {dropDown: false, members: []};
    if (props.classInfo.teamId > 0) {
      this.getTeamPaymentStatus(
        props.classInfo.scheduleId,
        props.classInfo.teamId,
      );
    }
  }

  private async getTeamPaymentStatus(scheduleId: number, teamId: number) {
    try {
      const res = await MypageApiController.getTeamPaymentStatus(
        scheduleId,
        teamId,
      );
      console.log('members: ', res?.data.memberStatusList);
      this.setState({members: res?.data.memberStatusList});
    } catch (e) {}
  }

  private async deleteSchedule(scheduleId: number, teamId: number) {
    try {
      await ApplyApiController.deleteSchedule(scheduleId, teamId);
      Alert.alert('신청취소가 완료되었습니다.');
    } catch (e) {}
  }

  private async deletePayment(payType: string, orderId: number) {
    try {
      console.log('payType: ', payType, ', orderId: ', orderId);
      await PaymentController.deletePayment(payType, orderId);
    } catch (e) {}
  }

  render() {
    return (
      <View style={this.styles.container}>
        <TouchableOpacity
          onPress={() => {
            if (this.props.classInfo.orderStatus === 'COMPLETE') {
              this.props.navigation.navigate('order', {
                orderId: this.props.classInfo.orderId,
              });
            }
          }}
          style={{flexDirection: 'row'}}>
          <Image
            style={{width: 120, height: 96, borderRadius: 7.9}}
            source={{
              uri: this.props.classInfo.lectureThumbnailUrl,
            }}
          />
          <View style={{marginLeft: 16}}>
            <Text style={this.styles.lectureTitle}>
              {this.props.classInfo.lectureTitle}
            </Text>
            {/* 2021.09.13 13:00 */}
            <Text style={this.styles.date1}>
              {parseDate(this.props.classInfo.startDateTime)}
            </Text>
            <Text style={this.styles.date2}>{` ~ ${parseDate(
              this.props.classInfo.endDateTime,
            )}`}</Text>
          </View>
        </TouchableOpacity>
        <DropDownPicker
          open={this.state.dropDown}
          value={0}
          items={[
            {
              label: '팀원 결제현황',
              value: 0,
            },
            ...this.state.members.map(
              (member: MemberPaymentInfo, index: number) => {
                return {
                  label: paymentStatus[member.orderState],
                  value: index + 1,
                  icon: () => <Text>{member.nickname}</Text>,
                };
              },
            ),
          ]}
          setOpen={() => this.setState({dropDown: !this.state.dropDown})}
          setValue={callback => {
            // this.setState(
            //   state => ({
            //     selectedCoupon: callback(state.selectedCoupon),
            //   }),
            //   () => {
            //     this.calcResult();
            //   },
            // );
          }}
          setItems={() => {}}
          showTickIcon={false}
          style={this.styles.dropDown}
          listItemContainerStyle={this.styles.listItemContainer}
          listItemLabelStyle={{color: '#4a4a4c', fontSize: 12}}
          dropDownContainerStyle={this.styles.dropDownContainer}
          arrowIconStyle={this.styles.arrowIcon}
          textStyle={{fontSize: 14}}
        />

        <View style={this.styles.bottom}>
          {this.props.classInfo.orderStatus === 'WAIT' &&
            this.props.classInfo.currentLectureStatus === 'BEFORE' && (
              <TouchableOpacity
                style={this.styles.button1}
                onPress={() => {
                  navigate('payment', {
                    lectureId: this.props.classInfo.lectureId,
                    scheduleId: this.props.classInfo.scheduleId,
                    teamId: this.props.classInfo.teamId,
                    payType: 'TEAM',
                  });
                }}>
                <Text style={this.styles.text1}>결제하기</Text>
              </TouchableOpacity>
            )}
          {this.props.classInfo.orderStatus === 'COMPLETE' &&
            this.props.classInfo.currentLectureStatus === 'BEFORE' && (
              <TouchableOpacity
                style={this.styles.button3}
                onPress={() => {
                  this.deletePayment(
                    this.props.classInfo.teamId ? 'TEAM' : 'PERSONAL',
                    this.props.classInfo.orderId,
                  );
                }}>
                <Text style={this.styles.text1}>결제 취소</Text>
              </TouchableOpacity>
            )}
          {this.props.classInfo.orderStatus === 'CANCEL' && (
            <TouchableOpacity style={this.styles.button2} onPress={() => {}}>
              <Text style={this.styles.text2}>결제 취소됨</Text>
            </TouchableOpacity>
          )}
          {this.props.classInfo.leader &&
            this.props.classInfo.currentLectureStatus === 'BEFORE' && (
              <TouchableOpacity
                style={this.styles.button2}
                onPress={() => {
                  this.deleteSchedule(
                    this.props.classInfo.scheduleId,
                    this.props.classInfo.teamId,
                  );
                }}>
                <Text style={this.styles.text2}>신청 취소</Text>
              </TouchableOpacity>
            )}
          {this.props.classInfo.currentLectureStatus === 'DONE' &&
            !this.props.classInfo.reviewed && (
              <TouchableOpacity
                style={this.styles.button1}
                onPress={() => {
                  this.props.navigation.navigate('reviewWrite', {
                    classInfo: this.props.classInfo,
                  });
                }}>
                <Text style={this.styles.text1}>리뷰남기기</Text>
              </TouchableOpacity>
            )}
          {this.props.classInfo.currentLectureStatus === 'DONE' &&
            this.props.classInfo.reviewed && (
              <TouchableOpacity style={this.styles.button2} onPress={() => {}}>
                <Text style={this.styles.text2}>리뷰 작성완료</Text>
              </TouchableOpacity>
            )}
          {/* <TouchableOpacity>
              <Text>결제하기</Text>
            </TouchableOpacity> */}
          {/* <TouchableOpacity>
              <Text>리뷰달기</Text>
            </TouchableOpacity> */}
        </View>
      </View>
    );
  }
  private styles = StyleSheet.create({
    container: {
      padding: 20,
      backgroundColor: '#fafafb',
      width: 350,
      height: 236,
      borderRadius: 6,
      marginBottom: 10,
    },
    lectureTitle: {
      width: 164,
      height: 36,
      fontFamily: 'NotoSansCJKkr-Regular',
      fontSize: 14,
      fontWeight: '500',
      fontStyle: 'normal',
      lineHeight: 18,
      letterSpacing: 0,
      color: '#070707',
    },
    date1: {
      width: 122,
      fontFamily: 'NotoSansCJKkr-Regular',
      fontSize: 12,
      fontWeight: 'normal',
      fontStyle: 'normal',
      lineHeight: 16,
      letterSpacing: 0,
      color: '#8e8e8f',
      marginTop: 4,
    },
    date2: {
      width: 122,
      fontFamily: 'NotoSansCJKkr-Regular',
      fontSize: 12,
      fontWeight: 'normal',
      fontStyle: 'normal',
      lineHeight: 16,
      letterSpacing: 0,
      color: '#8e8e8f',
    },
    dropDown: {
      backgroundColor: '#f5f5f7',
      borderWidth: 0,
      height: 34,
      borderRadius: 6,
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',
      marginTop: 10,
    },
    listItemContainer: {
      height: 30,
      backgroundColor: '#fafafb',
    },
    dropDownContainer: {
      borderWidth: 0,
    },
    arrowIcon: {
      width: 18,
      height: 18,
    },
    bottom: {
      marginTop: 10,
      flexDirection: 'row',
      justifyContent: 'space-between',
    },
    button1: {
      flex: 1,
      //width: 140,
      height: 40,
      borderRadius: 6,
      backgroundColor: '#4f6cff',
      justifyContent: 'center',
      alignItems: 'center',
    },
    text1: {
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 14,
      fontWeight: 'bold',
      fontStyle: 'normal',
      lineHeight: 18,
      letterSpacing: 0,
      textAlign: 'center',
      color: '#ffffff',
    },
    button2: {
      flex: 1,
      //width: 140,
      height: 40,
      borderRadius: 6,
      backgroundColor: '#f5f5f7',
      justifyContent: 'center',
      alignItems: 'center',
    },
    text2: {
      fontFamily: 'NotoSansCJKkr-Regular',
      fontSize: 14,
      fontWeight: '500',
      fontStyle: 'normal',
      lineHeight: 18,
      letterSpacing: 0,
      textAlign: 'center',
      color: '#4a4a4c',
    },
    button3: {
      flex: 1,
      //width: 140,
      height: 40,
      borderRadius: 6,
      backgroundColor: '#ff4f4f',
      justifyContent: 'center',
      alignItems: 'center',
    },
  });
}
