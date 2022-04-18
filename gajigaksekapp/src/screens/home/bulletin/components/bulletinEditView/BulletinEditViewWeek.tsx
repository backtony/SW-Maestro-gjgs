import DayOfWeekButtons from '@/components/DayOfWeekButtons';
import GeneralButton from '@/components/GeneralButton';
import React from 'react';
import {StyleSheet, Text, View} from 'react-native';

interface BulletinEditViewWeekProps {
  onClickBigDayOfWeek: (bigDay: string) => void;
  bigDayChecked: (bigDay: string) => boolean;
  onClickDayOfWeek: (day: string) => void;
  clickedDayOfWeek: string[];
}

export default class BulletinEditViewWeek extends React.Component<
  BulletinEditViewWeekProps,
  {}
> {
  render() {
    return (
      <View>
        <Text style={this.styles.text}>가능한 요일</Text>
        <View style={{flexDirection: 'row'}}>
          <GeneralButton
            title={'주중'}
            onClick={() => {
              this.props.onClickBigDayOfWeek('주중');
            }}
            clicked={this.props.bigDayChecked('주중')}
          />
          <GeneralButton
            title={'주말'}
            onClick={() => {
              this.props.onClickBigDayOfWeek('주말');
            }}
            clicked={this.props.bigDayChecked('주말')}
          />
          <GeneralButton
            title={'무관'}
            onClick={() => {
              this.props.onClickBigDayOfWeek('무관');
            }}
            clicked={this.props.bigDayChecked('무관')}
          />
        </View>

        <DayOfWeekButtons
          clickedList={this.props.clickedDayOfWeek}
          onClick={(day: string) => this.props.onClickDayOfWeek(day)}
        />
      </View>
    );
  }

  private styles = StyleSheet.create({
    text: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      marginTop: 40,
      marginBottom: 10,
    },
  });
}
