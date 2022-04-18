import MatchingController from '@/services/apis/MatchingController';
import UserDC from '@/services/login/UserDC';
import React from 'react';
import {Image, StyleSheet, Text, TouchableOpacity, View} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import BulletinMainViewMatchingModal from './BulletinMainViewMatchingModal';

interface BulletinMainViewMatchingButtonProps {
  navigation: any;
}

export default class BulletinMainViewMatchingButton extends React.Component<
  BulletinMainViewMatchingButtonProps,
  {matchingStatus: string; modalVisible: boolean}
> {
  constructor(props: any) {
    super(props);
    this.state = {matchingStatus: 'NOT_MATCHING', modalVisible: false};
    if (!UserDC.isLogout()) {
      this.getMatchingStatus();
    }
  }

  private async getMatchingStatus() {
    try {
      const res = await MatchingController.getMatchingStatus();
      this.setState({matchingStatus: res.data.status});
    } catch (e) {}
  }
  render() {
    return (
      <TouchableOpacity
        style={this.styles.button}
        onPress={() => {
          if (this.state.matchingStatus === 'NOT_MATCHING') {
            this.props.navigation.navigate('matchingZone');
          } else {
            this.setState({modalVisible: true});
          }
        }}>
        <Image
          style={this.styles.img}
          source={require('gajigaksekapp/src/asset/iconImage/main2.png')}
        />
        <View style={{marginLeft: 100}}>
          <Text style={this.styles.text1}>가지각색의 취미를</Text>
          <Text style={this.styles.text2}>{`함께할 친구 매칭${
            this.state.matchingStatus === 'NOT_MATCHING' ? '하기' : '중'
          }`}</Text>
        </View>
        <Icon name="chevron-forward" size={25} color="#FFF" />
        <View>
          <BulletinMainViewMatchingModal
            modalVisible={this.state.modalVisible}
            navigation={this.props.navigation}
            setModalVisible={(visible: boolean) => {
              this.setState({modalVisible: visible});
            }}
            setParentSubZone={(sub: string) => {
              // this.setState({profileSubZone: sub});
            }}
          />
        </View>
      </TouchableOpacity>
    );
  }

  private styles = StyleSheet.create({
    button: {
      height: 60,
      backgroundColor: '#2948ff',
      borderRadius: 10,
      flexDirection: 'row',
      alignItems: 'center',
      justifyContent: 'space-between',
      paddingHorizontal: 20,
      marginHorizontal: 20,
      marginVertical: 20,
    },
    img: {
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
