import MypageApiController from '@/services/apis/MypageApiController';
import {STATUSBAR_HEIGHT} from '@/utils/commonParam';
import React from 'react';
import {StyleSheet, Switch, Text, View} from 'react-native';
import {Button} from 'react-native-elements';
import Icon from 'react-native-vector-icons/Ionicons';

interface NotificationSettingViewStates {
  alarm: boolean;
}

export default class NotificationSettingView extends React.Component<
  {},
  NotificationSettingViewStates
> {
  constructor(props: any) {
    super(props);
    this.state = {alarm: false};
  }

  componentDidMount() {
    this.getAlarmStatus();
  }

  private async getAlarmStatus() {
    try {
      const res = await MypageApiController.getAlarmStatus();
      const data = res?.data;
      this.setState({alarm: data.eventAlarm});
    } catch (e) {}
  }

  private async postAlarmStatus(alarmStatus: boolean) {
    const params = {
      active: alarmStatus,
      type: 'EVENT',
    };
    try {
      await MypageApiController.postAlarmStatus(params);
      this.setState({alarm: alarmStatus});
    } catch (e) {}
  }

  render() {
    return (
      <View
        style={{
          flex: 1,
          //   justifyContent: 'center',
          alignItems: 'center',
          backgroundColor: '#FFF',
        }}>
        <View
          style={{
            marginTop: STATUSBAR_HEIGHT,
            height: 60,
            width: '100%',
            flexDirection: 'row',
            justifyContent: 'flex-start',
            alignItems: 'center',
          }}>
          <Button
            icon={<Icon name="chevron-back" size={24} color="#1d1d1f" />}
            buttonStyle={{backgroundColor: 'white'}}
            onPress={() => {
              this.props.navigation.goBack();
            }}
          />
          <Text
            style={{
              // width: 93,
              // height: 20,
              fontFamily: 'NotoSansCJKkr-Bold',
              fontSize: 20,
              color: '#1d1d1f',
            }}>
            알림 설정
          </Text>
        </View>
        <View>
          <View style={this.styles.wrapper}>
            <View>
              <Text style={this.styles.text}>
                {'이벤트 및 혜택 푸시 알림 수신'}
              </Text>
              <Text style={this.styles.text2}>
                {'가지각색에서 준비한 이벤트 및 혜택을 알려드립니다'}
              </Text>
            </View>
            <View>
              <Switch
                trackColor={{false: '#d2d2d2', true: '#4f6cff'}}
                onValueChange={() => this.postAlarmStatus(!this.state.alarm)}
                value={this.state.alarm}
              />
            </View>
          </View>
          <View style={{width: 320, height: 1, backgroundColor: '#f5f5f7'}} />
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    wrapper: {
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',
      height: 80,
    },
    text: {
      fontFamily: 'NotoSansCJKkr-Regular',
      fontSize: 16,
      fontWeight: '500',
      fontStyle: 'normal',
      lineHeight: 20,
      letterSpacing: 0,
      color: '#111111',
    },
    text2: {
      fontFamily: 'NotoSansCJKkr-Regular',
      fontSize: 12,
      fontWeight: 'normal',
      fontStyle: 'normal',
      lineHeight: 16,
      letterSpacing: 0,
      color: '#8e8e8f',
    },
    text3: {
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 16,
      fontWeight: 'bold',
      fontStyle: 'normal',
      lineHeight: 20,
      letterSpacing: 0,
      textAlign: 'right',
      color: '#4f6cff',
    },
  });
}
