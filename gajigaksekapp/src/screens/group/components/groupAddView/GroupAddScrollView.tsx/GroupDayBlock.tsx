import React from 'react';
import {StyleSheet} from 'react-native';
import {Text, View} from 'react-native';
import DayOfWeekButtons from '../../../../../components/DayOfWeekButtons';
import GeneralButton from '../../../../../components/GeneralButton';

interface GroupDayBlockProps {
  clickedDayOfWeek: string[];
  onClickBigDayOfWeek: (bigDay: string) => void;
  bigDayChecked: (bigDay: string) => boolean;
  onClickDayOfWeek: (day: string) => void;
}

export default class GroupDayBlock extends React.Component<
  GroupDayBlockProps,
  {}
> {
  render() {
    return (
      <View>
        <Text style={this.styles.container}>요일</Text>
        <View style={this.styles.innerContainer}>
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
    container: {
      fontSize: 14,
      lineHeight: 16,
      fontFamily: 'NotoSansCJKkr-Regular',
      marginTop: 20,
      marginBottom: 10,
    },
    innerContainer: {flexDirection: 'row'},
  });
}
