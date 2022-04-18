import React from 'react';
import {StyleSheet, TouchableOpacity, Image, FlatList} from 'react-native';
import {View, Text} from 'react-native';
import {Button} from 'react-native-elements';
import Icon from 'react-native-vector-icons/Ionicons';
import Category from '../../../utils/Category';
import Zone from '../../../utils/Zone';
import DropDownPicker from 'react-native-dropdown-picker';
import FavoriteSelectModal from '../../modal/FavoriteSelectModal';
import LectureApiController from '../../../services/apis/LectureApiController';
import UserDC from '../../../services/login/UserDC';
import EightCategoryViewHeader from './components/eightCategoryView/EightCategoryViewHeader';
import EigthCategoryViewSubCategories from './components/eightCategoryView/EightCategoryViewSubCategories';
import EightCategoryViewZone from './components/eightCategoryView/EightCategoryViewZone';
import EightCategoryViewFilter from './components/eightCategoryView/EightCategoryViewFilter';
import EightCategoryViewLecture from './components/eightCategoryView/EightCategoryViewLecture';
import {searchPriceList, sortOrderList} from '@/utils/commonParam';

export default class EightCategoryView extends React.Component<
  {},
  {
    keyword: string;
    data: any;
    zoneModalVisible: boolean;
    filterModalVisible: boolean;
    filterCondition: string;
    selectedSubZone: string;

    favorites: number[];
    favoriteModalVisible: boolean;
    dropDowns: number[];

    mainCategory: string;

    categoryIdList: number[];
    refreshing: boolean;
    page: number;
    heartClickedLecture: any;

    searchPriceCondition: string;
  }
