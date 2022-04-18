import React from 'react';
import {StyleSheet, FlatList, Alert} from 'react-native';
import {View, Text} from 'react-native';
import {searchPriceList, sortOrderList} from '../../../utils/commonParam';
import Zone from '../../../utils/Zone';
import lectureApi from '../../../services/apis/lectureApi';
import FavoriteSelectModal from '../../modal/FavoriteSelectModal';
import LectureApiController from '../../../services/apis/LectureApiController';
import UserDC from '../../../services/login/UserDC';
import LectureSearchResultViewHeader from './components/lectureSearchResultView/LectureSearchResultViewHeader';
import LectureSearchResultViewFilter from './components/lectureSearchResultView/LectureSearchResultViewFilter';
import LectureSearchResultViewLecture from './components/lectureSearchResultView/LectureSearchResultViewLecture';

interface LectureSearchResultViewStates {
  keyword: string;
  data: any;
  zoneModalVisible: boolean;
  filterModalVisible: boolean;
  filterCondition: string;
  selectedSubZone: string;

  favorites: number[];
  favoriteModalVisible: boolean;
  dropDowns: number[];

  totalElements: number;
  page: number;
  refreshing: boolean;

  heartClickedLecture: any;

  categoryIdList: number[];

  selectedPrice: any;

  searchPriceCondition: string;
}

export default class LectureSearchResultView extends React.Component<
  {},
  LectureSearchResultViewStates
> {
  constructor(props: any) {
    super(props);
    this.state = {
      keyword: '',
      data: lectureApi({}).lectureList,
      zoneModalVisible: false,

      filterModalVisible: false,
      filterCondition: '',

      selectedSubZone: '',

      favorites: [],
      favoriteModalVisible: false,
      dropDowns: [],

      totalElements: 0,
      page: 0,
      refreshing: false,

      heartClickedLecture: null,
      categoryIdList: [],
      selectedPrice: {},

      searchPriceCondition: '',
    };
  }

  componentDidMount() {
    this.setState(
      {
        keyword: this.props.route.params.keyword,
        selectedSubZone: this.props.route.params.selectedSubZone,
        categoryIdList: this.props.route.params.categoryIdList,
      },
      () => {
        this.getLectureListApi();
      },
    );
  }

  private checkForm(params: any) {
    if (params.keyword && params.keyword.length <= 0) {
      Alert.alert('검색할 때 공백 문자를 제외하고 1글자 이상 입력해주세요.');
      return false;
    }
    return true;
  }

  private async getLectureListApi() {
    const params = {
      keyword: this.state.keyword,
      page: this.state.page ? this.state.page : 0,
      size: 10,
      zoneId: this.state.selectedSubZone
        ? Zone.getId(this.state.selectedSubZone)
        : this.state.selectedSubZone,
      sort: sortOrderList[this.state.filterCondition],
      categoryIdList: this.state.categoryIdList,
      searchPriceCondition: searchPriceList[this.state.searchPriceCondition],
    };

    if (!this.checkForm(params)) {
      return;
    }

    try {
      const res = await LectureApiController.getLectureList(params);
      let selectedPrice = this.state.page === 0 ? {} : this.state.selectedPrice;

      res.data.content.forEach(lecture => {
        selectedPrice[lecture.lectureId] = 'one';
      });

      this.setState({
        totalElements: res.data.totalElements,
        data:
          this.state.page === 0
            ? res.data.content
            : [...this.state.data, ...res.data.content],
        refreshing: false,
        selectedPrice,
      });
    } catch (e) {
      console.error(e);
    }
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

  private LoadMoreLecture() {
    this.setState({page: this.state.page + 1}, () => {
      this.getLectureListApi();
    });
  }

  render() {
    return (
      <View style={{flex: 1, backgroundColor: '#FFF'}}>
        <LectureSearchResultViewHeader
          keyword={this.state.keyword}
          setKeyword={(keyword: string) => this.setState({keyword})}
          onSubmit={() => {
            this.setState({page: 0, data: []}, () => {
              this.getLectureListApi();
            });
          }}
          navigation={this.props.navigation}
        />
        <View style={{backgroundColor: '#FFF', flex: 1}}>
          <View style={{flexDirection: 'row', marginLeft: 28, marginTop: 20}}>
            <Text
              style={{
                fontSize: 14,
                fontWeight: 'bold',
              }}>{`${this.state.totalElements}개`}</Text>
            <Text style={{fontSize: 14}}>의 검색 결과</Text>
          </View>
          <LectureSearchResultViewFilter
            selectedSubZone={this.state.selectedSubZone}
            filterCondition={this.state.filterCondition}
            searchPriceCondition={this.state.searchPriceCondition}
            setSubZone={(sub: string) => {
              this.setState({selectedSubZone: sub, page: 0, data: []}, () => {
                this.getLectureListApi();
              });
            }}
            setFilterCondition={(filterCondition: string) => {
              this.setState(
                {filterCondition: filterCondition, page: 0, data: []},
                () => {
                  this.getLectureListApi();
                },
              );
            }}
            setSearchPriceCondition={(searchPriceCondition: string) => {
              this.setState({searchPriceCondition}, () =>
                this.getLectureListApi(),
              );
            }}
          />

          <FlatList
            style={styles.list}
            contentContainerStyle={styles.listContainer}
            showsVerticalScrollIndicator={false}
            showsHorizontalScrollIndicator={false}
            columnWrapperStyle={{justifyContent: 'space-between'}}
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
                <LectureSearchResultViewLecture
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
              this.setState({page: 0, data: []}, () => {
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
    marginHorizontal: 20,
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
