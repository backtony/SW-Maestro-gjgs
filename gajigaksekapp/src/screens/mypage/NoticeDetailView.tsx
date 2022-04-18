import MypageApiController from '@/services/apis/MypageApiController';
import {parseDate} from '@/utils/commonFunctions';
import {STATUSBAR_HEIGHT} from '@/utils/commonParam';
import React from 'react';
import {ScrollView, StyleSheet, Text, View} from 'react-native';
import {Button} from 'react-native-elements';
import Icon from 'react-native-vector-icons/Ionicons';
import {NoticeInfo} from './NoticeListView';

interface NoticeDetailViewStates {
  noticeInfo: NoticeInfo;
}

export default class NoticeDetailView extends React.Component<
  {},
  NoticeDetailViewStates
> {
  constructor(props: any) {
    super(props);
    this.state = {
      noticeInfo: {
        createdDate: '2021-10-05T01:54:28.076Z',
        noticeId: 0,
        text: '내일부터 해당 서비스를 종료되어집니다.',
        title: '중요한 공지를 알려드립니다.',
      },
    };
  }
  componentDidMount() {
    this.getNoticeDetail(this.props.route.params.noticeId);
  }

  private async getNoticeDetail(noticeId: number) {
    try {
      const res = await MypageApiController.getNoticeDetail(noticeId);
      this.setState({noticeInfo: res?.data});
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
            공지사항 조회
          </Text>
        </View>
        <ScrollView>
          <Text
            style={{
              fontFamily: 'NotoSansCJKkr-Bold',
              fontSize: 16,
              lineHeight: 18,
              color: '#1d1d1f',
              alignSelf: 'flex-start',
              marginLeft: 20,
              marginTop: 20,
            }}>
            제목
          </Text>
          <View
            style={{
              flexDirection: 'row',
              alignSelf: 'flex-start',
              marginLeft: 20,
              marginTop: 8,
            }}>
            <Text>{this.state.noticeInfo.title}</Text>
          </View>
          <Text
            style={{
              fontFamily: 'NotoSansCJKkr-Bold',
              fontSize: 16,
              lineHeight: 18,
              color: '#1d1d1f',
              alignSelf: 'flex-start',
              marginLeft: 20,
              marginTop: 20,
            }}>
            작성일시
          </Text>
          <View
            style={{
              flexDirection: 'row',
              alignSelf: 'flex-start',
              marginLeft: 20,
              marginTop: 8,
            }}>
            <Text>{parseDate(this.state.noticeInfo.createdDate)}</Text>
          </View>

          <Text
            style={{
              fontFamily: 'NotoSansCJKkr-Bold',
              fontSize: 16,
              lineHeight: 18,
              color: '#1d1d1f',
              alignSelf: 'flex-start',
              marginLeft: 20,
              marginTop: 20,
            }}>
            내용
          </Text>
          <View
            style={{
              flexDirection: 'row',
              alignSelf: 'flex-start',
              marginLeft: 20,
              marginTop: 8,
            }}>
            <Text>{this.state.noticeInfo.text}</Text>
          </View>
        </ScrollView>
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
    // marginTop: 20,
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
