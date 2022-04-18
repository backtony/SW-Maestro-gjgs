import React from 'react';
import {View} from 'react-native';
import {ScrollView} from 'react-native-gesture-handler';
import HomeViewAdBox from './components/HomeViewAdBox';
import CategorySelectModal from '../modal/CategorySelectModal';
import MultiZoneSelectModal from '../modal/MultiZoneSelectModal';
import Zone from '../../utils/Zone';
import DirectorModal from '../modal/DirectorModal';
import LectureApiController from '../../services/apis/LectureApiController';
import Category from '../../utils/Category';
import FavoriteSelectModal from '../modal/FavoriteSelectModal';
import HomeFavoriteLocalLectureList from './components/HomeFavoriteLocalLectureList';
import HomeRecommendLectureList from './components/HomeRecommendLectureList';
import HomePopularLectureList from './components/HomePopularLectureList';
import HomeNewLectureList from './components/HomeNewLectureList';
import HomeViewHeader from './components/HomeViewHeader';
import HomeViewFindFriendButton from './components/HomeViewFindFriendButton';
import HomeViewCategoryButtons from './components/HomeViewCategoryButtons';
import HomeViewFavoriteLocalHeader from './components/HomeViewFavoriteLocalHeader';
import HomeViewFavoriteLocalFilter from './components/HomeViewFavoriteLocalFilter';
import HomeViewRecommendLectureHeader from './components/HomeViewRecommendLectureHeader';
import HomeViewDirectorChangeButton from './components/HomeViewDirectorChangeButton';
import UserDC from '@/services/login/UserDC';

interface HomeViewStates {
  zoneModalVisible: boolean;
  selectedSubZone: string[];
  categoryModalVisible: boolean;
  selectedSubCategory: string[];
  favoriteModalVisible: boolean;

  favoriteLectureData: any;
  recommendLectureData: any;
  popularLectureData: any;
  newLectureData: any;
  conceptLectureData: any;

  favorites: number[];
  dropDowns: number[];

  directorModalVisible: boolean;

  categoryIdList: number[];

  heartClickedLecture: any;

  login: boolean;
}

class HomeView extends React.Component<{}, HomeViewStates> {
  constructor(props: any) {
    super(props);
    this.state = {
      zoneModalVisible: false,
      selectedSubZone: [],
      categoryModalVisible: false,
      selectedSubCategory: [],
      favoriteModalVisible: false,

      favoriteLectureData: [],
      recommendLectureData: [],
      popularLectureData: [],
      newLectureData: [],
      conceptLectureData: [],
      favorites: [],
      dropDowns: [],

      directorModalVisible: false,
      categoryIdList: [],
      heartClickedLecture: null,

      login: false,
    };
  }

  componentDidMount() {
    this.getUser();
  }

  private async getUser() {
    const user = UserDC.getUser();
    if (user !== undefined) {
      console.log('koko');
      this.setState({login: true}, () =>
        console.log('login2: ', this.state.login),
      );
    }
  }

  private async getLectureListApi() {
    const params = {
      page: 0,
      size: 4,
      zoneId:
        this.state.selectedSubZone.length > 0
          ? Zone.getId(this.state.selectedSubZone[0])
          : '',
      categoryIdList: this.state.categoryIdList,
    };
    try {
      const res = await LectureApiController.getLectureList(params);

      this.setState({
        favoriteLectureData: res.data.content.slice(0, 4),
      });
    } catch (e) {
      console.error(e);
    }
  }

  render() {
    return (
      <View
        style={{
          flex: 1,
          backgroundColor: '#FFF',
        }}>
        <HomeViewHeader navigation={this.props.navigation} />
        <ScrollView
          style={{marginHorizontal: 20}}
          showsVerticalScrollIndicator={false}
          showsHorizontalScrollIndicator={false}>
          <ScrollView horizontal={true}>
            <HomeViewAdBox color={'#ff5f6d'} />
            <HomeViewAdBox color={'#396afc'} />
          </ScrollView>
          <HomeViewFindFriendButton navigation={this.props.navigation} />
          <HomeViewCategoryButtons navigation={this.props.navigation} />

          <HomeViewFavoriteLocalHeader navigation={this.props.navigation} />

          <HomeViewFavoriteLocalFilter
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
          <HomeFavoriteLocalLectureList
            navigation={this.props.navigation}
            selectedSubZone={this.state.selectedSubZone}
            categoryIdList={this.state.categoryIdList}
          />

          {this.state.login && (
            <View>
              <HomeViewRecommendLectureHeader
                navigation={this.props.navigation}
                title={'추천 클래스'}
                imgUrl={
                  'https://cdn.zeplin.io/60f22448a6ebf90ffb53be83/assets/8CC05DE5-200B-4255-8D62-9629037825D2.png'
                }
              />

              <HomeRecommendLectureList navigation={this.props.navigation} />
            </View>
          )}

          <HomeViewRecommendLectureHeader
            navigation={this.props.navigation}
            title={'인기 클래스'}
            imgUrl={
              'https://cdn.zeplin.io/60f22448a6ebf90ffb53be83/assets/C6299747-7E0A-4980-84F1-1C6A16C84262.png'
            }
          />

          <HomePopularLectureList navigation={this.props.navigation} />

          <HomeViewRecommendLectureHeader
            navigation={this.props.navigation}
            title={'신규 클래스'}
            imgUrl={
              'https://cdn.zeplin.io/60f22448a6ebf90ffb53be83/assets/120F8348-8AA1-4E92-8E7F-E1114F04D742.png'
            }
          />

          <HomeNewLectureList navigation={this.props.navigation} />

          {/* <HomeViewRecommendLectureHeader
            navigation={this.props.navigation}
            title={'기획전'}
            imgUrl={
              'https://cdn.zeplin.io/60f22448a6ebf90ffb53be83/assets/536AC859-E4EA-4937-A0F9-97A3F083111E.png'
            }
          />

          <HomeConceptLectureList navigation={this.props.navigation} /> */}

          <HomeViewDirectorChangeButton
            onPress={() => {
              this.setState({directorModalVisible: true});
            }}
          />
          <View style={{height: 100}} />
        </ScrollView>

        <View>
          <CategorySelectModal
            modalVisible={this.state.categoryModalVisible}
            setModalVisible={(visible: boolean) => {
              this.setState({categoryModalVisible: visible});
            }}
            selectedCategoryList={this.state.selectedSubCategory}
            setSubCategory={(sub: string[]) => {
              this.setState(
                {
                  categoryIdList: sub.map(ss => Category.getIdWithSub(ss)),
                  selectedSubCategory: sub,
                  favoriteLectureData: [],
                },
                () => {
                  console.log('11');
                  // this.getLectureListApi();
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
              this.setState(
                {selectedSubZone: sub, favoriteLectureData: []},
                () => {
                  console.log('22');
                  // this.getLectureListApi();
                },
              );
            }}
          />
        </View>
        <View>
          <DirectorModal
            modalVisible={this.state.directorModalVisible}
            setModalVisible={(visible: boolean) => {
              this.setState({directorModalVisible: visible});
            }}
            setParentSubZone={(sub: string) => {
              // this.setState({profileSubZone: sub});
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
              this.setState({favoriteLectureData: []}, () => {
                this.getLectureListApi();
              });
            }}
          />
        </View>
      </View>
    );
  }
}

export default HomeView;