> {
  constructor() {
    super();
    this.state = {
      keyword: '',
      data: [],
      dropDowns: [],
      zoneModalVisible: false,
      filterModalVisible: false,
      filterCondition: '',
      selectedSubZone: '',
      favorites: [],
      favoriteModalVisible: false,
      mainCategory: '액티비티',

      categoryIdList: Category.getSubCategoryList('액티비티').map(
        item => +item.id,
      ),

      refreshing: false,
      page: 0,

      searchPriceCondition: '',
    };
  }

  componentDidMount() {
    this.setState(
      {
        // data: lectureApi({}).lectureList,
        mainCategory: this.props.route.params.main,
        categoryIdList: Category.getSubCategoryList(
          this.props.route.params.main,
        ).map(item => +item.id),
      },
      () => this.getLectureListApi(),
    );
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

  private toggleFavorite = (id: number) => {
    const updatedFavorites = this.state.favorites;
    if (updatedFavorites.includes(id)) {
      updatedFavorites.splice(updatedFavorites.indexOf(id), 1);
    } else {
      updatedFavorites.push(id);
    }

    this.setState({favorites: updatedFavorites});
  };

  private makeSortParam() {
    let sortParam: string = '';
    sortOrderList.forEach(item => {
      if (item[0] === this.state.filterCondition[0]) {
        sortParam = item[1];
      }
    });

    return sortParam;
  }

  private async getLectureListApi() {
    const params = {
      page: this.state.page ? this.state.page : 0,
      size: 10,
      zoneId: this.state.selectedSubZone
        ? Zone.getId(this.state.selectedSubZone)
        : this.state.selectedSubZone,
      sort: sortOrderList[this.state.filterCondition],
      categoryIdList: this.state.categoryIdList,
      searchPriceCondition: searchPriceList[this.state.searchPriceCondition],
    };
    try {
      const res = await LectureApiController.getLectureList(params);

      this.setState({
        data:
          this.state.page === 0
            ? res.data.content
            : [...this.state.data, ...res.data.content],
        refreshing: false,
      });
    } catch (e) {
      console.error(e);
    }
  }

  private onSelectSubCategory(id: number) {
    if (
      this.state.categoryIdList.length === 1 &&
      this.state.categoryIdList[0] === id
    ) {
      this.setState(
        {
          categoryIdList: Category.getSubCategoryList(
            this.state.mainCategory,
          ).map(item => +item.id),
          page: 0,
        },
        () => {
          this.getLectureListApi();
        },
      );
    }

    if (
      this.state.categoryIdList.length === 1 &&
      this.state.categoryIdList[0] !== id
    ) {
      this.setState({categoryIdList: [id], page: 0}, () => {
        this.getLectureListApi();
      });
    }

    if (this.state.categoryIdList.length > 1) {
      this.setState({categoryIdList: [id], page: 0}, () => {
        this.getLectureListApi();
      });
    }
  }

  private LoadMoreLecture() {
    this.setState({page: this.state.page + 1}, () => {
      this.getLectureListApi();
    });
  }

  render() {
    return (
      <View style={{flex: 1, backgroundColor: '#FFF'}}>
        <EightCategoryViewHeader
          navigation={this.props.navigation}
          mainCategory={this.state.mainCategory}
        />

        <View style={{backgroundColor: '#FFF', flex: 1}}>
          <EigthCategoryViewSubCategories
            mainCategory={this.state.mainCategory}
            categoryIdList={this.state.categoryIdList}
            onPressAll={() => {
              this.setState(
                {
                  categoryIdList: Category.getSubCategoryList(
                    this.state.mainCategory,
                  ).map(item => +item.id),
                },
                () => {
                  this.getLectureListApi();
                },
              );
            }}
            setSubCategory={(id: number) => this.onSelectSubCategory(id)}
          />

          <View style={{flexDirection: 'row', marginLeft: 28, marginTop: 20}}>
            <EightCategoryViewZone
              selectedSubZone={this.state.selectedSubZone}
              setParentSubZone={(sub: string) => {
                this.setState({selectedSubZone: sub}, () => {
                  this.getLectureListApi();
                });
              }}
            />

            <EightCategoryViewFilter
              filterCondition={this.state.filterCondition}
              searchPriceCondition={this.state.searchPriceCondition}
              setFilterCondition={(filterCondition: string) => {
                this.setState({filterCondition}, () => {
                  this.getLectureListApi();
                });
              }}
              setSearchPriceCondition={(searchPriceCondition: string) => {
                this.setState({searchPriceCondition}, () => {
                  this.getLectureListApi();
                });
              }}
            />
          </View>

          <FlatList
            style={styles.list}
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
                // <EightCategoryViewLecture
                //   navigation={this.props.navigation}
                //   lectureInfo={item}
                //   setFavorite={() => {
                //     this.setState({data: []}, () => {
                //       this.getLectureListApi();
                //     });
                //   }}
                // />
                <View style={styles.card} key={item.lectureId}>
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
                              <Icon
                                name="heart-outline"
                                size={30}
                                color="#FFF"
                              />
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
                            this.toggleFavorite(item.lectureId);
                            this.setState({
                              heartClickedLecture: item,
                            });
                            this.setState({
                              favoriteModalVisible:
                                !this.state.favoriteModalVisible,
                            });
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
                        icon: () => (
                          <Text style={{fontSize: 12}}>4인 이상</Text>
                        ),
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
            refreshing={this.state.refreshing}
            onRefresh={() =>
              this.setState({page: 0}, () => {
                this.getLectureListApi();
              })
            }
            onEndReachedThreshold={0}
            onEndReached={() => {
              this.LoadMoreLecture();
            }}
          />
        </View>

        <View>
          <FavoriteSelectModal
            modalVisible={this.state.favoriteModalVisible}
            lecture={
              this.state.heartClickedLecture
                ? this.state.heartClickedLecture
                : null
            }
            setModalVisible={(visible: boolean) => {
              this.setState({favoriteModalVisible: visible});
            }}
            setFavorite={() => {
              this.setState({data: []}, () => {
                this.getLectureListApi();
              });
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
    marginHorizontal: 10,
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
