import React from 'react';
import {ScrollView, StyleSheet, Text, View} from 'react-native';
import LectureDetailViewDateButton from './LectureDetailViewDateButton';
import {ScheduleInfo} from '../../LectureDetailView';

interface LectureDetailViewDateSelectProps {
  lecture: any;
  selectedDate: string;
  setSelectedDate: (date: string) => void;
}

export default class LectureDetailViewDateSelect extends React.Component<
  LectureDetailViewDateSelectProps,
  {}
> {
  render() {
    return (
      <View>
        <View style={this.styles.textWrapper}>
          <Text style={this.styles.text}>날짜 선택</Text>
        </View>
        <ScrollView
          style={{marginHorizontal: 20}}
          horizontal={true}
          showsVerticalScrollIndicator={false}
          showsHorizontalScrollIndicator={false}>
          {this.props.lecture &&
            this.props.lecture.scheduleResponseList &&
            this.props.lecture.scheduleResponseList.length > 0 &&
            Array.from(
              new Set(
                this.props.lecture.scheduleResponseList.map(
                  (schedule: ScheduleInfo) => schedule.lectureDate,
                ),
              ),
            ).map(lectureDate => (
              <LectureDetailViewDateButton
                key={JSON.stringify(lectureDate)}
                date={lectureDate as string}
                selectedDate={this.props.selectedDate}
                onPress={(date: string) =>
                  this.props.setSelectedDate(lectureDate as string)
                }
              />
            ))}
        </ScrollView>
      </View>
    );
  }

  private styles = StyleSheet.create({
    textWrapper: {
      flexDirection: 'row',
      marginTop: 40,
      alignItems: 'center',
      marginBottom: 20,
      marginHorizontal: 20,
    },
    text: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      marginTop: 7,
      marginLeft: 6,
    },
  });
}
