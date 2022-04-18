import NotificationController from '@/services/apis/NotificationController';
import {parseDate} from '@/utils/commonFunctions';
import * as React from 'react';
import {StyleSheet, Text, View, ScrollView} from 'react-native';
import {Button} from 'react-native-elements';
import Icon from 'react-native-vector-icons/Ionicons';
import {STATUSBAR_HEIGHT} from '../../utils/commonParam';
import MyNotificationBlock from './MyNotificationBlock';

export interface MyAlarmInfo {
  title: string;
  message: string;
  checked: boolean;
  notificationType: string;
  uuid: string;
  teamId: number;
  createdDate: string;
}

interface alarmListPageStates {
  data: MyAlarmInfo[];
  timeLine: string;
}

const testData = {
  title: 'title',
  message: 'message',
  checked: false,
  notificationType: 'ADMIN_CUSTOM',
  uuid: '275e1e28-329a-4d74-9e15-9aacfe684dc8',
  createdDate: '2021-09-30T05:38:26.946Z',
  teamId: 0,
};

class alarmListPage extends React.Component<{}, alarmListPageStates> {
  constructor(props: any) {
    super(props);
    this.state = {data: [testData], timeLine: ''};
    this.getMyNotification();
  }

  private async getMyNotification() {
    try {
      const res = await NotificationController.getMyNotification();
      this.setState({
        data: res.data.content,
      });
    } catch (e) {}
  }
  render() {
    return (
      <View style={this.styles.container}>
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
            onPress={() => this.props.navigation.goBack()}
          />
          <Text style={this.styles.text}>알림</Text>
        </View>
        <ScrollView style={{width: '100%'}}>
          {this.state.data.map((alarm: MyAlarmInfo) => (
            <MyNotificationBlock
              noti={alarm}
              navigation={this.props.navigation}
              timeLine={parseDate(alarm.createdDate).substr(5, 5)}
              curTimeLine={this.state.timeLine}
              onDateChange={(date: string) => this.setState({timeLine: date})}
            />
          ))}
        </ScrollView>
      </View>
    );
  }

  private styles = StyleSheet.create({
    container: {
      flex: 1,
      justifyContent: 'center',
      alignItems: 'center',
      backgroundColor: '#FFF',
    },
    text: {
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 20,
      color: '#1d1d1f',
    },
  });
}

export default alarmListPage;
