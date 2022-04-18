import React from 'react';
import {View, Text} from 'react-native';

export default class SettingPageSpliter extends React.Component<
  {title: string},
  {}
> {
  render() {
    return (
      <View
        style={{
          height: 32,
          justifyContent: 'flex-end',
          paddingBottom: 4,
          paddingLeft: 20,
          backgroundColor: '#fafafb',
        }}>
        <Text
          style={{
            color: '#8e8e8f',
            fontSize: 12,
            lineHeight: 14,
            fontFamily: 'NotoSansCJKkr-Regular',
          }}>
          {this.props.title}
        </Text>
      </View>
    );
  }
}
