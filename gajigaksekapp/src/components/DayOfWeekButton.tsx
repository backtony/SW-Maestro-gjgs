import * as React from 'react';
import {StyleSheet} from 'react-native';
import {Text, TouchableOpacity} from 'react-native';

export default class DayOfWeekButton extends React.Component<
  {title: string; clicked: boolean; onClick: () => void; color: string},
  {}
> {
  render() {
    return (
      <TouchableOpacity
        style={[
          this.styles.button,
          this.props.clicked ? {backgroundColor: '#4f6cff'} : {},
        ]}
        onPress={() => {
          this.props.onClick();
        }}>
        <Text
          style={[
            {
              fontSize: 14,
              lineHeight: 16,
              fontFamily: 'NotoSansCJKkr-Regular',
              marginTop: 3,
              color: this.props.color,
            },
            this.props.clicked ? {color: '#FFF'} : {},
          ]}>
          {this.props.title}
        </Text>
      </TouchableOpacity>
    );
  }

  private styles = StyleSheet.create({
    button: {
      backgroundColor: 'rgba(245, 245, 247, 0.6)',
      justifyContent: 'center',
      alignItems: 'center',
      width: 27,
      height: 32,
      borderRadius: 6,
      marginRight: 10,
    },
  });
}
