import {STATUSBAR_HEIGHT} from '@/utils/commonParam';
import React from 'react';
import {Alert, Image, StyleSheet, TouchableOpacity, View} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';

interface HomeViewHeaderProps {
  navigation: any;
}

export default class HomeViewHeader extends React.Component<
  HomeViewHeaderProps,
  {}
> {
  render() {
    return (
      <View
        style={[
          {
            marginTop: STATUSBAR_HEIGHT,
          },
          this.styles.header,
        ]}>
        <View style={{flexDirection: 'row', alignItems: 'center'}} />
        <TouchableOpacity
          onPress={() => Alert.alert('알림', '매칭이 완료되었습니다.')}>
          <Image
            style={{width: 77, height: 20, alignSelf: 'center'}}
            source={require('gajigaksekapp/src/asset/iconImage/logo.png')}
          />
        </TouchableOpacity>
        <TouchableOpacity
          style={this.styles.button}
          onPress={() => {
            this.props.navigation.navigate('lectureSearch');
          }}>
          <Icon name="search" size={24} color="rgba(0, 0, 0, 0.3)" />
        </TouchableOpacity>
      </View>
    );
  }

  private styles = StyleSheet.create({
    header: {
      height: 60,
      width: '100%',
      flexDirection: 'row',
      justifyContent: 'center',
      alignItems: 'center',
    },
    button: {
      backgroundColor: 'rgba(0, 0, 0, 0.05)',
      width: 40,
      height: 40,
      borderRadius: 20,
      marginRight: 20,
      alignItems: 'center',
      justifyContent: 'center',
      position: 'absolute',
      right: 0,
    },
  });
}
