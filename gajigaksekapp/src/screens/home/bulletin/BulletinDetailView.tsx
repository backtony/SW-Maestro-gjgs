import * as React from 'react';
import {Text, View, ScrollView, StyleSheet, Alert} from 'react-native';
import lectureApi from '../../../services/apis/lectureApi';
import ShareModal from '../../modal/ShareModal';
import BulletinApiController from '../../../services/apis/BulletinApiController';
import TeamManageApiController from '../../../services/apis/TeamManageApiController';
import FavoriteApiController from '../../../services/apis/FavoriteApiController';
import BulletinDetailViewHeader from './components/bulletinDetailView/BulletinDetailViewHeader';
import BulletinDetailViewBlocks from './components/bulletinDetailView/BulletinDetailViewBlocks';
import BulletinDetailViewLecture from './components/bulletinDetailView/BulletinDetailViewLecture';
import BulletinDetailViewTextBlock from './components/bulletinDetailView/BulletinDetailViewTextBlock';
import BulletinDetailViewMembers from './components/bulletinDetailView/BulletinDetailViewMembers';
import BulletinDetailViewBottom from './components/bulletinDetailView/BulletinDetailViewBottom';

export default class BulletinDetailView extends React.Component<
  {},
  {
    team: any;
    data: any;
    dropDowns: number[];
    memberEdit: boolean;
    favorites: number[];
    favoriteModalVisible: boolean;
    bulletinFavorite: boolean;
    shareModalVisible: boolean;
    selectedLecture: any;

    myFavorite: boolean;
  }
> {
  constructor(props: any) {
    super(props);
    this.state = {
      team: null,
      data: null,
      dropDowns: [],
      memberEdit: false,
      favorites: [],
      favoriteModalVisible: false,
      bulletinFavorite: false,
      shareModalVisible: false,
      selectedLecture: {
        lectureId: 1,
        lectureTitle: '16년차 베이커리 장인의 케이크 클래스',
        priceFour: 20000,
        priceOne: 50000,
        priceThree: 30000,
        priceTwo: 40000,
        thumbnailImageUrl: 'https://via.placeholder.com/400x200/FFB6C1/000000',
        zoneId: 1,
      },
      myFavorite: false,
    };
  }
  componentDidMount() {
    this.setState({
      data: [lectureApi(null).lectureList[0]],
      myFavorite: this.props.route.params.myFavorite,
    });
    this.getBulletinDash(this.props.route.params.bulletinId);
  }

  private async getBulletinDash(bulletinId: number) {
    try {
      const res = await BulletinApiController.getBulletinDash({
        bulletinId: bulletinId,
      });
      res.data.bulletinsTeam.members = [
        res.data.bulletinsTeam.leader,
        ...res.data.bulletinsTeam.members,
      ];

      console.log('res:', res);
      this.setState({team: res.data});
    } catch (e) {
      console.error(e);
    }
  }

  private async postFavoriteBulletin(bulletinId: number) {
    try {
      await FavoriteApiController.postFavoriteBulletin({
        bulletinId,
      });
      this.setState({myFavorite: true});
    } catch (e) {
      console.error(e);
    }
  }

  private async deleteFavoriteBulletin(bulletinId: number) {
    try {
      await FavoriteApiController.deleteFavoriteBulletin({
        bulletinId,
      });
      this.setState({myFavorite: false});
    } catch (e) {
      console.error(e);
    }
  }

  private async postTeamApply() {
    try {
      await TeamManageApiController.postTeamApply({
        teamId: this.state.team.bulletinsTeam.teamId,
      });
      Alert.alert('참가신청이 완료되었습니다.');
    } catch (e) {
      console.error(e);
    }
  }

  render() {
    return (
      <View style={this.styles.container}>
        <BulletinDetailViewHeader
          bulletinId={this.props.route.params.bulletinId}
          navigation={this.props.navigation}
          team={this.state.team}
        />
        <ScrollView
          style={this.styles.scrollView}
          showsVerticalScrollIndicator={false}
          showsHorizontalScrollIndicator={false}>
          <Text style={this.styles.text1}>
            {this.state.team
              ? this.state.team.bulletinTitle
              : '마라톤 좋아하는 사람들 모여라'}
          </Text>
          <BulletinDetailViewBlocks team={this.state.team} />
          <BulletinDetailViewLecture
            team={this.state.team}
            navigation={this.props.navigation}
            getBulletinDash={() =>
              this.getBulletinDash(this.props.route.params.bulletinId)
            }
          />
          <BulletinDetailViewTextBlock team={this.state.team} />
          <BulletinDetailViewMembers
            team={this.state.team}
            navigation={this.props.navigation}
          />
        </ScrollView>

        <BulletinDetailViewBottom
          myFavorite={this.state.myFavorite}
          postFavoriteBulletin={() =>
            this.postFavoriteBulletin(this.props.route.params.bulletinId)
          }
          deleteFavoriteBulletin={() =>
            this.deleteFavoriteBulletin(this.props.route.params.bulletinId)
          }
          postTeamApply={() => this.postTeamApply()}
        />

        <View>
          <ShareModal
            text={'게시글을'}
            url={`gajigaksekapp://bulletin/${this.props.route.params.bulletinId}`}
            modalVisible={this.state.shareModalVisible}
            setModalVisible={(visible: boolean) => {
              this.setState({shareModalVisible: visible});
            }}
            setParentSubZone={(sub: string) => {
              // this.setState({profileSubZone: sub});
            }}
          />
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    container: {flex: 1, backgroundColor: '#FFF'},
    scrollView: {paddingHorizontal: 20},
    text1: {
      fontSize: 20,
      lineHeight: 24,
      fontFamily: 'NotoSansCJKkr-Bold',
      marginTop: 20,
      marginBottom: 10,
      color: '#070707',
    },
  });
}
