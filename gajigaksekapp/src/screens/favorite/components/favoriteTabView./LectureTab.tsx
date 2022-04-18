import React from 'react';
import {StyleSheet, View} from 'react-native';
import FavoriteApiController from '../../../../services/apis/FavoriteApiController';
import UserDC from '../../../../services/login/UserDC';
import LectureList from '@/components/LectureList';

interface LectureTabProps {
  navigation: any;
}

interface LectureTabStates {
  data: any;
  filterModalVisible: boolean;
  filterCondition: string[];
  refreshing: boolean;
}

export default class LectureTab extends React.Component<
  LectureTabProps,
  LectureTabStates
> {
  constructor(props: any) {
    super(props);
    this.state = {
      data: [],
      filterModalVisible: false,
      filterCondition: [],
      refreshing: false,
    };
  }

  componentDidMount() {
    this.getFavoriteLecture();
  }

  private async getFavoriteLecture() {
    if (UserDC.isLogout()) {
      return;
    }
    try {
      const res = await FavoriteApiController.getLectureIndividual({});

      this.setState({data: res.data.lectureMemberDtoList});
    } catch (e) {
      console.error(e);
    }
  }

  private async deleteFavoriteIndividual(lectureId: number) {
    try {
      await FavoriteApiController.deleteFavoriteIndividual({
        lectureId: lectureId,
      });
      this.getFavoriteLecture();
    } catch (e) {
      console.error(e);
    }
  }

  render() {
    return (
      <View style={this.styles.container}>
        <View style={this.styles.innerContainer}>
          <LectureList
            navigation={this.props.navigation}
            data={this.state.data}
            refreshParent={() => this.getFavoriteLecture()}
            deleteLecture={(lectureId: number) =>
              this.deleteFavoriteIndividual(lectureId)
            }
          />
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    container: {flex: 1, backgroundColor: '#FFF'},
    innerContainer: {backgroundColor: '#FFF', flex: 1, marginTop: 10},
  });
}
