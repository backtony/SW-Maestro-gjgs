import * as React from 'react';
import {StyleSheet} from 'react-native';
import {View} from 'react-native';
import {DAY_OF_WEEK} from '../utils/commonParam';
import DayOfWeekButton from './DayOfWeekButton';

interface DayOfWeekButtonsProps {
  clickedList: string[];
  onClick: (day: string) => void;
}

export default class DayOfWeekButtons extends React.Component<
  DayOfWeekButtonsProps,
  {}
> {
  render() {
    return (
      <View style={this.styles.container}>
        {DAY_OF_WEEK.map(item => {
          return (
            <DayOfWeekButton
              key={item[0]}
              title={item[0]}
              clicked={this.props.clickedList.includes(item[2])}
              color={item[1]}
              onClick={() => {
                this.props.onClick(item[2]);
              }}
            />
          );
        })}
      </View>
    );
  }

  private styles = StyleSheet.create({
    container: {marginTop: 10, flexDirection: 'row'},
  });
}
