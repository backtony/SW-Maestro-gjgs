import React from 'react';
import {StyleSheet, TouchableOpacity, Image, FlatList} from 'react-native';
import {View, Text} from 'react-native';
import {Button} from 'react-native-elements';
import Icon from 'react-native-vector-icons/Ionicons';
import BulletinApiController from '../../../services/apis/BulletinApiController';
import FavoriteApiController from '../../../services/apis/FavoriteApiController';
import {
  englishAge,
  englishTime,
  STATUSBAR_HEIGHT,
} from '../../../utils/commonParam';
import UserDC from '../../../services/login/UserDC';
import Zone from '../../../utils/Zone';

interface LectureBulletinViewStates {
  keyword: string;
  data: any;
  zoneModalVisible: boolean;
  selectedSubZone: string;
  page: number;
  refreshing: boolean;
  totalElements: number;
}

export default class LectureBulletinView extends React.Component<
  {},
  LectureBulletinViewStates
> {
  constructor(props: any) {
    super(props);
    this.state = {
      keyword: '',
      data: [],
      zoneModalVisible: false,
      selectedSubZone: '',
      refreshing: false,
      page: 0,
      totalElements: 0,
    };
  }

  componentDidMount() {
    this.getBulletinListApi();
  }

  private async getBulletinListApi() {
    try {
      const res = await BulletinApiController.getLectureBulletin(
        {
          page: this.state.page ? this.state.page : 0,
          size: 10,
        },
        this.props.route.params.lectureId,
      );

      this.setState({
        data:
          this.state.page === 0
            ? res.data.content
            : [...this.state.data, ...res.data.content],
        refreshing: false,
        totalElements: res.data.totalElements,
      });
    } catch (e) {
      console.error(e);
      // alert('게시글 로드에 실패했습니다.');
    }
  }

  private LoadMoreBulletin() {
    this.setState({page: this.state.page + 1}, () => {
      this.getBulletinListApi();
    });
  }

  private toggleFavoriteBulletin(item: any) {
    const bulletinList = this.state.data;
    let updatedList = bulletinList.map(bulletin => {
      if (bulletin === item) {
        bulletin.myFavorite = !item.myFavorite;
      }
      return bulletin;
    });
    this.setState({data: updatedList});
  }

  private async postFavoriteBulletin(item: any) {
    try {
      await FavoriteApiController.postFavoriteBulletin({
        bulletinId: item.bulletinId,
      });
      this.toggleFavoriteBulletin(item);
    } catch (e) {
      console.error(e);
    }
  }

  private async deleteFavoriteBulletin(item: any) {
    try {
      await FavoriteApiController.deleteFavoriteBulletin({
        bulletinId: item.bulletinId,
      });
      this.toggleFavoriteBulletin(item);
    } catch (e) {
      console.error(e);
    }
  }

  render() {
    return (
      <View style={{flex: 1, backgroundColor: '#FFF'}}>
        <View style={styles.header}>
          <Button
            icon={<Icon name="chevron-back" size={24} color="#1d1d1f" />}
            buttonStyle={{backgroundColor: 'white'}}
            onPress={() => this.props.navigation.goBack()}
          />
          <Text style={styles.headerTitle}>관련 게시글</Text>
        </View>
        <View style={{backgroundColor: '#FFF', flex: 1}}>
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
                      source={{uri: item.lectureImageUrl}}
                    />
                    <View style={styles.locationWrapper}>
                      <Icon name="location-sharp" size={12} color="#FFF" />
                      <Text style={styles.zone}>
                        {Zone.getZone(item.zoneId)[1]}
                      </Text>
                    </View>
                  </View>
                  <View style={styles.cardContent}>
                    <Text style={styles.title}>{item.bulletinTitle}</Text>
                    <Text style={styles.age}>
                      {englishAge[item.age]} ·{' '}
                      {item.time
                        .split('|')
                        .map(tt => englishTime[tt])
                        .join('/')}
                    </Text>
                    <View style={{flexDirection: 'row', alignItems: 'center'}}>
                      <Icon name="person" size={14} color="#4f6cff" />
                      <Text style={styles.peopleBold}>{item.nowMembers}</Text>
                      <Text style={styles.people}>/{item.maxMembers}</Text>
                    </View>
                  </View>
                  <View style={{position: 'absolute', right: 0}}>
                    <Button
                      icon={
                        item.myFavorite ? (
                          <Icon name="heart" size={30} color="#ff4f4f" />
                        ) : (
                          <Icon name="heart-outline" size={30} color="#FFF" />
                        )
                      }
                      buttonStyle={{
                        backgroundColor: 'transparent',
                      }}
                      onPress={() => {
                        if (UserDC.isLogout()) {
                          alert('로그인 해주세요.');
                          return;
                        }
                        if (!item.myFavorite) {
                          this.postFavoriteBulletin(item);
                        } else if (item.myFavorite) {
                          this.deleteFavoriteBulletin(item);
                        }
                      }}
                    />
                  </View>
                </TouchableOpacity>
              );
            }}
            refreshing={this.state.refreshing}
            onRefresh={() =>
              this.setState({page: 0}, () => {
                this.getBulletinListApi();
              })
            }
            onEndReachedThreshold={0}
            onEndReached={() => {
              this.LoadMoreBulletin();
            }}
          />
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    header: {
      fontSize: 25,
    },
  });
}

const styles = StyleSheet.create({
  locationWrapper: {
    position: 'absolute',
    bottom: 0,
    flexDirection: 'row',
    borderRadius: 9,
    backgroundColor: 'rgba(0, 0, 0, 0.3)',
    padding: 2,
    paddingHorizontal: 5,
    margin: 8,
  },
  headerTitle: {
    fontFamily: 'NotoSansCJKkr-Bold',
    fontSize: 20,
    color: '#1d1d1f',
  },
  subCategory: {
    backgroundColor: '#2980b9',
    padding: 10,
    borderRadius: 5,
    margin: 5,
    alignContent: 'center',
    justifyContent: 'center',
  },
  subCategoryText: {
    fontSize: 20,
    textAlign: 'center',
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
    marginTop: 10,
  },
  separator: {
    marginTop: 10,
  },
  /******** card **************/
  card: {
    backgroundColor: 'white',
    width: 152,
    marginBottom: 15,
  },
  cardContent: {
    paddingTop: 7,
    paddingLeft: 4,
    justifyContent: 'space-between',
  },
  cardImage: {
    flex: 1,
    height: 122,
    borderRadius: 9,
  },
  imageContainer: {
    borderRadius: 10,
  },
  /******** card components **************/
  zone: {
    fontSize: 10,
    color: '#FFF',
  },
  title: {
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
  header: {
    marginTop: STATUSBAR_HEIGHT,
    height: 60,
    width: '100%',
    flexDirection: 'row',
    justifyContent: 'flex-start',
    alignItems: 'center',
    backgroundColor: '#FFF',
  },
});
