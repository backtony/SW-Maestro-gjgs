import React from 'react';
import {ScrollView} from 'react-native';
import {StyleSheet} from 'react-native';
import GroupDetailInfo from './GroupDetailInfo';
import GroupDetailMemberBlock from './GroupDetailMemberBlock';
import GroupDetailLectureList from './GroupDetailLectureList';
import GroupDetailFavoriteLectureList from './GroupDetailFavoriteLectureList';

interface GroupDetailScrollViewProps {
  navigation: any;
  team: any;
  setTeam: (team: any) => void;
}

export default class GroupDetailScrollView extends React.Component<
  GroupDetailScrollViewProps,
  {favoriteLectureList: any; categoryList: any}
> {
  constructor(props: any) {
    super(props);
    this.state = {favoriteLectureList: [], categoryList: []};
  }
  componentDidMount() {
    if (this.props.team) {
      this.setState({
        favoriteLectureList: this.props.team.favoriteLectureList,
        categoryList: this.props.team.categoryList,
      });
    }
  }

  componentDidUpdate() {
    if (this.props.team) {
      if (
        this.props.team.favoriteLectureList !== this.state.favoriteLectureList
      ) {
        console.log('cibal!! : ', this.props.team.favoriteLectureList);
        this.setState({
          favoriteLectureList: this.props.team.favoriteLectureList,
        });
      }

      if (this.props.team.categoryList !== this.state.categoryList) {
        this.setState({
          categoryList: this.props.team.categoryList,
        });
      }
    }
  }
  render() {
    return (
      <ScrollView
        style={this.styles.scrollView}
        showsVerticalScrollIndicator={false}
        showsHorizontalScrollIndicator={false}>
        <GroupDetailInfo team={this.props.team} />
        <GroupDetailMemberBlock
          navigation={this.props.navigation}
          team={this.props.team}
          setTeam={(team: any) => this.props.setTeam(team)}
        />
        <GroupDetailFavoriteLectureList
          navigation={this.props.navigation}
          data={this.state.favoriteLectureList}
          title={'그룹이 찜한 클래스'}
          imageSource={require('gajigaksekapp/src/asset/iconImage/freeCategory.png')}
        />
        <GroupDetailLectureList
          navigation={this.props.navigation}
          categoryIdList={this.state.categoryList}
          title={'추천 클래스'}
          imageSource={require('gajigaksekapp/src/asset/iconImage/goodjob.png')}
        />
      </ScrollView>
    );
  }
  private styles = StyleSheet.create({scrollView: {paddingHorizontal: 20}});
}
