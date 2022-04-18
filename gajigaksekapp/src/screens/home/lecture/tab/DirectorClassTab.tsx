import FavoriteSelectModal from '@/screens/modal/FavoriteSelectModal';
import LectureApiController from '@/services/apis/LectureApiController';
import UserDC from '@/services/login/UserDC';
import React from 'react';
import {FlatList, StyleSheet, View} from 'react-native';
import DirectorClassTabLectureBlock from './components/DirectorClassTabLectureBlock';

interface DirectorClassTabProps {
  navigation: any;
  directorId: number;
}

interface DirectorClassTabStates {
  data: any;
  filterModalVisible: boolean;
  filterCondition: string[];
  refreshing: boolean;
  dropDowns: number[];
  favorites: number[];
  heartClickedLecture: any;
  favoriteModalVisible: boolean;
}

export default class DirectorClassTab extends React.Component<
  DirectorClassTabProps,
  DirectorClassTabStates
> {
  constructor(props: any) {
    super(props);
    this.state = {
      data: [],
      filterModalVisible: false,
      filterCondition: [],
      refreshing: false,
      dropDowns: [],
      favorites: [],
      heartClickedLecture: null,
      favoriteModalVisible: false,
    };
  }

  componentDidMount() {
    this.getDirectorLectures(this.props.directorId);
  }

  private async getDirectorLectures(directorId: number) {
    try {
      const res = await LectureApiController.getDirectorLectures(directorId);
      this.setState({data: res?.data.content});
    } catch (e) {}
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
      <View>
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
              <DirectorClassTabLectureBlock
                navigation={this.props.navigation}
                lecture={item}
                onPressHeart={() => {
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
          onRefresh={() => {}}
          onEndReachedThreshold={0}
          onEndReached={() => {
            // this.LoadMoreLecture();
          }}
        />
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
                this.getDirectorLectures(this.props.directorId);
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
  },
  separator: {
    marginTop: 10,
  },
  /******** card **************/
  card: {
    // marginVertical: 8,
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
});
