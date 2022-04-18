import DayOfWeekButtons from '@/components/DayOfWeekButtons';
import GeneralButton from '@/components/GeneralButton';
import React from 'react';
import {StyleSheet, Text, View} from 'react-native';

interface MatchingDetailViewWeekProps {
  clickedDayOfWeek: string[];
  onClickBigDayOfWeek: (bigDay: string) => void;
  onClickDayOfWeek: (day: string) => void;
  bigDayChecked: (bigDay: string) => boolean;
}

const bigDayList = ['주중', '주말', '무관'];

export default class MatchingDetailViewWeek extends React.Component<
  MatchingDetailViewWeekProps,
  {}
> {
  render() {
    return (
      <View>
        <Text style={this.styles.text}>가능한 요일</Text>
        <View style={{flexDirection: 'row'}}>
          {bigDayList.map((bigDay: string) => (
            <GeneralButton
              title={bigDay}
              onClick={() => {
                this.props.onClickBigDayOfWeek(bigDay);
              }}
              clicked={this.props.bigDayChecked(bigDay)}
            />
          ))}
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
