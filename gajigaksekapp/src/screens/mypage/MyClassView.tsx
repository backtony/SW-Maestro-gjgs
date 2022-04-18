import MypageApiController from '@/services/apis/MypageApiController';
import {STATUSBAR_HEIGHT} from '@/utils/commonParam';
import React from 'react';
import {ScrollView, StyleSheet, Text, View} from 'react-native';
import DropDownPicker from 'react-native-dropdown-picker';
import {Button} from 'react-native-elements';
import {Image} from 'react-native-elements/dist/image/Image';
import {TouchableOpacity} from 'react-native-gesture-handler';
import Icon from 'react-native-vector-icons/Ionicons';
import MyClassBlock from './MyClassBlock';

export interface MyPageClassInfo {
  currentLectureStatus: string;
  endDateTime: string;
  leader: boolean;
  lectureId: number;
  lectureThumbnailUrl: string;
  lectureTitle: string;
  orderId: number;
  orderStatus: string;
  paid: boolean;
  scheduleId: number;
  startDateTime: string;
  teamId: number;
  reviewed: boolean;
}

interface MyClassViewStates {
  dropDowns: number[];
  lectures: MyPageClassInfo[];
}

export default class MyClassView extends React.Component<
  {},
  MyClassViewStates
> {
  constructor(props: any) {
    super(props);
    this.state = {dropDowns: [], lectures: []};
    this.getLectures();
  }

  private async getLectures() {
    try {
      const res = await MypageApiController.getLectures();
      console.log('res: ', res);
      this.setState({lectures: res?.data.content});
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
          <Text
            style={{
              // width: 93,
              // height: 20,
              fontFamily: 'NotoSansCJKkr-Bold',
              fontSize: 20,
              color: '#1d1d1f',
            }}>
            나의 클래스
          </Text>
        </View>
        <ScrollView style={{marginHorizontal: 20, marginTop: 10}}>
          {this.state.lectures.map((lecture: MyPageClassInfo) => (
            <MyClassBlock
              classInfo={lecture}
              navigation={this.props.navigation}
            />
          ))}
        </ScrollView>
      </View>
    );
  }
  private styles = StyleSheet.create({
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
