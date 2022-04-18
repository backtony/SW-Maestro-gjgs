import LectureApiController from '@/services/apis/LectureApiController';
import React from 'react';
import {Image, SafeAreaView, StyleSheet, Text, View} from 'react-native';
import {FlatList} from 'react-native-gesture-handler';

interface ReviewInfo {
  score: number;
  reviewImageFileUrl: string;
  text: string;
  replyText: string;
  nickname: string;
  profileImageFileUrl: string;
  reviewId: number;
}

interface ReviewTabProps {
  navigation: any;
  lectureId: number;
}

interface ReviewTabStates {
  refreshing: boolean;
  data: ReviewInfo[];
}

export default class ReviewTab extends React.Component<
  ReviewTabProps,
  ReviewTabStates
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
          replyText: '좋은 리뷰를 남겨주셔서 감사합니다! 다음에 또 봐요~!',
          score: 3,
          nickname: '이지원',
          profileImageFileUrl:
            'https://images.unsplash.com/photo-1632769096411-a3c1e3aa1c3c?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=688&q=80',
          reviewId: 0,
        },
      ],
    };
    this.getLectureReviews(props.lectureId);
  }

  private async getLectureReviews(lectureId: number) {
    try {
      const res = await LectureApiController.getLectureReviews(lectureId);

      if (res?.data.content.length > 0) {
        console.log('res: ', res);
        this.setState({data: res?.data.content});
      }
    } catch (e) {}
  }

  render() {
    return (
      <SafeAreaView style={{flex: 1}}>
        <FlatList
          style={this.styles.list}
          ListHeaderComponent={() => <View />}
          ListFooterComponent={() => <View />}
          ListEmptyComponent={() => <View />}
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
            const item: ReviewInfo = post.item;
            return (
              <View
                style={{
                  flex: 1,
                  padding: 20,
                  backgroundColor: '#fafafb',
                  borderRadius: 6,
                }}>
                <View style={{flexDirection: 'row', marginBottom: 10}}>
                  <Image
                    source={{uri: item.profileImageFileUrl}}
                    style={{
                      width: 60,
                      height: 60,
                      borderWidth: 2,
                      borderRadius: 30,
                      borderColor: '#ffffff',
                      marginRight: 10,
                    }}
                  />
                  <View>
                    <Text
                      style={{
                        color: '#4f6cff',
                        fontFamily: 'NotoSansCJKkr-Bold',
                        fontSize: 16,
                      }}>{`★ ${item.score}`}</Text>
                    <Text
                      style={{
                        fontFamily: 'NotoSansCJKkr-Bold',
                        fontSize: 16,
                        fontWeight: 'bold',
                        fontStyle: 'normal',
                        lineHeight: 20,
                        letterSpacing: 0,
                        color: '#1d1d1f',
                      }}>
                      {item.nickname}
                    </Text>
                  </View>
                </View>
                <Text
                  style={{
                    fontFamily: 'NotoSansCJKkr-Regular',
                    fontSize: 12,
                    fontWeight: 'normal',
                    fontStyle: 'normal',
                    lineHeight: 18,
                    letterSpacing: 0,
                    color: '#1d1d1f',
                  }}>
                  {item.text}
                </Text>
                <Image
                  source={{uri: item.reviewImageFileUrl}}
                  style={{
                    width: 90,
                    height: 90,
                    borderRadius: 10,
                    marginTop: 10,
                  }}
                />
                {item.replyText && (
                  <View style={{marginTop: 20}}>
                    <Text
                      style={{
                        fontFamily: 'NotoSansCJKkr-Bold',
                        fontSize: 16,
                        fontWeight: 'bold',
                        fontStyle: 'normal',
                        lineHeight: 20,
                        letterSpacing: 0,
                        color: '#1d1d1f',
                      }}>
                      디렉터 답변 :
                    </Text>
                    <Text>{item.replyText}</Text>
                  </View>
                )}
              </View>
            );
          }}
          refreshing={this.state.refreshing}
          onRefresh={() => {}}
        />
      </SafeAreaView>
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
