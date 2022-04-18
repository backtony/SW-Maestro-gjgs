import React, {Component} from 'react';
import {
  StyleSheet,
  Text,
  View,
  Alert,
  ScrollView,
  TextInput,
  Image,
} from 'react-native';
import {impUid, STATUSBAR_HEIGHT} from '../../utils/commonParam';
import {Button} from 'react-native-elements';
import Icon from 'react-native-vector-icons/Ionicons';
import {TouchableOpacity} from 'react-native-gesture-handler';
import LectureApiController from '@/services/apis/LectureApiController';
import DropDownPicker from 'react-native-dropdown-picker';
import CouponController from '@/services/apis/CouponController';
import PaymentController from '@/services/apis/PaymentController';
import {navigate} from '@/navigation/RootNavigation';
import IamPortPayment2 from '@/services/payment/IamPortPayment2';
import {SafeAreaView} from 'react-native-safe-area-context';
import UserDC from '@/services/login/UserDC';
import User from '@/services/login/User';
import {parseDate} from '@/utils/commonFunctions';
import MypageApiController from '@/services/apis/MypageApiController';

export interface CouponInfo {
  couponTitle: string;
  discountPrice: number;
  memberCouponId: number;
}

interface PaymentViewStates {
  payType: string;
  dropDown: boolean;
  selectedCoupon: CouponInfo;
  coupons: CouponInfo[];
  totalPrice: number;

  endTime: string;
  haveReward: number;
  lectureThumbnailUrl: string;
  lectureTitle: string;
  orderId: number;
  price: number;
  scheduleId: number;
  startTime: string;
  teamId: number;
  teamName: string;

  rewardAmount: number;

  iamportOn: boolean;

  user: User;
}

export default class PaymentView extends Component<{}, PaymentViewStates> {
  constructor(props: any) {
    super(props);
    this.state = {
      payType: 'PERSONAL',
      dropDown: false,
      selectedCoupon: {
        couponTitle: 'null',
        discountPrice: 0,
        memberCouponId: 0,
      },
      coupons: [],
      totalPrice: 230000,

      endTime: '2021-09-25T14:30:00',
      haveReward: 0,
      lectureThumbnailUrl:
        'https://images.unsplash.com/photo-1529156069898-49953e39b3ac?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=2089&q=80',
      lectureTitle: '16년차 베이커리 장인의 케이크 클래스',
      orderId: 0,
      price: 230000,
      scheduleId: 0,
      startTime: '2021-09-25T12:30:00',
      teamId: 0,
      teamName: 'asdf',
      rewardAmount: 0,
      iamportOn: false,
      user: UserDC.getUser(),
    };
  }

  componentDidMount() {
    console.log('props:', this.props.route.params);
    this.setState({payType: this.props.route.params.payType});

    if (this.props.route.params.payType === 'PERSONAL') {
      this.getLectureDash(this.props.route.params.lectureId);
      this.getReward();
      this.setState({
        price: this.props.route.params.price,
        totalPrice: this.props.route.params.price,
        scheduleId: this.props.route.params.scheduleId,
      });
    }

    this.getCoupon(this.props.route.params.lectureId);
    this.getPaymentInfo(
      this.props.route.params.payType,
      this.props.route.params.scheduleId,
    );
  }

  private async getReward() {
    try {
      const res = await MypageApiController.getMypage({});
      this.setState({haveReward: res?.data.totalReward});
    } catch (e) {}
  }

  private async getLectureDash(lectureId: number) {
    try {
      const res = await LectureApiController.getLectureDash({lectureId});
      let lecture = res.data;
      lecture.imageUrl = lecture.thumbnailImageUrl;
      lecture.myFavorite = lecture.myFavoriteLecture;
      lecture.title = lecture.lectureTitle;

      this.setState({
        lectureTitle: lecture.title,
        lectureThumbnailUrl: lecture.imageUrl,
      });
    } catch (e) {
      console.error(e);
    }
  }

  private async getCoupon(lectureId: number) {
    try {
      const res = await CouponController.getCoupon(lectureId);
      this.setState({coupons: res.data.enableCouponList});
    } catch (e) {}
  }

