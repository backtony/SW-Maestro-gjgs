import React from 'react';
import {StyleSheet, FlatList} from 'react-native';
import {View} from 'react-native';
import Category from '../../../utils/Category';
import {searchPriceList, sortOrderList} from '../../../utils/commonParam';
import FilterModal from '../../modal/FilterModal';
import Zone from '../../../utils/Zone';
import FavoriteSelectModal from '../../modal/FavoriteSelectModal';
import CategroySelectModal from '../../modal/CategorySelectModal';
import MultiZoneSelectModal from '../../modal/MultiZoneSelectModal';
import LectureApiController from '../../../services/apis/LectureApiController';
import UserDC from '../../../services/login/UserDC';
import FavoriteZoneLectureViewHeader from './components/FavoriteZoneLectureViewHeader';
import FavoriteZoneLectureViewFilter from './components/FavoriteZoneLectureViewFilter';
import FavoriteZoneLectureViewLecture from './components/FavoriteZoneLectureViewLecture';

interface FavoriteZoneLectureViewStates {
  keyword: string;
  data: any;
  zoneModalVisible: boolean;
  filterModalVisible: boolean;
  filterCondition: string;
  selectedSubZone: string[];

  favorites: number[];
  favoriteModalVisible: boolean;
  dropDowns: number[];

  categoryModalVisible: boolean;
  selectedSubCategory: string[];
  page: number;
  refreshing: boolean;
  heartClickedLecture: any;

  searchPriceCondition: string;
}

export default class FavoriteZoneLectureView extends React.Component<
  {},
  FavoriteZoneLectureViewStates
> {
  constructor(props: any) {
    super(props);
    this.state = {
      keyword: '',
      data: [],
      dropDowns: [],
      zoneModalVisible: false,
      filterModalVisible: false,
      filterCondition: '',
      selectedSubZone: [],
      favorites: [],
      favoriteModalVisible: false,
      categoryModalVisible: false,
      selectedSubCategory: [],
      page: 0,
      refreshing: false,
      searchPriceCondition: '',
    };
  }

  componentDidMount() {
    this.getLectureListApi();
  }

  private toggleFavorite = (id: number) => {
    const updatedFavorites = this.state.favorites;
    if (updatedFavorites.includes(id)) {
      updatedFavorites.splice(updatedFavorites.indexOf(id), 1);
    } else {
      updatedFavorites.push(id);
    }

    this.setState({favorites: updatedFavorites});
  };

  private async getLectureListApi() {
    const params = {
      page: this.state.page ? this.state.page : 0,
      size: 10,
      zoneId:
        this.state.selectedSubZone.length > 0
          ? Zone.getId(this.state.selectedSubZone[0])
          : '',
      sort: sortOrderList[this.state.filterCondition],
      categoryIdList: this.state.selectedSubCategory.map(sub =>
        Category.getIdWithSub(sub),
      ),
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

  private LoadMoreLecture() {
    this.setState({page: this.state.page + 1}, () => {
      this.getLectureListApi();
    });
  }

  render() {
    return (
      <View style={{flex: 1, backgroundColor: '#FFF'}}>
        <FavoriteZoneLectureViewHeader
          onPressBack={() => this.props.navigation.goBack()}
          onPressFilter={() => {
            this.setState({
              filterModalVisible: !this.state.filterModalVisible,
            });
          }}
        />

        <View style={{backgroundColor: '#FFF', flex: 1}}>
          <FavoriteZoneLectureViewFilter
            selectedSubZone={this.state.selectedSubZone}
            selectedSubCategory={this.state.selectedSubCategory}
            setZoneModalVisible={() =>
              this.setState({zoneModalVisible: !this.state.zoneModalVisible})
            }
            setCategoryModalVisible={() =>
              this.setState({
                categoryModalVisible: !this.state.categoryModalVisible,
              })
            }
          />

          <FlatList
            style={styles.list}
            contentContainerStyle={styles.listContainer}
            data={this.state.data}
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
                <FavoriteZoneLectureViewLecture
                  navigation={this.props.navigation}
                  lecture={item}
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
                      favoriteModalVisible: !this.state.favoriteModalVisible,
                    });
                  }}
                />
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
              this.setState({page: 0, data: []}, () => {
                this.getLectureListApi();
              });
            }}
          />
        </View>
        <View>
          <CategroySelectModal
            modalVisible={this.state.categoryModalVisible}
            setModalVisible={(visible: boolean) => {
              this.setState({categoryModalVisible: visible});
            }}
            selectedCategoryList={this.state.selectedSubCategory}
            setSubCategory={(sub: string[]) => {
              this.setState(
                {selectedSubCategory: sub, page: 0, data: []},
                () => {
                  this.getLectureListApi();
                },
              );
            }}
          />
        </View>
        <View>
          <MultiZoneSelectModal
            modalVisible={this.state.zoneModalVisible}
            setModalVisible={(visible: boolean) => {
              this.setState({zoneModalVisible: visible});
            }}
            setParentSubZone={(sub: string[]) => {
              this.setState({selectedSubZone: sub, page: 0, data: []}, () => {
                this.getLectureListApi();
              });
            }}
          />
        </View>
        <View>
          <FilterModal
            modalVisible={this.state.filterModalVisible}
            filterCondition={this.state.filterCondition}
            searchPriceCondition={this.state.searchPriceCondition}
            setModalVisible={(visible: boolean) => {
              this.setState({filterModalVisible: visible});
            }}
            setFilterCondition={(filterCondition: string) => {
              this.setState({filterCondition: filterCondition}, () => {
                this.getLectureListApi();
              });
            }}
            setSearchPriceCondition={(searchPriceCondition: string) =>
              this.setState({searchPriceCondition}, () =>
                this.getLectureListApi(),
              )
            }
          />
        </View>
      </View>
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
    paddingHorizontal: 10,
  },
  listContainer: {
    marginHorizontal: 10,
    marginTop: 10,
  },
  separator: {
    marginTop: 10,
  },
  /******** card **************/
  card: {
    backgroundColor: 'white',
    width: '45%',
    marginHorizontal: 10,
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
});
