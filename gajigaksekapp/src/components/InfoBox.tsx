import * as React from 'react';
import {StyleSheet} from 'react-native';
import {Text, View} from 'react-native';

export default class InfoBox extends React.Component<{title: string}, {}> {
  render() {
    return (
      <View style={this.styles.container}>
        <View style={this.styles.textWrapper}>
          <Text style={this.styles.text}>{this.props.title}</Text>
          {this.props.children}
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    container: {
      height: 70,
      backgroundColor: '#fafafb',
      width: '47%',
      alignItems: 'center',
      justifyContent: 'center',
      borderRadius: 6,
    },
    textWrapper: {
      alignItems: 'center',
      justifyContent: 'center',
    },
    text: {
      fontSize: 12,
      lineHeight: 14,
      fontFamily: 'NotoSansCJKkr-Regular',
      color: '#8e8e8f',
      marginBottom: 4,
    },
  });
}
