import React from 'react';
import {ScrollView, StyleSheet, Text, View} from 'react-native';
import LectureDetailViewTimeButton from './LectureDetailViewTimeButton';
import {ScheduleInfo} from '../../LectureDetailView';

interface LectureDetailViewTimeSelectProps {
  lecture: any;
  selectedDate: any;
  selectedSchedule: ScheduleInfo;
  setSchedule: (schedule: ScheduleInfo) => void;
}

export default class LectureDetailViewTimeSelect extends React.Component<
  LectureDetailViewTimeSelectProps,
  {}
> {
  render() {
    return (
      <View>
        <View style={this.styles.textWrapper}>
          <Text style={this.styles.text}>시간 선택</Text>
        </View>
        <ScrollView
          style={{marginHorizontal: 20}}
          horizontal={true}
          showsVerticalScrollIndicator={false}
          showsHorizontalScrollIndicator={false}>
          {this.props.selectedDate ? (
            this.props.lecture.scheduleResponseList
              .filter(
                (schedule: ScheduleInfo) =>
                  schedule.lectureDate === this.props.selectedDate,
              )
              .map((schedule: ScheduleInfo) => (
                <LectureDetailViewTimeButton
                  key={schedule.scheduleId}
                  schedule={schedule}
                  maxParticipants={this.props.lecture.maxParticipants}
                  selectedId={
                    this.props.selectedSchedule &&
                    this.props.selectedSchedule.scheduleId
                      ? this.props.selectedSchedule.scheduleId
                      : 0
                  }
                  onPress={() => this.props.setSchedule(schedule)}
                />
              ))
          ) : (
            <Text />
          )}
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
