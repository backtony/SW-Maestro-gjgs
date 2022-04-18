import {STATUSBAR_HEIGHT} from '@/utils/commonParam';
import React from 'react';
import {StyleSheet} from 'react-native';
import {Text, View} from 'react-native';

interface FavoriteViewHeaderProps {
  title: string;
}

export default class FavoriteViewHeader extends React.Component<
  FavoriteViewHeaderProps,
  {}
> {
  render() {
    return (
      <View style={this.styles.container}>
        <Text style={this.styles.text}>{this.props.title}</Text>
      </View>
    );
  }

  private styles = StyleSheet.create({
    container: {
      marginTop: STATUSBAR_HEIGHT,
      height: 60,
      width: '100%',
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',
    },
    text: {
      marginLeft: 20,
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 20,
      color: '#1d1d1f',
    },
  });
}
