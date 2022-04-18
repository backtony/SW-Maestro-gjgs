import React from 'react';
import {StyleSheet} from 'react-native';
import {Text, TouchableOpacity, View} from 'react-native';

import Icon from 'react-native-vector-icons/Ionicons';
import {ScheduleInfo} from '../../LectureDetailView';

interface LectureDashTimeButtonProps {
  selectedId: number;
  schedule: ScheduleInfo;
  maxParticipants: number;
  onPress: () => void;
}

export default class LectureDetailViewTimeButton extends React.Component<
  LectureDashTimeButtonProps,
  {}
> {
  render() {
    const selected: boolean =
      this.props.selectedId === this.props.schedule.scheduleId;
    return (
      <TouchableOpacity
        style={[
          this.styles.button,
          selected ? {backgroundColor: '#4f6cff'} : {},
        ]}
        onPress={() => this.props.onPress()}>
        <Text style={[this.styles.text, selected ? {color: '#FFF'} : {}]}>
          {`${this.props.schedule.startHour}:${
            this.props.schedule.startMinute === 0 ? '00' : '30'
          } ~ ${this.props.schedule.endHour}:${
            this.props.schedule.endMinute === 0 ? '00' : '30'
          }`}
        </Text>
        <View style={this.styles.view}>
          <Icon name="person" size={14} color={selected ? '#FFF' : '#4a4a4c'} />
          <Text
            style={
              selected ? {color: '#FFF'} : {color: '#4a4a4c'}
            }>{`${this.props.schedule.currentParticipants}/`}</Text>
          <Text style={selected ? {color: '#B5C4FF'} : {color: '#8e8e8f'}}>
            {this.props.maxParticipants}
          </Text>
        </View>
      </TouchableOpacity>
    );
  }

  private styles = StyleSheet.create({
    button: {
      padding: 15,
      backgroundColor: '#f5f5f7',
      borderRadius: 6,
      marginRight: 10,
    },
    text: {
      fontSize: 14,
      lineHeight: 16,
      fontFamily: 'NotoSansCJKkr-Bold',
      color: '#1d1d1f',
    },
    view: {
      flexDirection: 'row',
      alignItems: 'center',
      marginTop: 5,
    },
  });
}
