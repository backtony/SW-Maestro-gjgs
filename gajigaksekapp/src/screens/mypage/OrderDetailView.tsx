import {navigate} from '@/navigation/RootNavigation';
import GoogleMapApiController from '@/services/apis/GoogleMapApiController';
import MypageApiController from '@/services/apis/MypageApiController';
import FirebaseAddChatRoom from '@/services/firebaseAuth/FirebaseAddChatRoom';
import User from '@/services/login/User';
import UserDC from '@/services/login/UserDC';
import {parseDate} from '@/utils/commonFunctions';
import {STATUSBAR_HEIGHT} from '@/utils/commonParam';
import Clipboard from '@react-native-clipboard/clipboard';
import React from 'react';
import {Image, ScrollView, Text, TouchableOpacity, View} from 'react-native';
import {Button} from 'react-native-elements';
import MapView, {Marker} from 'react-native-maps';
import Icon from 'react-native-vector-icons/Ionicons';
import SettingPageButton from './settingpage/SettingPageButton';

export interface OrderInfo {
  endDateTime: string;
  lectureAddress: string;
  lectureId: number;
  lectureThumbnailUrl: string;
  lectureTitle: string;
  paymentPrice: number;
  scheduleId: number;
  startDateTime: string;
}

interface MyClassDetailViewStates {
  orderInfo: OrderInfo;
  region: any;
}

export default class OrderDetailView extends React.Component<
  {},
  MyClassDetailViewStates
