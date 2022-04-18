import React from 'react';
import {StyleSheet, Text, View} from 'react-native';

interface MatchingZoneViewSmallZoneProps {
  selectedSubZone: string;
}

export default class MatchingZoneViewSmallZone extends React.Component<
  MatchingZoneViewSmallZoneProps,
  {}
> {
  render() {
    return (
      <View style={this.styles.textWrapper}>
        <Text style={[this.styles.buttonText, {color: '#4f6cff'}]}>
          {this.props.selectedSubZone}
        </Text>
      </View>
    );
  }

  private styles = StyleSheet.create({
    buttonText: {
      fontSize: 12,
      fontFamily: 'NotoSansCJKkr-Bold',
      lineHeight: 14,
      color: '#fff',
    },
    textWrapper: {
      height: 28,
      backgroundColor: '#f5f7ff',
      alignItems: 'center',
      justifyContent: 'center',
      paddingHorizontal: 10,
      borderRadius: 6,
      marginLeft: 5,
    },
  });
}
