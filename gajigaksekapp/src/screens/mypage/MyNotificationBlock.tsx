import {parseDate} from '@/utils/commonFunctions';
import React from 'react';
import {Image, StyleSheet, Text, TouchableOpacity, View} from 'react-native';
import {MyAlarmInfo} from './alaramListPage';

interface MyNotificationBlockProps {
  noti: MyAlarmInfo;
  navigation: any;
  timeLine: string;
  curTimeLine: string;
  onDateChange: (date: string) => void;
}

export default class MyNotificationBlock extends React.Component<
  MyNotificationBlockProps,
  {}
> {
  render() {
    const on: boolean = this.props.timeLine !== this.props.curTimeLine;
    if (on) {
      this.props.onDateChange(this.props.timeLine);
    }
    return (
      <View>
        {on && (
          <View
            style={{
              height: 44,
              justifyContent: 'flex-end',
              paddingBottom: 4,
              paddingLeft: 20,
            }}>
            <Text
              style={{
                color: '#1d1d1f',
                fontSize: 16,
                lineHeight: 18,
                fontFamily: 'NotoSansCJKkr-Regular',
              }}>
              {parseDate(this.props.noti.createdDate).substr(5, 5)}
            </Text>
          </View>
        )}

        <TouchableOpacity
          onPress={() =>
            this.props.navigation.navigate('notiDetail', {
              noti: this.props.noti,
            })
          }>
          <View
            style={{
              flexDirection: 'row',
              alignItems: 'center',
              paddingVertical: 10,
            }}>
            <View
              style={[
                {
                  width: 4,
                  height: 4,
                  borderRadius: 4,
                  backgroundColor: '#4f6cff',
                  marginLeft: 8,
                },
                this.props.noti.checked ? {backgroundColor: 'transparent'} : {},
              ]}
            />
            <Image
              style={this.styles.avatar}
              source={{
                uri: 'https://bootdey.com/img/Content/avatar/avatar6.png',
              }}
            />
            <View>
              <Text
                style={{
                  color: '#1d1d1f',
                  fontSize: 16,
                  lineHeight: 18,
                  fontFamily: 'NotoSansCJKkr-Regular',
                  width: 256,
                  paddingTop: 11,
                }}>
                {this.props.noti.message}
              </Text>
              <Text
                style={{
                  color: '#8e8e8f',
                  fontSize: 12,
                  lineHeight: 14,
                  fontFamily: 'NotoSansCJKkr-Regular',
                  marginTop: 2,
                }}>
                {parseDate(this.props.noti.createdDate)}
              </Text>
            </View>
          </View>
          <View style={{flex: 1, height: 1, backgroundColor: '#f5f5f7'}} />
        </TouchableOpacity>
      </View>
    );
  }

  private styles = StyleSheet.create({
    flatList: {
      width: '100%',
      padding: 10,
    },
    block: {
      height: 50,
      width: '100%',
      backgroundColor: '#5d5d67',
      marginTop: 10,
      borderRadius: 10,
      justifyContent: 'center',
      alignItems: 'center',
    },
    avatar: {
      width: 48,
      height: 48,
      borderRadius: 48,
      borderWidth: 1,
      borderColor: '#f5f5f7',
      borderStyle: 'solid',
      // marginBottom: 10,
      // alignSelf: 'center',
      // position: 'absolute',
      // marginTop: 130,
      marginLeft: 8,
      marginRight: 16,
    },
  });
}
