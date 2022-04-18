import React from 'react';
import {StyleSheet, Text} from 'react-native';
import {SceneMap, TabBar, TabView} from 'react-native-tab-view';
import CurriculumTab from '../../tab/CurriculumTab';
import IntroTab from '../../tab/IntroTab';
import QuestionTab from '../../tab/QuestionTab';
import ReviewTab from '../../tab/ReviewTab';
import ZoneTab from '../../tab/ZoneTab';

interface LectureDetailViewTabViewProps {
  navigation: any;
  width: number;
  questionList: any[];
  lecture: any;
  refreshParent: () => void;
}

export default class LectureDetailViewTabView extends React.Component<
  LectureDetailViewTabViewProps,
  {}
> {
  render() {
    return (
      <TabView
        renderTabBar={props => (
          <TabBar
            style={{backgroundColor: 'white'}}
            indicatorStyle={{
              backgroundColor: '#4f6cff',
              height: 4,
              width: 40,
              alignSelf: 'center',
              // marginLeft: 25,
              borderRadius: 2,
              marginHorizontal: ((this.props.width - 10) / 5 - 40) / 2,
            }}
            renderLabel={({route, focused, color}) => (
              <Text
                style={[this.styles.text, focused ? {color: '#4f6cff'} : {}]}>
                {route.title}
              </Text>
            )}
            {...props}
          />
        )}
        style={{height: 500, marginHorizontal: 10, marginTop: 36}}
        navigationState={{
          index: 0,
          routes: [
            {key: 'intro', title: '소개'},
            {key: 'curriculum', title: '커리큘럼'},
            {key: 'zone', title: '장소'},
            {key: 'review', title: '리뷰'},
            {
              key: 'question',
              title: `문의(${this.props.questionList.length})`,
            },
          ],
        }}
        onIndexChange={index => {
          this.setState({tabIndex: index});
        }}
        renderScene={SceneMap({
          intro: () => (
            <IntroTab
              finishedProductList={
                this.props.lecture.finishedProductResponseList
              }
            />
          ),
          curriculum: () => (
            <CurriculumTab
              curriculumList={this.props.lecture.curriculumResponseList}
            />
          ),
          zone: ZoneTab,
          review: () => (
            <ReviewTab
              navigation={this.props.navigation}
              lectureId={this.props.lecture.lectureId}
            />
          ),
          question: () => (
            <QuestionTab
              navigation={this.props.navigation}
              lectureId={this.props.lecture.lectureId}
              lecture={this.props.lecture}
              refreshParent={() => this.props.refreshParent()}
            />
          ),
        })}
      />
    );
  }

  private styles = StyleSheet.create({
    text: {
      fontSize: 14,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Regular',
      color: '#8e8e8f',
    },
  });
}
