import React, {Component} from 'react';
import {
  StyleSheet,
  Text,
  View,
  TouchableOpacity,
  Image,
  Alert,
  ScrollView,
  FlatList,
  Switch,
} from 'react-native';
import bulletinApi from '../../services/apis/bulletinApi';
import Zone from '../../utils/Zone';
import {STATUSBAR_HEIGHT} from '../../utils/commonParam';
import {Button} from 'react-native-elements';
import Icon from 'react-native-vector-icons/Ionicons';
import MypageApiController from '../../services/apis/MypageApiController';
import BulletinApiController from '../../services/apis/BulletinApiController';

const timeParamList = [
  ['MORNING', '오전'],
  ['NOON', '오후'],
  ['AFTERNOON', '저녁'],
];

export default class MyPageBulletin extends Component<
  {},
  {bulletinTest: boolean}
> {
  constructor() {
    super();
    this.state = {data: [], bulletinTest: false};
    this.getBulletin();
  }

  addProductToCart = () => {
    Alert.alert('Success', 'The product has been added to your cart');
  };

  private async getBulletin() {
    try {
      const res = await MypageApiController.getBulletin({});

      this.setState({data: res?.data.myBulletinDtoList});
    } catch (e) {
      console.error(e);
    }
  }

  private englishToKoreanAge(age1: string) {
    switch (age1) {
      case 'TWENTY_TO_TWENTYFIVE':
        return '20~25세';
      case 'TWENTYFIVE_TO_THIRTY':
        return '25~30세';
      case 'THIRTY_TO_THIRTYFIVE':
        return '30~35세';
      case 'THIRTYFIVE_TO_FORTY':
        return '35세~40세';
      case 'FORTY':
        return '40세 이상';
      default:
        return '';
    }
  }

  private timeParamToKorean(timeParam: string) {
    let timeKorean;
    timeParamList.forEach(item => {
      if (item[0] === timeParam) {
        timeKorean = item[1];
      }
    });
    return timeKorean;
  }

  private async patchBulletinStatus(bulletinId: number) {
    try {
      await BulletinApiController.patchBulletinStatus({bulletinId});
      this.getBulletin();
    } catch (e) {
      console.error(e);
    }
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
            나의 게시글
          </Text>
        </View>
        <FlatList
          style={styles.list}
          contentContainerStyle={styles.listContainer}
          columnWrapperStyle={{justifyContent: 'space-between'}}
          data={this.state.data}
          horizontal={false}
          numColumns={2}
          keyExtractor={item => {
            return item.id;
          }}
          ItemSeparatorComponent={() => {
            return <View style={styles.separator} />;
          }}
          renderItem={post => {
            const item = post.item;
            return (
              <TouchableOpacity
                style={styles.card}
                onPress={() =>
                  this.props.navigation.navigate('bulletinDash', {
                    bulletinId: item.bulletinId,
                  })
                }>
                <View style={styles.imageContainer}>
                  <Image
                    style={styles.cardImage}
                    source={{uri: item.thumbnailImageFileUrl}}
                  />
                  <View
                    style={{
                      position: 'absolute',
                      bottom: 0,
                      flexDirection: 'row',
                      borderRadius: 9,
                      backgroundColor: 'rgba(0, 0, 0, 0.3)',
                      padding: 2,
                      paddingHorizontal: 5,
                      margin: 8,
                    }}>
                    <Icon name="location-sharp" size={12} color="#FFF" />
                    <Text style={styles.zone}>
                      {Zone.getZone(item.zoneId)[1]}
                    </Text>
                  </View>
                </View>
                <View style={styles.cardContent}>
                  <Text style={styles.title}>{item.title}</Text>
                  <Text style={styles.age}>
                    {this.englishToKoreanAge(item.age)} ·{' '}
                    {this.timeParamToKorean(item.timeType)}
                  </Text>
                  <View
                    style={{
                      flexDirection: 'row',
                      justifyContent: 'space-between',
                      alignItems: 'center',
                    }}>
                    <View style={{flexDirection: 'row', alignItems: 'center'}}>
                      <Icon name="person" size={14} color="#4f6cff" />
                      <Text style={styles.peopleBold}>
                        {item.currentPeople}
                      </Text>
                      <Text style={styles.people}>/{item.maxPeople}</Text>
                    </View>
                    <Switch
                      trackColor={{false: '#d2d2d2', true: '#4f6cff'}}
                      onValueChange={() =>
                        this.patchBulletinStatus(item.bulletinId)
                      }
                      style={{transform: [{scaleX: 0.6}, {scaleY: 0.6}]}}
                      value={item.status}
                    />
                  </View>
                </View>
              </TouchableOpacity>
            );
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
