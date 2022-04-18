import DayOfWeekButtons from '@/components/DayOfWeekButtons';
import GeneralButton from '@/components/GeneralButton';
import React from 'react';
import {Text, View} from 'react-native';

interface GroupEditDayBlockProps {
  clickedDayOfWeek: string[];
  onClickBigDayOfWeek: (bigDay: string) => void;
  bigDayChecked: (bigDay: string) => boolean;
  onClickDayOfWeek: (day: string) => void;
}

export default class GroupEditDayBlock extends React.Component<
  GroupEditDayBlockProps,
  {}
> {
  render() {
    return (
      <View>
        <Text
          style={{
            fontSize: 14,
            lineHeight: 16,
            fontFamily: 'NotoSansCJKkr-Regular',
            marginTop: 20,
            marginBottom: 10,
          }}>
          요일
        </Text>
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
}
