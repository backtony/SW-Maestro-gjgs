import Zone from '@/utils/Zone';
import React from 'react';
import {
  FlatList,
  Image,
  SafeAreaView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';

import Icon from 'react-native-vector-icons/Ionicons';
import {Button} from 'react-native-elements';
import DropDownPicker from 'react-native-dropdown-picker';
import FavoriteSelectModal from '@/screens/modal/FavoriteSelectModal';
import LectureApiController from '@/services/apis/LectureApiController';

interface HomeConceptLectureListProps {
  navigation: any;
}

interface HomeConceptLectureListStates {
  data: any;
  dropDowns: number[];
  favorites: number[];
  favoriteModalVisible: boolean;
  heartClickedLecture: any;
}

export default class HomeConceptLectureList extends React.Component<
  HomeConceptLectureListProps,
  HomeConceptLectureListStates
> {
  constructor(props: any) {
    super(props);
    this.state = {
      data: [],
      dropDowns: [],
      favorites: [],
      favoriteModalVisible: false,
      heartClickedLecture: null,
    };
    this.getConceptLectureList();
  }

  private async getConceptLectureList() {
    const params = {
      page: 1,
      size: 4,
      zoneId: 2,
      categoryIdList: [2],
    };
    console.log('params: ', params);
    try {
      const res = await LectureApiController.getLectureList(params);

      this.setState({
        data: res.data.content.slice(0, 4),
      });
    } catch (e) {
      console.error(e);
    }
  }

  private toggleDropDown = (id: number) => {
    const updateddropDowns = this.state.dropDowns;
    if (updateddropDowns.includes(id)) {
      updateddropDowns.splice(updateddropDowns.indexOf(id), 1);
    } else {
      updateddropDowns.push(id);
    }

    this.setState({dropDowns: updateddropDowns});
  };

  render() {
    return (
      <SafeAreaView style={{flex: 1}}>
        <FlatList
          style={styles.list}
          ListHeaderComponent={() => <View />}
          ListFooterComponent={() => <View />}
          ListEmptyComponent={() => <View />}
          contentContainerStyle={styles.listContainer}
          // columnWrapperStyle={{justifyContent: 'space-between'}}
          data={this.state.data}
          // horizontal
          numColumns={2}
          keyExtractor={item => {
            return item.lectureId;
          }}
          ItemSeparatorComponent={() => {
            return <View style={styles.separator} />;
          }}
          renderItem={post => {
            const item = post.item;
            return (
              <View style={styles.card}>
                <TouchableOpacity
                  onPress={() => {
                    this.props.navigation.navigate('lectureDash', {
                      lectureId: item.lectureId,
                      myFavorite: item.myFavorite,
                      lecture: item,
                    });
                  }}>
                  <View style={styles.imageContainer}>
                    <Image
                      style={styles.cardImage}
                      source={{uri: item.imageUrl}}
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
                          // this.toggleFavorite(item.lectureId);
                          this.setState({heartClickedLecture: item});
                          this.setState({favoriteModalVisible: true});
                        }}
                      />
                    </View>
                  </View>

                  <View style={styles.cardContent}>
                    <Text style={styles.title}>{item.title}</Text>
                  </View>
                </TouchableOpacity>
                <DropDownPicker
                  open={!!this.state.dropDowns.includes(item.lectureId)}
                  value={'one'}
                  items={[
                    {
                      label: `${item.priceOne}원`,
                      value: 'one',
                      icon: () => <Text style={{fontSize: 12}}>1인</Text>,
                    },
                    {
                      label: `${item.priceTwo}원`,
                      value: 'two',
                      icon: () => <Text style={{fontSize: 12}}>2인</Text>,
                    },
                    {
                      label: `${item.priceThree}원`,
                      value: 'three',
                      icon: () => <Text style={{fontSize: 12}}>3인</Text>,
                    },
                    {
                      label: `${item.priceFour}원`,
                      value: 'four',
                      icon: () => <Text style={{fontSize: 12}}>4인 이상</Text>,
                    },
                  ]}
                  setOpen={() => {
                    this.toggleDropDown(item.lectureId);
                  }}
                  setValue={() => {}}
                  setItems={() => {}}
                  showTickIcon={false}
                  dropDownDirection="TOP"
                  style={{
                    backgroundColor: '#fafafb',
                    borderWidth: 0,
                    height: 34,
                  }}
                  listItemContainerStyle={{height: 28}}
                  listItemLabelStyle={{color: '#4a4a4c', fontSize: 12}} //dd
                  dropDownContainerStyle={{
                    borderWidth: 0,
                  }}
                  arrowIconStyle={{
                    width: 18,
                    height: 18,
                  }}
                  textStyle={{fontSize: 14, fontWeight: 'bold'}}
                />
              </View>
            );
          }}
        />
        <View>
          <FavoriteSelectModal
            modalVisible={this.state.favoriteModalVisible}
            lecture={this.state.heartClickedLecture}
            setModalVisible={(visible: boolean) => {
              this.setState({favoriteModalVisible: visible});
            }}
            setFavorite={() => {
              this.getConceptLectureList();
            }}
          />
        </View>
      </SafeAreaView>
    );
  }
}

const styles = StyleSheet.create({
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
    // paddingHorizontal: 10,
  },
  listContainer: {
    // marginHorizontal: 10,
    marginTop: 10,
    // backgroundColor: 'red',
  },
  separator: {
    marginTop: 10,
  },
  /******** card **************/
  card: {
    // marginVertical: 8,
    backgroundColor: 'white',
    width: '45%',
    // flexBasis: '45%',
    marginHorizontal: 10,
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
