import {STATUSBAR_HEIGHT} from '@/utils/commonParam';
import React from 'react';
import {StyleSheet, Text, TouchableOpacity, View} from 'react-native';
import {Button} from 'react-native-elements';
import Icon from 'react-native-vector-icons/Ionicons';

interface BulletinMainViewHeaderProps {
  navigation: any;
}

export default class BulletinMainViewHeader extends React.Component<
  BulletinMainViewHeaderProps,
  {}
> {
  render() {
    return (
      <View style={this.styles.container}>
        <View style={{flexDirection: 'row', alignItems: 'center'}}>
          <Button
            icon={<Icon name="chevron-back" size={24} color="#1d1d1f" />}
            buttonStyle={{backgroundColor: 'white'}}
            onPress={() => this.props.navigation.goBack()}
          />
          <Text style={this.styles.text}>함께 참여할 친구 찾기</Text>
        </View>
        <TouchableOpacity
          style={this.styles.button}
          onPress={() => {
            this.props.navigation.navigate('bulletinSearch');
          }}>
          <Icon name="search" size={24} color="rgba(0, 0, 0, 0.3)" />
        </TouchableOpacity>
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
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 20,
      color: '#1d1d1f',
    },
    button: {
      backgroundColor: 'rgba(0, 0, 0, 0.05)',
      width: 40,
      height: 40,
      borderRadius: 20,
      marginRight: 20,
      alignItems: 'center',
      justifyContent: 'center',
    },
  });
}
