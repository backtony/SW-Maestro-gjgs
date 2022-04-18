import React from 'react';
import {StyleSheet, Text, View} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import {Button} from 'react-native-elements';
import {STATUSBAR_HEIGHT} from '@/utils/commonParam';
import {parseDate} from '@/utils/commonFunctions';
import NotificationController from '@/services/apis/NotificationController';

export default class NotificationDetailView extends React.Component {
  componentDidMount() {
    if (!this.props.route.params.noti.checked) {
      this.postNotiRead(this.props.route.params.noti.uuid);
    }
  }

  private async postNotiRead(uuid: string) {
    console.log('uuid: ', uuid);
    try {
      await NotificationController.postRead(uuid);
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
            알림 조회
          </Text>
        </View>
        <View
          style={{
            flexDirection: 'row',
            marginTop: 10,
            alignItems: 'center',
            marginBottom: 10,
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
            제목
          </Text>
        </View>
        <View
          style={{
            flexDirection: 'row',
            alignItems: 'center',
            marginBottom: 20,
            marginHorizontal: 20,
            padding: 10,
            backgroundColor: '#fafafb',
          }}>
          <Text
            style={{
              fontSize: 16,
              lineHeight: 18,
              fontFamily: 'NotoSansCJKkr-Regular',
              marginTop: 7,
              marginLeft: 6,
            }}>
            {this.props.route.params.noti.title}
          </Text>
        </View>

        <View
          style={{
            flexDirection: 'row',
            marginTop: 10,
            alignItems: 'center',
            marginBottom: 10,
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
            시간
          </Text>
        </View>
        <View
          style={{
            flexDirection: 'row',
            alignItems: 'center',
            marginBottom: 20,
            marginHorizontal: 20,
            padding: 10,
            backgroundColor: '#fafafb',
          }}>
          <Text
            style={{
              fontSize: 16,
              lineHeight: 18,
              fontFamily: 'NotoSansCJKkr-Regular',
              marginTop: 7,
              marginLeft: 6,
            }}>
            {parseDate(this.props.route.params.noti.createdDate)}
          </Text>
        </View>

        <View
          style={{
            flexDirection: 'row',
            marginTop: 10,
            alignItems: 'center',
            marginBottom: 10,
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
            내용
          </Text>
        </View>
        <View
          style={{
            flexDirection: 'row',
            alignItems: 'center',
            marginBottom: 20,
            marginHorizontal: 20,
            padding: 10,
            backgroundColor: '#fafafb',
          }}>
          <Text
            style={{
              fontSize: 16,
              lineHeight: 18,
              fontFamily: 'NotoSansCJKkr-Regular',
              marginTop: 7,
              marginLeft: 6,
            }}>
            {this.props.route.params.noti.message}
          </Text>
        </View>
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