> {
  constructor(props: any) {
    super(props);
    this.state = {
      orderInfo: {
        endDateTime: '2021-09-28T04:53:32.226Z',
        lectureAddress: '우리집',
        lectureId: 0,
        lectureThumbnailUrl:
          'https://images.unsplash.com/photo-1632755529629-c94547b07922?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=765&q=80',
        lectureTitle: 'title',
        paymentPrice: 0,
        scheduleId: 0,
        startDateTime: '2021-09-28T04:53:32.226Z',
      },
      region: null,
    };
    this.getRegion('서울 시청');
  }

  private async getRegion(address: string) {
    const location = await GoogleMapApiController.getRegion(address);
    console.log('location: ', location);
    this.setState({
      region: {
        latitude: location.lat,
        longitude: location.lng,
        latitudeDelta: 0.005,
        longitudeDelta: 0.005,
      },
    });
    return location;
  }

  componentDidMount() {
    console.log(this.props.route.params.orderId);
    this.getOrder(this.props.route.params.orderId);
  }

  private async getOrder(orderId: number) {
    try {
      const res = await MypageApiController.getOrder(orderId);
      this.setState({orderInfo: res?.data});
    } catch (e) {}
  }

  private async copyToClipboard(text: string) {
    Clipboard.setString(text);
    const rr = await Clipboard.getString();
    console.log('rr: ', rr);
    alert('주소가 복사되었습니다.');
  }

  render() {
    return (
      <View style={{flex: 1, backgroundColor: '#FFF'}}>
        <View
          style={{
            marginTop: STATUSBAR_HEIGHT,
            height: 60,
            width: '100%',
            flexDirection: 'row',
            // justifyContent: 'space-between',
            alignItems: 'center',
            paddingHorizontal: 15,
            position: 'absolute',
            zIndex: 100,
          }}>
          <Button
            icon={<Icon name="chevron-back" size={24} color="#FFF" />}
            buttonStyle={{backgroundColor: 'transparent'}}
            onPress={() => {
              this.props.navigation.goBack();
            }}
          />
          <Text
            style={{
              fontFamily: 'NotoSansCJKkr-Bold',
              fontSize: 20,
              fontWeight: 'bold',
              fontStyle: 'normal',
              letterSpacing: 0,
              color: '#ffffff',
            }}>
            예약 정보
          </Text>
        </View>
        <ScrollView
          showsVerticalScrollIndicator={false}
          showsHorizontalScrollIndicator={false}>
          <Image
            style={{
              height: 350,
            }}
            source={{
              uri: this.state.orderInfo.lectureThumbnailUrl,
            }}
          />
          <Text
            style={{
              fontSize: 20,
              lineHeight: 24,
              fontFamily: 'NotoSansCJKkr-Bold',
              marginTop: 20,
              marginBottom: 10,
              color: '#070707',
              marginHorizontal: 20,
            }}>
            {this.state.orderInfo.lectureTitle}
          </Text>
          <View
            style={{
              flexDirection: 'row',
              backgroundColor: '#fafafb',
              marginHorizontal: 20,
              marginTop: 30,
              padding: 20,
              justifyContent: 'space-between',
              alignItems: 'center',
            }}>
            <Text
              style={{
                fontFamily: 'NotoSansCJKkr-Regular',
                fontSize: 12,
                fontWeight: '500',
                fontStyle: 'normal',
                lineHeight: 16,
                letterSpacing: 0,
                color: '#8e8e8f',
              }}>
              스케줄
            </Text>
            <View>
              <Text
                style={{
                  fontFamily: 'NotoSansCJKkr-Bold',
                  fontSize: 16,
                  fontWeight: 'bold',
                  fontStyle: 'normal',
                  lineHeight: 20,
                  letterSpacing: 0,
                  textAlign: 'right',
                  color: '#1d1d1f',
                }}>
                {parseDate(this.state.orderInfo.startDateTime)}
              </Text>
              <Text
                style={{
                  fontFamily: 'NotoSansCJKkr-Bold',
                  fontSize: 16,
                  fontWeight: 'bold',
                  fontStyle: 'normal',
                  lineHeight: 20,
                  letterSpacing: 0,
                  textAlign: 'right',
                  color: '#1d1d1f',
                }}>
                {`~ ${parseDate(this.state.orderInfo.endDateTime)}`}
              </Text>
            </View>
          </View>
          <View style={{flexDirection: 'row'}}>
            <View
              style={{
                backgroundColor: '#fafafb',
                padding: 20,
                margin: 20,
                marginRight: 10,
                flex: 1,
                alignItems: 'center',
              }}>
              <Text
                style={{
                  fontFamily: 'NotoSansCJKkr-Regular',
                  fontSize: 12,
                  fontWeight: '500',
                  fontStyle: 'normal',
                  lineHeight: 16,
                  letterSpacing: 0,
                  color: '#8e8e8f',
                }}>
                예약번호
              </Text>
              <Text
                style={{
                  fontFamily: 'NotoSansCJKkr-Bold',
                  fontSize: 16,
                  fontWeight: 'bold',
                  fontStyle: 'normal',
                  lineHeight: 20,
                  letterSpacing: 0,
                  textAlign: 'center',
                  color: '#1d1d1f',
                }}>
                {this.props.route.params.orderId}
              </Text>
            </View>
            <View
              style={{
                backgroundColor: '#fafafb',
                padding: 20,
                margin: 20,
                marginLeft: 10,
                flex: 1,
                alignItems: 'center',
              }}>
              <Text
                style={{
                  fontFamily: 'NotoSansCJKkr-Regular',
                  fontSize: 12,
                  fontWeight: '500',
                  fontStyle: 'normal',
                  lineHeight: 16,
                  letterSpacing: 0,
                  color: '#8e8e8f',
                }}>
                예약자
              </Text>
              <Text
                style={{
                  fontFamily: 'NotoSansCJKkr-Bold',
                  fontSize: 16,
                  fontWeight: 'bold',
                  fontStyle: 'normal',
                  lineHeight: 20,
                  letterSpacing: 0,
                  textAlign: 'center',
                  color: '#1d1d1f',
                }}>
                {UserDC.getUser().nickname}
              </Text>
            </View>
          </View>

          <View
            style={{
              flexDirection: 'row',
              backgroundColor: '#fafafb',
              marginHorizontal: 20,
              padding: 20,
              justifyContent: 'space-between',
              alignItems: 'center',
            }}>
            <Text
              style={{
                fontFamily: 'NotoSansCJKkr-Regular',
                fontSize: 12,
                fontWeight: '500',
                fontStyle: 'normal',
                lineHeight: 16,
                letterSpacing: 0,
                color: '#8e8e8f',
              }}>
              결제금액
            </Text>
            <View>
              <Text
                style={{
                  fontFamily: 'NotoSansCJKkr-Bold',
                  fontSize: 16,
                  fontWeight: 'bold',
                  fontStyle: 'normal',
                  lineHeight: 20,
                  letterSpacing: 0,
                  textAlign: 'right',
                  color: '#1d1d1f',
                }}>
                {`${this.state.orderInfo.paymentPrice}원`}
              </Text>
            </View>
          </View>
          <View
            style={{
              flexDirection: 'row',
              marginTop: 40,
              alignItems: 'center',
              marginBottom: 20,
              marginHorizontal: 20,
            }}>
            <Text
              style={{
                fontSize: 16,
                lineHeight: 18,
                fontFamily: 'NotoSansCJKkr-Bold',
                marginTop: 7,
                marginLeft: 6,
              }}>
              클래스 진행 장소
            </Text>
          </View>
          <View style={{marginHorizontal: 20}}>
            {this.state.region && (
              <MapView
                style={{width: '100%', height: 200, borderRadius: 10}}
                initialRegion={this.state.region}>
                <Marker coordinate={this.state.region} />
              </MapView>
            )}
            <TouchableOpacity
              style={{flexDirection: 'row', alignItems: 'center', padding: 10}}
              onPress={() => {
                this.copyToClipboard(this.state.orderInfo.lectureAddress);
              }}>
              <Icon name="copy-outline" size={15} color="#d2d2d2" />
              <Text
                style={{
                  fontSize: 14,
                  lineHeight: 16,
                  fontFamily: 'NotoSansCJKkr-Regular',
                  marginLeft: 6,
                }}>
                {this.state.orderInfo.lectureAddress}
              </Text>
            </TouchableOpacity>
          </View>
          <View style={{marginVertical: 40}}>
            <SettingPageButton
              title={'영수증'}
              bottomBorder
              onPress={async () => {}}
            />
            <SettingPageButton
              title={'취소 및 환불 정책'}
              bottomBorder={false}
              onPress={async () => {}}
            />
          </View>
        </ScrollView>
        <View style={{marginHorizontal: 20}}>
          <TouchableOpacity
            style={{
              height: 44,
              backgroundColor: '#4f6cff',
              borderRadius: 6,
              marginVertical: 20,
              justifyContent: 'center',
              alignItems: 'center',
            }}
            onPress={async () => {
              if (UserDC.isLogout()) {
                alert('로그인 해주세요.');
                return;
              }
              const user: User = UserDC.getUser();
              await FirebaseAddChatRoom.addNewLectureChatRoom(
                '디렉터와의 채팅방',
                JSON.stringify(this.state.orderInfo.lectureId),
                JSON.stringify(user.memberId),
                user.nickname,
                this.state.orderInfo.lectureTitle,
                user.imageFileUrl,
                () =>
                  navigate('chatroom', {
                    thread: {
                      teamId:
                        JSON.stringify(this.state.orderInfo.lectureId) +
                        '|' +
                        JSON.stringify(UserDC.getUser().memberId),
                      teamName: '디렉터와',
                    },
                  }),
              );
            }}>
            <Text
              style={{
                fontFamily: 'NotoSansCJKkr-Bold',
                fontSize: 16,
                fontWeight: 'bold',
                fontStyle: 'normal',
                lineHeight: 20,
                letterSpacing: 0,
                textAlign: 'center',
                color: '#ffffff',
              }}>
              디렉터와 채팅하기
            </Text>
          </TouchableOpacity>
        </View>
      </View>
    );
  }
}
