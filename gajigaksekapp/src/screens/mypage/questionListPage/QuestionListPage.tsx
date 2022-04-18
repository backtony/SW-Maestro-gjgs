import * as React from 'react';
import {StyleSheet, Text, View, FlatList, TouchableOpacity} from 'react-native';
import {Button} from 'react-native-elements';
import questionApi from '../../../services/apis/questionApi';
import {STATUSBAR_HEIGHT} from '../../../utils/commonParam';
import Icon from 'react-native-vector-icons/Ionicons';
import {ScrollView} from 'react-native-gesture-handler';
import QuestionItem from './QuestionItem';
import MypageApiController from '../../../services/apis/MypageApiController';

class QuestionListPage extends React.Component<{}, {questionList: any}> {
  constructor() {
    super();
    this.state = {questionList: []};
    this.getQuestion();
  }

  private async getQuestion() {
    try {
      const res = await MypageApiController.getQuestion({});
      console.log('res: ', res?.data.myQuestionDtoList);
      this.setState({questionList: res?.data.myQuestionDtoList});
    } catch (e) {
      console.error(e);
    }
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

    return `${minute}분 전`;
  }

  render() {
    return (
      <View
        style={{
          flex: 1,
          justifyContent: 'center',
          alignItems: 'center',
          backgroundColor: '#FFF',
        }}>
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
            나의 문의
          </Text>
        </View>
        <ScrollView style={{width: '100%'}}>
          {this.state.questionList.map(question => (
            <TouchableOpacity
              onPress={() => {
                this.props.navigation.navigate('lectureQuestionDash', {
                  questionId: question.questionId,
                  bottom: true,
                });
              }}>
              <QuestionItem
                lectureName={question.classTitle}
                title={question.mainText}
                createdAt={this.timeLapse(question.questionDate)}
                bottomBorder={true}
                pending={question.status === 'WAIT'}
              />
            </TouchableOpacity>
          ))}
          {/* <QuestionItem
            lectureName={'다이빙 클래스'}
            title={'수강 기간변경 가능할까요?'}
            createdAt={'1일 전'}
            bottomBorder={true}
            pending={true}
          />
          <QuestionItem
            lectureName={'처음부터하는 제과제빵'}
            title={'수업 신청 취소문의 드려요.'}
            createdAt={'2개월 전'}
            bottomBorder={true}
            pending={false}
          />
          <QuestionItem
            lectureName={'파스타로 시작하는 양식 클래스'}
            title={'재수강 신청 요청 방법'}
            createdAt={'1년 전'}
            bottomBorder={false}
            pending={false}
          /> */}
        </ScrollView>
      </View>
    );
  }

  private styles = StyleSheet.create({
    flatList: {
      width: '100%',
      padding: 10,
    },
    block: {
      //   height: 50,
      width: '100%',
      backgroundColor: '#bdc3c7',
      marginTop: 10,
      borderRadius: 10,
      justifyContent: 'space-around',
      alignItems: 'center',
      flexDirection: 'row',
    },
  });
}

export default QuestionListPage;
