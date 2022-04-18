import MypageApiController from '@/services/apis/MypageApiController';
import {parseDate} from '@/utils/commonFunctions';
import {STATUSBAR_HEIGHT} from '@/utils/commonParam';
import React from 'react';
import {FlatList, StyleSheet, Text, TouchableOpacity, View} from 'react-native';
import {Button} from 'react-native-elements';
import Icon from 'react-native-vector-icons/Ionicons';

export interface NoticeInfo {
  createdDate: string;
  noticeId: number;
  text: string;
  title: string;
}

const testList = [
  {
    createdDate: '2021-10-05T01:54:28.076Z',
    noticeId: 0,
    text: '내일부터 해당 서비스를 종료되어집니다.',
    title: '중요한 공지를 알려드립니다.',
  },
  {
    createdDate: '2021-10-05T01:54:28.076Z',
    noticeId: 1,
    text: '금일 오후부터 정기 점검에 들어갈 예정입니다. 이사항에 대해서 양해부탁드립니다.',
    title: '점검안내사항',
  },
];

interface NoticeListViewStates {
  notices: NoticeInfo[];
  refreshing: boolean;
}

export default class NoticeListView extends React.Component<
  {},
  NoticeListViewStates
> {
  constructor(props: any) {
    super(props);
    this.state = {notices: testList, refreshing: false};
    this.getNotices();
  }

  private async getNotices() {
    try {
      const res = await MypageApiController.getNotices();
      this.setState({notices: res?.data.content}, () =>
        console.log('notices: ', this.state.notices),
      );
    } catch (e) {}
  }

  private onRefreshing() {}
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
            공지사항
          </Text>
        </View>

        <FlatList
          style={styles.list}
          contentContainerStyle={styles.listContainer}
          data={this.state.notices}
          keyExtractor={item => {
            return JSON.stringify(item.noticeId);
          }}
          ItemSeparatorComponent={() => {
            return <View style={styles.separator} />;
          }}
          renderItem={post => {
            const item: NoticeInfo = post.item;
            return (
              <TouchableOpacity
                onPress={() =>
                  this.props.navigation.navigate('noticeDetail', {
                    noticeId: item.noticeId,
                  })
                }>
                <Text
                  style={{
                    // width: 93,
                    // height: 20,
                    fontFamily: 'NotoSansCJKkr-Bold',
                    fontSize: 16,
                    color: '#1d1d1f',
                  }}>
                  {item.title}
                </Text>
                <Text
                  numberOfLines={1}
                  style={{
                    // width: 93,
                    // height: 20,
                    fontFamily: 'NotoSansCJKkr-Regular',
                    fontSize: 16,
                    color: '#8e8e8f',
                  }}>
                  {item.text}
                </Text>
                <Text
                  style={{
                    // width: 93,
                    // height: 20,
                    fontFamily: 'NotoSansCJKkr-Regular',
                    fontSize: 12,
                    color: '#8e8e8f',
                  }}>
                  {parseDate(item.createdDate)}
                </Text>
                <View
                  style={{
                    width: 320,
                    height: 1,
                    marginTop: 20,
                    backgroundColor: '#f5f5f7',
                  }}
                />
              </TouchableOpacity>
            );
          }}
          refreshing={this.state.refreshing}
          onRefresh={() => this.onRefreshing()}
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
