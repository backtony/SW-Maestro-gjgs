import {parseDate} from '@/utils/commonFunctions';
import React from 'react';
import {StyleSheet, Text, View} from 'react-native';
import {MyCouponInfo} from './CouponView';

export default class CouponBlock extends React.Component<MyCouponInfo, {}> {
  render() {
    return (
      <View style={this.styles.container}>
        <Text style={this.styles.text}>{this.props.title}</Text>
        <Text
          style={
            this.styles.text2
          }>{`${this.props.discountPrice}원 할인`}</Text>

        <Text style={this.styles.text3}>
          {`${parseDate(this.props.issueDate).substr(2, 8)} ~ ${parseDate(
            this.props.closeDate,
          ).substr(2, 8)}`}
        </Text>
      </View>
    );
  }

  private styles = StyleSheet.create({
    container: {
      // width: 350,
      height: 130,
      borderRadius: 6,
      backgroundColor: '#fafafb',
      borderStyle: 'solid',
      borderWidth: 1,
      borderColor: '#f5f5f7',
      padding: 20,
      marginTop: 10,
    },
    text: {
      fontFamily: 'NotoSansCJKkr-Regular',
      fontSize: 12,
      fontWeight: '500',
      fontStyle: 'normal',
      lineHeight: 16,
      letterSpacing: 0,
      color: '#1d1d1f',
    },
    text2: {
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 20,
      fontWeight: 'bold',
      fontStyle: 'normal',
      lineHeight: 24,
      letterSpacing: 0,
      color: '#4f6cff',
      marginTop: 4,
    },
    text3: {
      fontFamily: 'NotoSansCJKkr-Regular',
      fontSize: 12,
      fontWeight: 'normal',
      fontStyle: 'normal',
      lineHeight: 16,
      letterSpacing: 0,
      color: '#8e8e8f',
      marginTop: 10,
    },
  });
}
