import MypageApiController from '@/services/apis/MypageApiController';
import React from 'react';
import {StyleSheet, View} from 'react-native';
import {FlatList} from 'react-native-gesture-handler';
import DirectorReviewTabReviewBlock from './components/DirectorReviewTabReviewBlock';

interface DirectorReviewInfo {
  score: number;
  reviewImageFileUrl: string;
  text: string;
  replyText: string;
  nickname: string;
  profileImageFileUrl: string;
  reviewId: number;
}

interface DirectorReviewTabProps {
  navigation: any;
  directorId: number;
}

interface DirectorReviewTabStates {
  refreshing: boolean;
  data: DirectorReviewInfo[];
}

export default class DirectorReviewTab extends React.Component<
  DirectorReviewTabProps,
  DirectorReviewTabStates
> {
  constructor(props: any) {
    super(props);
    this.state = {
      refreshing: false,
      data: [
        {
          reviewImageFileUrl:
            'https://images.unsplash.com/photo-1632769096411-a3c1e3aa1c3c?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=688&q=80',
          text: '남들이 하지 않는 것에 관심이 많습니다. 새로운 길을 가는 것은 즐겁고 흥미진진하지만 여러 위험도 도사리고 있습니다.',
          replyText:
            '리뷰를 남겨주세서 감사합니다! 다음 방문을 기대하겠습니다!',
          score: 3,
          nickname: '이지원',
          profileImageFileUrl:
            'https://images.unsplash.com/photo-1632769096411-a3c1e3aa1c3c?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=688&q=80',
          reviewId: 0,
        },
      ],
    };
    this.getDirectorReviews(props.directorId);
  }

  private async getDirectorReviews(directorId: number) {
    try {
      const res = await MypageApiController.getDirectorReviews(directorId);
      this.setState({data: res?.data.content});
    } catch (e) {}
  }
  render() {
    return (
      <View>
        <FlatList
          style={this.styles.list}
          contentContainerStyle={this.styles.listContainer}
          data={this.state.data}
          numColumns={2}
          keyExtractor={item => {
            return item.id;
          }}
          ItemSeparatorComponent={() => {
            return <View style={this.styles.separator} />;
          }}
          renderItem={post => {
            const item: DirectorReviewInfo = post.item;
            return <DirectorReviewTabReviewBlock lecture={item} />;
          }}
          refreshing={this.state.refreshing}
          onRefresh={() => {}}
        />
      </View>
    );
  }

  private styles = StyleSheet.create({
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
  });
}