  private calcResult() {
    let couponPrice = 0;

    if (this.state.selectedCoupon.couponTitle !== 'null') {
      console.log('coupon: ', this.state.selectedCoupon);
      couponPrice = this.state.selectedCoupon.discountPrice;
    }

    this.setState({
      totalPrice: this.state.price - couponPrice - this.state.rewardAmount,
    });
  }

  private async getPaymentInfo(payType: string, scheduleId: number) {
    if (payType === 'PERSONAL') {
      return;
    }
    try {
      const res = await PaymentController.getPaymentInfo(payType, scheduleId);
      this.setState({...res.data, totalPrice: res.data.price});
    } catch (e) {
      this.props.navigation.goBack();
      Alert.alert(e.response.data.message);
    }
  }

  private filterNumber = (text: string) => {
    return +text.replace(/[^0-9.]/g, '').replace(/(\..*?)\..*/g, '$1');
  };

  private async postPayment(payType: string, scheduleId: number) {
    let params: any = {
      lectureId: this.props.route.params.lectureId,
      //   memberCouponId: this.state.selectedCoupon.memberCouponId,
      originalPrice: this.state.price,
      rewardAmount: this.state.rewardAmount,
      totalDiscountPrice: this.state.price - this.state.totalPrice,
      totalPrice: this.state.totalPrice,
    };

    if (this.state.selectedCoupon.memberCouponId > 0) {
      params = {
        ...params,
        memberCouponId: this.state.selectedCoupon.memberCouponId,
      };
    }

    try {
      const res = await PaymentController.postPayment(
        payType,
        scheduleId,
        params,
      );
      this.setState({orderId: res.data.orderId}, () =>
        this.setState({
          iamportOn: true,
        }),
      );
      // this.setState({iamportOn: true});
    } catch (e) {}
  }

  private async patchPayment(
    payType: string,
    scheduleId: number,
    iamportUid: string,
    orderId: number,
  ) {
    const params = {
      iamportUid,
      orderId,
    };
    console.log(
      'patchPayment: ',
      'payType: ',
      payType,
      ', scheduleId: ',
      scheduleId,
      ', params: ',
      params,
    );
    try {
      const res = await PaymentController.patchPayment(
        payType,
        scheduleId,
        params,
      );

      if (res.data.description === '결제 금액이 맞지 않아 취소되었습니다.') {
        Alert.alert('결제 금액이 맞지 않아 취소되었습니다.');
        return;
      }

      Alert.alert('결제가 완료되었습니다.');
      navigate('home', {});
      console.log('patchPayment res: ', res);
    } catch (e) {}
  }

  private printResponse(response: any) {
    console.log(response);
  }

