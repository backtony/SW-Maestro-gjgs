import MypageApiController from '@/services/apis/MypageApiController';
import {STATUSBAR_HEIGHT} from '@/utils/commonParam';
import React from 'react';
import {Image, StyleSheet, Text, View} from 'react-native';
import {Button} from 'react-native-elements';
import {FlatList} from 'react-native-gesture-handler';
import Icon from 'react-native-vector-icons/Ionicons';

export interface MyReviewInfo {
  lectureId: number;
  lectureTitle: string;
  reviewImageFileUrl: string;
  text: string;
  replyText: string;
  score: number;
}

interface MyReviewViewProps {
  navigation: any;
}

interface MyReviewViewStates {
  refreshing: boolean;
  data: MyReviewInfo[];
}

export default class MyReviewView extends React.Component<
  MyReviewViewProps,
  MyReviewViewStates
> {
  constructor(props: any) {
    super(props);
    this.state = {
      refreshing: false,
      data: [
        {
          lectureId: 0,
          lectureTitle: 'sksksksk',
          reviewImageFileUrl:
            'https://images.unsplash.com/photo-1632769096411-a3c1e3aa1c3c?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=688&q=80',
          text: '남들이 하지 않는 것에 관심이 많습니다. 새로운 길을 가는 것은 즐겁고 흥미진진하지만 여러 위험도 도사리고 있습니다.',
          replyText: 'asdf',
          score: 3,
        },
      ],
    };
    this.getMyReview();
  }

  private async getMyReview() {
    try {
      const res = await MypageApiController.getMyReview();
      this.setState({data: res.data.content});
    } catch (e) {}
  }

  render() {
    return (
      <View style={styles.container}>
        <View
          style={{
            marginTop: STATUSBAR_HEIGHT,
            height: 60,
            width: '100%',
            flexDirection: 'row',
            justifyContent: 'flex-start',
            alignItems: 'center',
          }}>
          <Button
            icon={<Icon name="chevron-back" size={24} color="#1d1d1f" />}
            buttonStyle={{backgroundColor: 'white'}}
            onPress={() => this.props.navigation.goBack()}
          />
          <Text
            style={{
              // width: 93,
              // height: 20,
              fontFamily: 'NotoSansCJKkr-Bold',
              fontSize: 20,
              color: '#1d1d1f',
            }}>
            나의 리뷰
          </Text>
        </View>
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
            const item: MyReviewInfo = post.item;
            return (
              <View
                style={{
                  flex: 1,
                  padding: 20,
                  backgroundColor: '#fafafb',
                  borderRadius: 6,
                }}>
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
                  {item.lectureTitle}
                </Text>
                <Text
                  style={{
                    color: '#4f6cff',
                    fontFamily: 'NotoSansCJKkr-Bold',
                    fontSize: 16,
                  }}>{`★ ${item.score}`}</Text>
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
                    width: 100,
                    height: 100,
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

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#FFF',
  },
  list: {
    paddingHorizontal: 10,
  },
  listContainer: {
    marginHorizontal: 20,
    marginTop: 20,
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
