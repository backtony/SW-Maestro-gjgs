import {DAY_OF_WEEK} from '@/utils/commonParam';
import React from 'react';
import {StyleSheet, Text, TouchableOpacity} from 'react-native';

interface LectureDetailViewDateButtonProps {
  selectedDate: string;
  date: string;
  onPress: (date: string) => void;
}

export default class LectureDetailViewDateButton extends React.Component<
  LectureDetailViewDateButtonProps,
  {}
> {
  render() {
    const dateObj = new Date(this.props.date);
    return (
      <TouchableOpacity
        style={[
          this.styles.button,
          this.props.selectedDate === this.props.date
            ? {backgroundColor: '#4f6cff'}
            : {},
        ]}
        onPress={() => {
          this.props.onPress(this.props.date);
        }}>
        <Text
          style={[
            this.styles.text,
            this.props.selectedDate === this.props.date ? {color: '#FFF'} : {},
          ]}>
          {`${dateObj.getMonth()}/${dateObj.getDate()} ${
            DAY_OF_WEEK[dateObj.getDay()][0]
          }`}
        </Text>
      </TouchableOpacity>
    );
  }

  private styles = StyleSheet.create({
    button: {
      padding: 10,
      backgroundColor: '#f5f5f7',
      borderRadius: 6,
      marginRight: 10,
    },
    text: {
      fontSize: 14,
      lineHeight: 16,
      fontFamily: 'NotoSansCJKkr-Regular',
      color: '#1d1d1f',
    },
  });
}
