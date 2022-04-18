import React from 'react';
import {StyleSheet, Text, TouchableOpacity} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';

interface HomeViewFavoriteLocalHeaderProps {
  navigation: any;
}

export default class HomeViewFavoriteLocalHeader extends React.Component<
  HomeViewFavoriteLocalHeaderProps,
  {}
> {
  render() {
    return (
      <TouchableOpacity
        style={this.styles.header}
        onPress={() => this.props.navigation.navigate('favoriteZoneLecture')}>
        <Text style={this.styles.text}>관심지역</Text>
        <Icon name="chevron-forward" size={25} color="#8e8e8f" />
      </TouchableOpacity>
    );
  }

  private styles = StyleSheet.create({
    header: {
      flexDirection: 'row',
      alignItems: 'center',
      justifyContent: 'space-between',
      marginTop: 40,
      marginLeft: 10,
    },
    text: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      //   marginTop: 20,
      //   marginBottom: 10,
      color: '#1d1d1f',
    },
  });
}
