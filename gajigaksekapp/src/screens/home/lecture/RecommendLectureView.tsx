import React from 'react';
import {StyleSheet, FlatList} from 'react-native';
import {View} from 'react-native';
import FilterModal from '../../modal/FilterModal';
import FavoriteSelectModal from '../../modal/FavoriteSelectModal';
import RecommendLectureViewHeader from './components/recommendLectureView/RecommendLectureViewHeader';
import RecommendLectureBlock from './components/recommendLectureView/RecommendLectureBlock';
import LectureApiController from '@/services/apis/LectureApiController';

interface RecommendLectureViewStates {
  keyword: string;
  data: any;
  filterModalVisible: boolean;
  filterCondition: string;

  favorites: number[];
  favoriteModalVisible: boolean;
  dropDowns: number[];

  title: string;

  searchPriceCondition: string;
}

export default class RecommendLectureView extends React.Component<
  {},
  RecommendLectureViewStates
> {
  constructor(props: any) {
    super(props);
    this.state = {
      keyword: '',
      data: [],
      dropDowns: [],
      filterModalVisible: false,
      filterCondition: '',
      favorites: [],
      favoriteModalVisible: false,
      title: '추천 클래스',
      searchPriceCondition: '',
    };
    this.getLectureListApi();
  }

  componentDidMount() {
    this.setState({
      title: this.props.route.params.title,
    });
  }

  private async getLectureListApi() {
    const params = {
      keyword: '강남',
      size: 10,
    };

    try {
      const res = await LectureApiController.getLectureList(params);

      this.setState({
        data: res.data.content,
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

  render() {
    return (
      <View style={{flex: 1, backgroundColor: '#FFF'}}>
        <RecommendLectureViewHeader
          navigation={this.props.navigation}
          title={this.state.title}
          onPress={() => {
            this.setState({
              filterModalVisible: !this.state.filterModalVisible,
            });
          }}
        />

        <View style={{backgroundColor: '#FFF', flex: 1}}>
          <FlatList
            style={styles.list}
            contentContainerStyle={styles.listContainer}
            data={this.state.data}
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
                <RecommendLectureBlock
                  navigation={this.props.navigation}
                  lecture={item}
                  favorite={this.state.favorites.includes(item.lectureId)}
                  onPress={() => {
                    this.toggleFavorite(item.lectureId);
                    this.setState({
                      favoriteModalVisible: !this.state.favoriteModalVisible,
                    });
                  }}
                />
              );
            }}
          />
        </View>

        <View>
          <FavoriteSelectModal
            modalVisible={this.state.favoriteModalVisible}
            setModalVisible={(visible: boolean) => {
              this.setState({favoriteModalVisible: visible});
            }}
            setFavorite={() => {}}
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
              this.setState({filterCondition});
            }}
            setSearchPriceCondition={(searchPriceCondition: string) =>
              this.setState({searchPriceCondition})
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
