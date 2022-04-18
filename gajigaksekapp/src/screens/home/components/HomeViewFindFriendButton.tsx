import React from 'react';
import {Image, StyleSheet, Text, TouchableOpacity, View} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';

interface HomeViewFindFriendButtonProps {
  navigation: any;
}

export default class HomeViewFindFriendButton extends React.Component<
  HomeViewFindFriendButtonProps,
  {}
> {
  render() {
    return (
      <TouchableOpacity
        style={this.styles.button}
        onPress={() => {
          this.props.navigation.navigate('bulletinMain');
        }}>
        <Image
          style={this.styles.image}
          source={require('gajigaksekapp/src/asset/iconImage/main2.png')}
        />
        <View style={{marginLeft: 100}}>
          <Text style={this.styles.text1}>가지각색의 취미를</Text>
          <Text style={this.styles.text2}>함께할 친구 찾기</Text>
        </View>
        <Icon name="chevron-forward" size={25} color="#FFF" />
      </TouchableOpacity>
    );
  }

  private styles = StyleSheet.create({
    button: {
      height: 60,
      width: '100%',
      marginTop: 40,
      backgroundColor: '#2948ff',
      borderRadius: 10,
      flexDirection: 'row',
      alignItems: 'center',
      justifyContent: 'space-between',
      paddingHorizontal: 20,
    },
    image: {
      width: 99,
      height: 60,
      position: 'absolute',
      bottom: -10,
      left: 10,
    },
    text1: {
      fontSize: 12,
      lineHeight: 14,
      fontFamily: 'NotoSansCJKkr-Regular',
      color: 'rgba(255, 255, 255, 0.6)',
    },
    text2: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      color: '#FFF',
    },
  });
}
