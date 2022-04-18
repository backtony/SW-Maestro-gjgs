import LectureApiController from '@/services/apis/LectureApiController';
import React from 'react';
import {
  View,
  Text,
  ScrollView,
  Image,
  StyleSheet,
  TouchableOpacity,
} from 'react-native';
import UserDC from '../../../../services/login/UserDC';
import QuestionTabHeader from './components/QuestionTabHeader';
import QuestionTabWriteButton from './components/QuestionTabWriteButton';

export default class QuestionTab extends React.Component<
  {navigation: any; lectureId: number; lecture: any; refreshParent: () => void},
  {questionList: any[]}
> {
  constructor(props: any) {
    super(props);
    this.state = {questionList: []};
  }

  componentDidMount() {
    this.getQuestion(this.props.lectureId);
  }
  private timeLapse(time: number) {
    const minute = Math.floor((Date.now() - Date.parse(time)) / 1000 / 60);

    if (minute < 60) {
      return `${minute}분 전`;
    } else if (minute < 1440) {
      return `${Math.floor(minute / 60)}시간 전`;
    } else if (minute < 44640) {
      return `${Math.floor(minute / 1440)}달 전`;
    } else if (minute < 525600) {
      return `${Math.floor(minute / 44640)}년 전`;
    }

    return minute;
  }

  private async getQuestion(lectureId: number) {
    try {
      const res = await LectureApiController.getQuestion({lectureId});
      this.setState({questionList: res.data.content});
    } catch (e) {
      console.error(e);
    }
  }

  render() {
    return (
      <ScrollView style={{flex: 1, backgroundColor: 'white'}}>
        <QuestionTabHeader
          counts={this.state.questionList.length}
          onPress={() => {
            if (UserDC.isLogout()) {
              alert('로그인 해주세요.');
              return;
            }
            this.props.navigation.navigate('lectureQuestion', {
              lectureId: this.props.lectureId,
              lecture: this.props.lecture,
              questionList: this.state.questionList,
            });
          }}
        />
        {this.state.questionList.map(question => {
          return (
            <TouchableOpacity
              style={{
                flexDirection: 'row',
                alignItems: 'center',
                paddingVertical: 15,
                borderBottomWidth: 1,
                borderBottomColor: '#f5f5f7',
              }}
              onPress={() => {
                this.props.navigation.navigate('lectureQuestionDash', {
                  questionId: question.questionId,
                });
              }}>
              <Image
                style={this.styles.avatar}
                source={{
                  uri: question.questionerProfileImageUrl,
                }}
              />
              <View>
                <View style={{flexDirection: 'row'}}>
                  <Text
                    style={{
                      color: '#1d1d1f',
                      fontSize: 16,
                      lineHeight: 18,
                      fontFamily: 'NotoSansCJKkr-Bold',
                    }}>
                    {question.questionerNickname}
                  </Text>
                  <Text
                    style={{
                      color: '#8e8e8f',
                      fontSize: 12,
                      lineHeight: 14,
                      fontFamily: 'NotoSansCJKkr-Regular',
                      marginTop: 2,
                      marginLeft: 5,
                    }}>
                    {this.timeLapse(question.questionDate)}
                  </Text>
                </View>
                <Text numberOfLines={1} style={{width: '80%'}}>
                  {question.questionText}
                </Text>
              </View>
            </TouchableOpacity>
          );
        })}

        <QuestionTabWriteButton
          onPress={() => {
            if (UserDC.isLogout()) {
              alert('로그인 해주세요.');
              return;
            }
            this.props.navigation.navigate('lectureQuestionWrite', {
              lectureId: this.props.lectureId,
              refreshParent: () => this.props.refreshParent(),
            });
          }}
        />
      </ScrollView>
    );
  }

  private styles = StyleSheet.create({
    flatList: {
      width: '100%',
      padding: 10,
    },
    block: {
      height: 50,
      width: '100%',
      backgroundColor: '#5d5d67',
      marginTop: 10,
      borderRadius: 10,
      justifyContent: 'center',
      alignItems: 'center',
    },
    avatar: {
      width: 48,
      height: 48,
      borderRadius: 48,
      borderWidth: 1,
      borderColor: '#f5f5f7',
      borderStyle: 'solid',
      marginLeft: 8,
      marginRight: 16,
    },
  });
}
