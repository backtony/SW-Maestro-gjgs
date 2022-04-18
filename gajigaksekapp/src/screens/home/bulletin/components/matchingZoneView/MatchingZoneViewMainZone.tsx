import React from 'react';
import {StyleSheet, Text, TouchableOpacity} from 'react-native';

interface MatchingZoneViewMainZoneProps {
  selectedMainZone: string;
  mainZone: string;
  setMainZone: (main: string) => void;
}

export default class MatchingZoneViewMainZone extends React.Component<
  MatchingZoneViewMainZoneProps,
  {}
> {
  render() {
    return (
      <TouchableOpacity
        style={[
          this.props.selectedMainZone === this.props.mainZone
            ? this.styles.buttonOn
            : this.styles.buttonOff,
          this.styles.button2,
        ]}
        onPress={() => {
          this.props.setMainZone(this.props.mainZone);
        }}>
        <Text
          style={[
            this.styles.buttonText,
            this.props.selectedMainZone === this.props.mainZone
              ? {}
              : {color: '#4a4a4c'},
          ]}>
          {this.props.mainZone}
        </Text>
      </TouchableOpacity>
    );
  }

  private styles = StyleSheet.create({
    buttonOn: {backgroundColor: '#4f6cff'},
    buttonOff: {backgroundColor: '#fafafb'},

    button2: {
      height: 40,
      borderRadius: 8,
      justifyContent: 'center',
      paddingLeft: 12,
      marginBottom: 10,
    },
    buttonText: {
      fontSize: 12,
      fontFamily: 'NotoSansCJKkr-Bold',
      lineHeight: 14,
      color: '#fff',
    },
  });
}
