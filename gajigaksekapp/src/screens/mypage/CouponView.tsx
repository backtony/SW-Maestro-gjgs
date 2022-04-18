import CouponController from '@/services/apis/CouponController';
import {STATUSBAR_HEIGHT} from '@/utils/commonParam';
import React from 'react';
import {
  ScrollView,
  StyleSheet,
  Text,
  View,
  TouchableOpacity,
} from 'react-native';
import {Button} from 'react-native-elements';
import {FlatList} from 'react-native-gesture-handler';
import Icon from 'react-native-vector-icons/Ionicons';
import CouponAddModal from '../modal/CouponAddModal';
import CouponBlock from './CouponBlock';

export interface MyCouponInfo {
  closeDate: string;
  discountPrice: number;
  issueDate: string;
  memberCouponId: number;
  title: string;
}

interface CouponViewStates {
  coupons: MyCouponInfo[];
  modalVisible: boolean;
  page: number;
  refreshing: boolean;
}

const couponTest = {
  closeDate: '2021-09-29T05:27:30.308Z',
  issueDate: '2021-09-29T05:27:30.308Z',
  title: '신규 회원 가입 쿠폰',
  discountPrice: 10000,
  memberCouponId: 0,
};

export default class CouponView extends React.Component<{}, CouponViewStates> {
  constructor(props: any) {
    super(props);
    this.state = {
      coupons: [couponTest, couponTest],
      modalVisible: false,
      page: 0,
      refreshing: false,
    };
    this.getMyCoupon();
  }

  private async getMyCoupon() {
    try {
      const res = await CouponController.getMyCoupon();
      console.log('coupons: ', res.data.myCouponList);
      this.setState({coupons: res.data.myCouponList});
    } catch (e) {}
  }

  private onRefreshing() {
    console.log('??');
    // this.setState({refreshing: true});
    // const page = this.state.page;
    // this.getMyCoupon(page + 1);
    // this.setState({page: page + 1, refreshing: false});
  }

  render() {
    return (
      <View style={styles.container}>
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
            쿠폰함
          </Text>
        </View>
        <View style={{alignItems: 'center'}}>
          <TouchableOpacity
            onPress={() => this.setState({modalVisible: true})}
            style={{
              width: 350,
              height: 44,
              borderRadius: 6,
              backgroundColor: '#4f6cff',
              alignItems: 'center',
              justifyContent: 'center',
            }}>
            <Text
              style={{
                fontFamily: 'NotoSansCJKkr-Bold',
                fontSize: 16,
                fontWeight: 'bold',
                fontStyle: 'normal',
                lineHeight: 20,
                letterSpacing: 0,
                color: '#ffffff',
              }}>
              + 쿠폰 등록
            </Text>
          </TouchableOpacity>
        </View>
        <FlatList
          style={styles.list}
          contentContainerStyle={styles.listContainer}
          data={this.state.coupons}
          // numColumns={2}
          keyExtractor={item => {
            return item.id;
          }}
          ItemSeparatorComponent={() => {
            return <View style={styles.separator} />;
          }}
          renderItem={post => {
            const item: MyCouponInfo = post.item;
            return (
              <CouponBlock
                closeDate={item.closeDate}
                issueDate={item.issueDate}
                title={item.title}
                discountPrice={item.discountPrice}
                memberCouponId={item.memberCouponId}
              />
            );
          }}
          refreshing={this.state.refreshing}
          onRefresh={() => this.onRefreshing()}
        />
        {/* <ScrollView style={{marginHorizontal: 20, marginTop: 10}}>
          {this.state.coupons.map((coupon: MyCouponInfo) => (
            <CouponBlock
              closeDate={coupon.closeDate}
              issueDate={coupon.issueDate}
              title={coupon.title}
              discountPrice={coupon.discountPrice}
              memberCouponId={coupon.memberCouponId}
            />
          ))}
        </ScrollView> */}
        <CouponAddModal
          modalVisible={this.state.modalVisible}
          setModalVisible={(visible: boolean) => {
            this.setState({modalVisible: visible});
          }}
          setParentSubZone={(sub: string) => {
            // this.setState({profileSubZone: sub});
          }}
        />
      </View>
    );
  }
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