  render() {
    return (
      <SafeAreaView style={styles.container}>
        {!this.state.iamportOn && (
          <View style={{flex: 1}}>
            <View
              style={{
                // marginTop: STATUSBAR_HEIGHT,
                height: 60,
                width: '100%',
                flexDirection: 'row',
                justifyContent: 'flex-start',
                alignItems: 'center',
              }}>
              <Button
                icon={<Icon name="chevron-back" size={24} color="#1d1d1f" />}
                buttonStyle={{backgroundColor: 'white'}}
                onPress={() => this.props.navigation.goBack()}
              />
              <Text
                style={{
                  // width: 93,
                  // height: 20,
                  fontFamily: 'NotoSansCJKkr-Bold',
                  fontSize: 20,
                  color: '#1d1d1f',
                }}>
                결제
              </Text>
            </View>
            <ScrollView
              showsVerticalScrollIndicator={false}
              showsHorizontalScrollIndicator={false}>
              <View
                style={{
                  marginHorizontal: 20,
                  marginTop: 10,
                  flexDirection: 'row',
                }}>
                <Image
                  style={{width: 120, height: 90, borderRadius: 7.9}}
                  source={{
                    uri: this.state.lectureThumbnailUrl,
                  }}
                />
                <View style={{marginLeft: 16}}>
                  <Text
                    style={{
                      width: 184,
                      height: 40,
                      fontFamily: 'NotoSansCJKkr-Regular',
                      fontSize: 16,
                      fontWeight: '500',
                      fontStyle: 'normal',
                      lineHeight: 20,
                      letterSpacing: 0,
                      color: '#070707',
                    }}>
                    {this.state.lectureTitle}
                  </Text>
                  <Text
                    style={{
                      width: 116,
                      //   height: 36,
                      fontFamily: 'NotoSansCJKkr-Regular',
                      fontSize: 14,
                      fontWeight: 'normal',
                      fontStyle: 'normal',
                      //   lineHeight: 18,
                      letterSpacing: 0,
                      color: '#8e8e8f',
                    }}>
                    {this.props.route.params.payType === 'TEAM'
                      ? parseDate(this.state.startTime)
                      : `${this.props.route.params.selectedDate.substr(
                          0,
                          4,
                        )}.${this.props.route.params.selectedDate.substr(
                          5,
                          2,
                        )}.${this.props.route.params.selectedDate.substr(
                          8,
                          2,
                        )} ${this.props.route.params.selectedTime.substr(
                          0,
                          5,
                        )}`}
                  </Text>
                  <Text
                    style={{
                      width: 116,
                      //   height: 36,
                      fontFamily: 'NotoSansCJKkr-Regular',
                      fontSize: 14,
                      fontWeight: 'normal',
                      fontStyle: 'normal',
                      //   lineHeight: 18,
                      letterSpacing: 0,
                      color: '#8e8e8f',
                    }}>
                    {this.props.route.params.payType === 'TEAM'
                      ? `~${parseDate(this.state.endTime)}`
                      : `~${this.props.route.params.selectedDate.substr(
                          0,
                          4,
                        )}.${this.props.route.params.selectedDate.substr(
                          5,
                          2,
                        )}.${this.props.route.params.selectedDate.substr(
                          8,
                          2,
                        )} ${this.props.route.params.selectedTime.substr(
                          8,
                          5,
                        )}`}
                  </Text>
                </View>
              </View>
              {this.state.payType === 'TEAM' && (
                <View>
                  <Text
                    style={{
                      fontSize: 16,
                      lineHeight: 18,
                      fontFamily: 'NotoSansCJKkr-Bold',
                      marginLeft: 20,
                      marginTop: 20,
                      marginBottom: 10,
                    }}>
                    팀명
                  </Text>
                  <View
                    style={{
                      marginHorizontal: 20,
                      backgroundColor: 'rgba(245, 245, 247, 0.6)',
                      height: 44,
                      borderRadius: 6,
                      justifyContent: 'center',
                    }}>
                    <Text
                      style={{
                        height: 24,
                        width: '70%',
                        paddingLeft: 13,
                        fontSize: 15,
                      }}>
                      {this.state.teamName}
                    </Text>
                    {/* <Text style={{marginLeft: 13}}>{this.state.name}</Text> */}
                  </View>
                </View>
              )}
              <View style={{marginHorizontal: 20}}>
                <Text
                  style={{
                    fontSize: 16,
                    lineHeight: 18,
                    fontFamily: 'NotoSansCJKkr-Bold',

                    marginTop: 20,
                    marginBottom: 10,
                  }}>
                  쿠폰
                </Text>
                <DropDownPicker
                  open={this.state.dropDown}
                  value={this.state.selectedCoupon.memberCouponId}
                  items={[
                    {
                      label: '쿠폰 선택',
                      value: 0,
                    },
                    ...this.state.coupons.map((coupon: CouponInfo) => ({
                      label: coupon.couponTitle,
                      value: coupon.memberCouponId,
                    })),
                  ]}
                  setOpen={() => {
                    this.setState({dropDown: !this.state.dropDown});
                  }}
                  setValue={callback => {
                    this.setState(
                      state => ({
                        selectedCoupon: this.state.coupons.filter(
                          (coupon: CouponInfo) =>
                            coupon.memberCouponId ===
                            callback(state.selectedCoupon),
                        )[0],
                      }),
                      () => {
                        this.calcResult();
                      },
                    );
                  }}
                  setItems={() => {}}
                  dropDownDirection="TOP"
                  showTickIcon={false}
                  style={this.styles.dropDown}
                  listItemContainerStyle={this.styles.listItemContainer}
                  listItemLabelStyle={{color: '#4a4a4c', fontSize: 12}}
                  dropDownContainerStyle={this.styles.dropDownContainer}
                  arrowIconStyle={this.styles.arrowIcon}
                  textStyle={{fontSize: 14}}
                />
              </View>
              <View style={{marginHorizontal: 20}}>
                <View
                  style={{
                    flexDirection: 'row',
                    justifyContent: 'space-between',
                    marginTop: 20,
                    marginBottom: 10,
                  }}>
                  <Text
                    style={{
                      fontSize: 16,
                      lineHeight: 18,
                      fontFamily: 'NotoSansCJKkr-Bold',
                    }}>
                    리워드
                  </Text>
                  <Text
                    style={{
                      fontFamily: 'NotoSansCJKkr-Regular',
                      fontSize: 12,
                      fontWeight: '500',
                      fontStyle: 'normal',
                      lineHeight: 16,
                      letterSpacing: 0,
                      textAlign: 'right',
                      color: '#4f6cff',
                    }}>{`보유 ${this.state.haveReward}원`}</Text>
                </View>
                <View
                  style={{
                    backgroundColor: 'rgba(245, 245, 247, 0.6)',
                    height: 44,
                    borderRadius: 6,
                    flexDirection: 'row',
                    justifyContent: 'flex-end',
                    alignItems: 'center',
                  }}>
                  <TextInput
                    style={{
                      height: 44,
                      // width: '70%',
                      paddingLeft: 13,
                      marginRight: 5,
                    }}
                    onChangeText={text => {
                      const reward: number = this.filterNumber(text);
                      if (reward > this.state.haveReward) {
                        Alert.alert('소지 리워드를 초과했습니다.');
                        return;
                      }
                      this.setState({rewardAmount: reward}, () =>
                        this.calcResult(),
                      );
                    }}
                    value={JSON.stringify(this.state.rewardAmount)}
                  />
                  <Text
                    style={{
                      fontFamily: 'NotoSansCJKkr-Regular',
                      fontSize: 14,
                      fontWeight: 'normal',
                      fontStyle: 'normal',
                      lineHeight: 18,
                      letterSpacing: 0,
                      color: '#8e8e8f',
                      marginRight: 13,
                    }}>
                    원
                  </Text>
                  {/* <Text style={{marginLeft: 13}}>{this.state.name}</Text> */}
                </View>
              </View>
              <View style={{marginHorizontal: 20}}>
                <View
                  style={{
                    flexDirection: 'row',
                    justifyContent: 'space-between',
                    marginTop: 20,
                    marginBottom: 10,
                  }}>
                  <Text
                    style={{
                      fontSize: 16,
                      lineHeight: 18,
                      fontFamily: 'NotoSansCJKkr-Bold',
                    }}>
                    결제 금액
                  </Text>
                </View>
                <View
                  style={{
                    // backgroundColor: 'rgba(245, 245, 247, 0.6)',
                    height: 44,
                    borderRadius: 6,
                    flexDirection: 'row',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                  }}>
                  <Text
                    style={{
                      fontFamily: 'NotoSansCJKkr-Regular',
                      fontSize: 14,
                      fontWeight: 'normal',
                      fontStyle: 'normal',
                      lineHeight: 18,
                      letterSpacing: 0,
                      color: '#8e8e8f',
                      marginRight: 13,
                    }}>
                    금액
                  </Text>
                  <Text
                    style={{
                      fontFamily: 'NotoSansCJKkr-Regular',
                      fontSize: 14,
                      fontWeight: 'normal',
                      fontStyle: 'normal',
                      lineHeight: 18,
                      letterSpacing: 0,
                      // color: '#8e8e8f',
                    }}>
                    {`${this.state.price} 원`}
                  </Text>
                </View>
                <View
                  style={{
                    // backgroundColor: 'rgba(245, 245, 247, 0.6)',
                    height: 44,
                    borderRadius: 6,
                    flexDirection: 'row',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                  }}>
                  <Text
                    style={{
                      fontFamily: 'NotoSansCJKkr-Regular',
                      fontSize: 14,
                      fontWeight: 'normal',
                      fontStyle: 'normal',
                      lineHeight: 18,
                      letterSpacing: 0,
                      color: '#8e8e8f',
                      marginRight: 13,
                    }}>
                    쿠폰 할인 금액
                  </Text>
                  <Text
                    style={{
                      fontFamily: 'NotoSansCJKkr-Regular',
                      fontSize: 14,
                      fontWeight: 'normal',
                      fontStyle: 'normal',
                      lineHeight: 18,
                      letterSpacing: 0,
                      // color: '#8e8e8f',
                    }}>
                    {`(-)${this.state.selectedCoupon.discountPrice} 원`}
                  </Text>
                </View>
                <View
                  style={{
                    // backgroundColor: 'rgba(245, 245, 247, 0.6)',
                    height: 44,
                    borderRadius: 6,
                    flexDirection: 'row',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                  }}>
                  <Text
                    style={{
                      fontFamily: 'NotoSansCJKkr-Regular',
                      fontSize: 14,
                      fontWeight: 'normal',
                      fontStyle: 'normal',
                      lineHeight: 18,
                      letterSpacing: 0,
                      color: '#8e8e8f',
                      marginRight: 13,
                    }}>
                    리워드 할인 금액
                  </Text>
                  <Text
                    style={{
                      fontFamily: 'NotoSansCJKkr-Regular',
                      fontSize: 14,
                      fontWeight: 'normal',
                      fontStyle: 'normal',
                      lineHeight: 18,
                      letterSpacing: 0,
                      // color: '#8e8e8f',
                    }}>
                    {`(-)${this.state.rewardAmount} 원`}
                  </Text>
                </View>
                <View
                  style={{
                    height: 1,
                    backgroundColor: '#8e8e8f',
                    marginVertical: 10,
                  }}
                />
                <View
                  style={{
                    // width: 320,
                    flexDirection: 'row',
                    justifyContent: 'space-between',
                    marginBottom: 20,
                  }}>
                  <Text
                    style={{
                      fontFamily: 'NotoSansCJKkr-Regular',
                      fontSize: 16,
                      fontWeight: '500',
                      fontStyle: 'normal',
                      lineHeight: 20,
                      letterSpacing: 0,
                      color: '#1d1d1f',
                    }}>
                    최종 결제 금액
                  </Text>
                  <Text
                    style={{
                      fontFamily: 'NotoSansCJKkr-Bold',
                      fontSize: 20,
                      fontWeight: 'bold',
                      fontStyle: 'normal',
                      lineHeight: 24,
                      letterSpacing: 0,
                      textAlign: 'right',
                      color: '#1d1d1f',
                    }}>
                    {`${this.state.totalPrice}원`}
                  </Text>
                </View>
                <TouchableOpacity
                  style={{
                    backgroundColor: '#4f6cff',
                    // width: 320,
                    height: 44,
                    alignItems: 'center',
                    justifyContent: 'center',
                    borderRadius: 6,
                    marginLeft: 5,
                    flex: 1,
                    marginBottom: 10,
                  }}
                  onPress={async () => {
                    this.postPayment(
                      this.props.route.params.payType,
                      this.state.scheduleId,
                    );
                  }}>
                  <Text
                    style={{
                      // width: 93,
                      // height: 20,
                      fontFamily: 'NotoSansCJKkr-Bold',
                      fontSize: 16,
                      color: '#FFF',
                    }}>
                    결제하기
                  </Text>
                </TouchableOpacity>
              </View>
            </ScrollView>

            {/* <View
              style={{
                height: 70,
                marginHorizontal: 16,
                paddingVertical: 8,
                marginVertical: 8,
                alignItems: 'center',
                marginBottom: 50,
              }}>
              <View
                style={{
                  width: 320,
                  flexDirection: 'row',
                  justifyContent: 'space-between',
                  marginBottom: 20,
                }}>
                <Text
                  style={{
                    fontFamily: 'NotoSansCJKkr-Regular',
                    fontSize: 16,
                    fontWeight: '500',
                    fontStyle: 'normal',
                    lineHeight: 20,
                    letterSpacing: 0,
                    color: '#1d1d1f',
                  }}>
                  최종 결제 금액
                </Text>
                <Text
                  style={{
                    fontFamily: 'NotoSansCJKkr-Bold',
                    fontSize: 20,
                    fontWeight: 'bold',
                    fontStyle: 'normal',
                    lineHeight: 24,
                    letterSpacing: 0,
                    textAlign: 'right',
                    color: '#1d1d1f',
                  }}>
                  {`${this.state.totalPrice}원`}
                </Text>
              </View>
              <TouchableOpacity
                style={{
                  backgroundColor: '#4f6cff',
                  width: 320,
                  height: 44,
                  alignItems: 'center',
                  justifyContent: 'center',
                  borderRadius: 6,
                  marginLeft: 5,
                  flex: 1,
                  marginBottom: 10,
                }}
                onPress={async () => {
                  this.postPayment(
                    this.props.route.params.payType,
                    this.state.scheduleId,
                  );
                }}>
                <Text
                  style={{
                    // width: 93,
                    // height: 20,
                    fontFamily: 'NotoSansCJKkr-Bold',
                    fontSize: 16,
                    color: '#FFF',
                  }}>
                  결제하기
                </Text>
              </TouchableOpacity>
            </View> */}
          </View>
        )}
        {this.state.iamportOn && (
          <IamPortPayment2
            orderId={this.state.orderId}
            data={{
              pg: 'html5_inicis',
              pay_method: 'card',
              name: '아임포트 결제데이터 분석',
              merchant_uid: `mid_${this.state.orderId}_${new Date().getTime()}`,
              amount: JSON.stringify(this.state.totalPrice),
              buyer_name: this.state.user.nickname,
              buyer_tel: this.state.user.phone
                ? this.state.user.phone
                : '01011112222',
              buyer_email: 'example@naver.com',
              buyer_addr: '서울시 강남구 신사동 661-16',
              buyer_postcode: '06018',
              app_scheme: 'gajigaksek',
              // escrow: false,
              // [Deprecated v1.0.3]: m_redirect_url
            }}
            callback={(response: any) => {
              this.printResponse(response);
              if (response.imp_success === 'false') {
                this.setState({iamportOn: false}, () => {
                  this.props.navigation.goBack();
                  Alert.alert(
                    '결제를 취소하였습니다. 다시 신청 후 시도해주세요.',
                  );
                });
              }

              if (response.imp_success === 'true') {
                this.setState({iamportOn: false});

                this.patchPayment(
                  this.state.payType,
                  this.state.scheduleId,
                  response.imp_uid,
                  this.state.orderId,
                );
              }
            }}
          />
        )}
      </SafeAreaView>
    );
  }
  private styles = StyleSheet.create({
    dropDown: {
      backgroundColor: '#fafafb',
      borderWidth: 0,
      height: 34,
      borderRadius: 6,
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',
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
    title: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      marginTop: 20,
      marginBottom: 10,
    },
  });
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#FFF',
  },
  list: {
    paddingHorizontal: 10,
  },
  listContainer: {
    marginHorizontal: 20,
    marginTop: 20,
    // backgroundColor: 'red',
  },
  separator: {
    marginTop: 10,
  },
  /******** card **************/
  card: {
    // marginVertical: 8,
    backgroundColor: 'white',
    width: 152,
    // flexBasis: '45%',
    // marginHorizontal: 10,
    marginBottom: 15,
  },
  cardContent: {
    paddingTop: 7,
    paddingLeft: 4,
    justifyContent: 'space-between',
    // backgroundColor: 'skyblue',
  },
  cardImage: {
    flex: 1,
    height: 122,
    borderRadius: 9,
  },
  imageContainer: {
    // shadowColor: '#000',
    // shadowOffset: {
    //   width: 0,
    //   height: 4,
    // },
    // shadowOpacity: 0.32,
    // shadowRadius: 5.46,

    // elevation: 9,
    borderRadius: 10,
  },
  /******** card components **************/
  zone: {
    fontSize: 10,
    // flex: 1,
    color: '#FFF',

    // bottom: 0,
  },
  title: {
    // width: 93,
    // height: 20,
    fontFamily: 'NotoSansCJKkr-Bold',
    fontSize: 14,
    lineHeight: 16,
    color: '#070707',
    marginBottom: 4,
  },
  age: {
    fontFamily: 'NotoSansCJKkr-Regular',
    fontSize: 12,
    lineHeight: 14,
    color: '#4a4a4c',
    marginBottom: 4,
  },
  time: {
    fontSize: 18,
    flex: 1,
    color: '#778899',
  },
  people: {
    fontFamily: 'NotoSansCJKkr-Regular',
    fontSize: 14,
    lineHeight: 16,
    color: '#4a4a4c',
  },
  peopleBold: {
    fontFamily: 'NotoSansCJKkr-Bold',
    fontSize: 14,
    lineHeight: 16,
    color: '#4f6cff',
    marginLeft: 5,
  },
  count: {
    fontSize: 18,
    flex: 1,
    color: '#B0C4DE',
  },
});
