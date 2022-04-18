import MypageApiController from '@/services/apis/MypageApiController';
import {STATUSBAR_HEIGHT} from '@/utils/commonParam';
import React from 'react';
import {ScrollView, StyleSheet, View} from 'react-native';
import {Button, Text} from 'react-native-elements';
import {Image} from 'react-native-elements/dist/image/Image';
import {TouchableOpacity} from 'react-native-gesture-handler';
import Icon from 'react-native-vector-icons/Ionicons';
import RewardBlock from './RewardBlock';

export interface RewardInfo {
  amount: number;
  createdDate: string;
  rewardType: string;
  text: string;
}

interface RewardViewStates {
  type: string;
  rewards: RewardInfo[];
  totalReward: number;
}

const rewardItem = {
  amount: 1000,
  createdDate: '2021-09-24T05:16:28.393Z',
  rewardType: '???',
  text: '후기 이벤트',
};

export default class RewardView extends React.Component<{}, RewardViewStates> {
  constructor(props: any) {
    super(props);
    this.state = {type: 'SAVE', rewards: [rewardItem], totalReward: 0};
    this.getReward('SAVE');
  }

  private async getReward(type: string) {
    try {
      const res = await MypageApiController.getReward(type);
      if (!res) {
        return;
      }
      const data = res.data;
      this.setState({
        rewards: data.rewardDtoList.content,
        totalReward: data.totalReward,
      });
    } catch (e) {}
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
          <Text style={styles.text6}>리워드</Text>
        </View>
        <View style={{alignItems: 'center'}}>
          <View style={styles.wrapper}>
            <View style={styles.wrapper2}>
              <View style={{flexDirection: 'row', marginBottom: 4}}>
                <Image
                  style={{width: 28, height: 28}}
                  source={{
                    uri: 'https://cdn.zeplin.io/60f22448a6ebf90ffb53be83/assets/7BD9BFD9-0F76-4D13-8582-9C28A1412585.png',
                  }}
                />
                <Text style={styles.text7}>리워드</Text>
              </View>
              <Text style={styles.text8}>{`${this.state.totalReward}원`}</Text>
            </View>
          </View>
          <View style={styles.wrapper3}>
            <TouchableOpacity
              onPress={() =>
                this.setState({type: 'SAVE'}, () => this.getReward('SAVE'))
              }
              style={[
                styles.button,
                this.state.type === 'SAVE'
                  ? {backgroundColor: '#4f6cff'}
                  : {backgroundColor: '#f5f7ff'},
              ]}>
              <Text
                style={[
                  styles.text9,
                  this.state.type === 'SAVE'
                    ? {color: '#FFF'}
                    : {color: '#4a4a4c'},
                ]}>
                적립
              </Text>
            </TouchableOpacity>
            <TouchableOpacity
              onPress={() =>
                this.setState({type: 'USE'}, () => this.getReward('USE'))
              }
              style={[
                styles.button2,
                this.state.type === 'USE'
                  ? {backgroundColor: '#4f6cff'}
                  : {backgroundColor: '#f5f7ff'},
              ]}>
              <Text
                style={[
                  styles.text10,
                  this.state.type === 'USE'
                    ? {color: '#FFF'}
                    : {color: '#4a4a4c'},
                ]}>
                사용내역
              </Text>
            </TouchableOpacity>
          </View>
        </View>
        <ScrollView style={{marginHorizontal: 20, marginTop: 10}}>
          {this.state.rewards.map((reward: RewardInfo) => (
            <RewardBlock
              text={reward.text}
              createdDate={reward.createdDate}
              amount={reward.amount}
              rewardType={reward.rewardType}
            />
          ))}
        </ScrollView>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  text10: {
    fontFamily: 'NotoSansCJKkr-Regular',
    fontSize: 14,
    fontWeight: '500',
    fontStyle: 'normal',
    lineHeight: 18,
    letterSpacing: 0,
    textAlign: 'center',
  },
  button2: {
    width: 172,
    height: 40,
    borderRadius: 6,
    justifyContent: 'center',
    alignItems: 'center',
  },
  text9: {
    fontFamily: 'NotoSansCJKkr-Regular',
    fontSize: 14,
    fontWeight: '500',
    fontStyle: 'normal',
    lineHeight: 18,
    letterSpacing: 0,
    textAlign: 'center',
  },
  button: {
    width: 172,
    height: 40,
    borderRadius: 6,
    justifyContent: 'center',
    alignItems: 'center',
  },
  wrapper3: {
    flexDirection: 'row',
    backgroundColor: 'white',
    width: 350,
    justifyContent: 'space-between',
    marginTop: 20,
  },
  text8: {
    fontSize: 16,
    fontFamily: 'NotoSansCJKkr-Bold',
    marginBottom: 4,
  },
  text7: {
    fontSize: 16,
    marginLeft: 10,
    fontFamily: 'NotoSansCJKkr-Bold',
  },
  wrapper2: {
    marginTop: 9,
    flexDirection: 'row',
    justifyContent: 'space-between',
    flex: 1,
  },
  wrapper: {
    width: 350,
    height: 80,
    borderRadius: 6,
    backgroundColor: '#fafafb',
    padding: 20,
  },
  text6: {
    fontFamily: 'NotoSansCJKkr-Bold',
    fontSize: 20,
    color: '#1d1d1f',
  },
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
