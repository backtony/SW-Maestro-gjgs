import React from 'react';
import {StyleSheet, Text, View} from 'react-native';

interface BulletinSearchResultViewCountProps {
  totalElements: number;
}

export default class BulletinSearchResultViewCount extends React.Component<
  BulletinSearchResultViewCountProps,
  {}
> {
  render() {
    return (
      <View style={this.styles.wrapper}>
        <Text style={this.styles.text1}>{`${this.props.totalElements}개`}</Text>
        <Text style={{fontSize: 14}}>의 검색 결과</Text>
      </View>
    );
  }

  private styles = StyleSheet.create({
    wrapper: {flexDirection: 'row', marginLeft: 28, marginTop: 20},
    text1: {
      fontSize: 14,
      fontWeight: 'bold',
    },
  });
}
