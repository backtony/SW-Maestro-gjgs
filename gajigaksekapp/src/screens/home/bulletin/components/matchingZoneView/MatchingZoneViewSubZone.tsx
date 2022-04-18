import {ZonePair} from '@/utils/Zone';
import React from 'react';
import {StyleSheet, Text, TouchableOpacity} from 'react-native';

interface MatchingZoneViewSubZoneProps {
  subZoneInfo: ZonePair;
  selectedSubZone: string;
  setSubZone: (sub: string) => void;
}

export default class MatchingZoneViewSubZone extends React.Component<
  MatchingZoneViewSubZoneProps,
  {}
> {
  render() {
    return (
      <TouchableOpacity
        key={this.props.subZoneInfo.id}
        style={[
          this.styles.button2,
          this.props.selectedSubZone === this.props.subZoneInfo.sub
            ? this.styles.buttonOn
            : this.styles.buttonOff,
        ]}
        onPress={() => {
          if (this.props.selectedSubZone === this.props.subZoneInfo.sub) {
            this.props.setSubZone('');
          } else {
            this.props.setSubZone(this.props.subZoneInfo.sub);
          }
        }}>
        <Text
          style={[
            this.styles.buttonText,
            this.props.selectedSubZone === this.props.subZoneInfo.sub
              ? {}
              : {color: '#4a4a4c'},
          ]}>
          {this.props.subZoneInfo.sub}
        </Text>
      </TouchableOpacity>
    );
  }

  private styles = StyleSheet.create({
    buttonText: {
      fontSize: 12,
      fontFamily: 'NotoSansCJKkr-Bold',
      lineHeight: 14,
      color: '#fff',
    },
    button2: {
      height: 40,
      borderRadius: 8,
      justifyContent: 'center',
      paddingLeft: 12,
      marginBottom: 10,
    },
    buttonOn: {backgroundColor: '#4f6cff'},
    buttonOff: {backgroundColor: '#fafafb'},
  });
}
